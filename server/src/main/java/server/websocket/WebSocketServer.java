package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import dataAccess.DatabaseAuth;
import dataAccess.DatabaseGame;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import service.Service;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.messages.Error;
import webSocketMessages.serverMessages.messages.LoadGame;

// this is also my websocket handler

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.messages.Notification;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.commands.*;

import java.io.IOException;
import java.sql.SQLException;

@WebSocket
public class WebSocketServer {

    DatabaseAuth databaseAuth = new DatabaseAuth();
    DatabaseGame databaseGame = new DatabaseGame();
    Service service = new Service();
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketServer() throws DataAccessException, SQLException {
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(message, session);
            case JOIN_OBSERVER -> joinObserver(message, session);
            case LEAVE -> leave(message, session);
            case MAKE_MOVE -> makeMove(message, session);
            case RESIGN -> resign(message, session);
        }
    }

    private void makeMove(String message, Session session) {
        try {
            MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
            GameData game = databaseGame.getGame(makeMove.getGameID());
            AuthData userAuth = databaseAuth.getAuth(makeMove.getAuthString());

            checkErrors(game, userAuth, session);

            if(game.game().isGameOver()){
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: game is over")));
                return;
            }
            if (!userAuth.username().equals(game.whiteUsername()) && !userAuth.username().equals(game.blackUsername())) {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Not a player in this game")));
                return;
            }
            if (userAuth.username().equals(game.whiteUsername()) && game.game().getTeamTurn() == ChessGame.TeamColor.BLACK) {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Not your turn")));
                return;
            }
            if (userAuth.username().equals(game.blackUsername()) && game.game().getTeamTurn() == ChessGame.TeamColor.WHITE) {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Not your turn")));
                return;
            }

            ChessMove move = makeMove.getMove();
            // check if move is valid
            if (game.game().validMoves(move.start).contains(move)) {
                try {
                    game.game().makeMove(move);
                    databaseGame.updateGame(game);
                    connections.broadcastJoinGame(game.gameID(), String.format("%s has made a move", userAuth.username()), userAuth.authToken());
                    connections.sendLoadGame(game.gameID(), new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.game())));

                    if (game.game().isInCheck(game.game().getTeamTurn()) ) {
                        if (game.game().isInCheckmate(game.game().getTeamTurn())) {
                            connections.broadcastJoinGame(game.gameID(), String.format("%s is in checkmate", userAuth.username()), userAuth.authToken());
                        } else {
                            connections.broadcastJoinGame(game.gameID(), String.format("%s is in check", userAuth.username()), userAuth.authToken());
                        }
                    }
                    if (game.game().isInStalemate(game.game().getTeamTurn())) {
                        connections.broadcastJoinGame(game.gameID(), String.format("%s is in stalemate", userAuth.username()), userAuth.authToken());
                    }

                } catch (InvalidMoveException e) {
                    session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Invalid move")));
                }
            } else {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Invalid move")));
            }
        } catch (DataAccessException | SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void resign(String message, Session session) {
        try {
            Resign resign = new Gson().fromJson(message, Resign.class);
            GameData game = databaseGame.getGame(resign.getGameID());
            AuthData userAuth = databaseAuth.getAuth(resign.getAuthString());

            if (game.game().isGameOver()) {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: game is already over")));
                return;
            }
            if (userAuth == null) {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Auth token not found")));
                return;
            }
            if (!userAuth.username().equals(game.whiteUsername()) && !userAuth.username().equals(game.blackUsername())) {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Not a player in this game")));
                return;
            }
            // set game to over
            game.game().gameOverResign();
            databaseGame.updateGame(game);
            // cannot remove players from game
            connections.broadcastGameOver(game.gameID(), String.format("%s has resigned", userAuth.username()));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    private void leave(String msg, Session session) {
        try {
            Leave leave = new Gson().fromJson(msg, Leave.class);
            AuthData userAuth = databaseAuth.getAuth(leave.getAuthString());
            GameData game = databaseGame.getGame(leave.getGameID());
            checkErrors(game, userAuth, session);
            service.removePlayer(game.gameID(), userAuth.username());
            connections.broadcastJoinGame(game.gameID(), String.format("%s has left the game", userAuth.username()), userAuth.authToken());
            connections.remove(game.gameID(), userAuth.authToken());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void joinObserver(String message, Session session) {
        try {
            JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
            AuthData userAuth = databaseAuth.getAuth(joinObserver.getAuthString());
            GameData game = databaseGame.getGame(joinObserver.getGameID());
            checkErrors(game, userAuth, session);
            connections.add(game.gameID(), session, userAuth.authToken());
            connections.broadcastJoinGame(game.gameID(), String.format("%s has joined the game as an observer", userAuth.username()), userAuth.authToken());
            session.getRemote().sendString(new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.game())));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void joinPlayer(String msg, Session session) {
        try {
            JoinPlayer joinPlayer = new Gson().fromJson(msg, JoinPlayer.class);
            ChessGame.TeamColor color = joinPlayer.getColor();
            // check if auth exists in database
            AuthData userAuth = databaseAuth.getAuth(joinPlayer.getAuthString());
            GameData game = databaseGame.getGame(joinPlayer.getGameID());

            checkErrors(game, userAuth, session);

            // if team color is white check the game to make sure the player has been correctly added to that spot
            if (joinPlayer.getColor() == ChessGame.TeamColor.WHITE) {
                if (game.whiteUsername() == null || !game.whiteUsername().equals(userAuth.username())) {
                    session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: White player is taken")));
                } else {
                    connections.add(game.gameID(), session, userAuth.authToken());
                    connections.broadcastJoinGame(game.gameID(), String.format("%s has joined the game", userAuth.username()), userAuth.authToken());
                    session.getRemote().sendString(new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.game())));
                }
            }
            // if team color is black check the game to make sure the player has been correctly added to that spot
            if (joinPlayer.getColor() == ChessGame.TeamColor.BLACK) {
                if (game.blackUsername() == null || !game.blackUsername().equals(userAuth.username())) {
                    session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Black player is taken")));
                } else {
                    connections.add(game.gameID(), session, userAuth.authToken());
                    connections.broadcastJoinGame(game.gameID(), String.format("%s has joined the game", userAuth.username()), userAuth.authToken());
                    session.getRemote().sendString(new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.game())));
                }
            }



        }   catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    private void checkErrors(GameData game, AuthData userAuth, Session session) throws IOException {
        if (game == null) {
            session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Game not found")));
            return;
        }
        if (userAuth == null) {
            session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Auth token not found")));
            return;
        }

    }

}
