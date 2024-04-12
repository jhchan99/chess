package server.websocket;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseAuth;
import dataAccess.DatabaseGame;
import dataAccess.DatabaseUser;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import service.Service;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.messages.Error;
import webSocketMessages.serverMessages.messages.LoadGame;
import webSocketMessages.serverMessages.messages.Notification;
import webSocketMessages.userCommands.UserGameCommand;

// this is also my websocket handler

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.commands.JoinObserver;
import webSocketMessages.userCommands.commands.JoinPlayer;
import webSocketMessages.userCommands.commands.MakeMove;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

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
//            case LEAVE -> leave(command.getAuthString());
            case MAKE_MOVE -> makeMove(message, session);
//            case RESIGN -> resign(command.gameID(), command.getAuthString());
        }
    }

    private void makeMove(String message, Session session) {
        try {
            MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
            AuthData userAuth = databaseAuth.getAuth(makeMove.getAuthString());
            GameData game = databaseGame.getGame(makeMove.getGameID());
            service.

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void resign(Integer gameID, String auth) {
        try {
            AuthData userAuth = databaseAuth.getAuth(auth);
            GameData game = databaseGame.getGame(gameID);
            // mark game as over no more moves can be made
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void leave(String auth) {
        try {
            Integer gameID = connections.getGameID(auth);
            AuthData userAuth = databaseAuth.getAuth(auth);
            service.removePlayer(gameID, userAuth.username());
            connections.remove(gameID, auth);
            connections.broadcastJoinGame(gameID, String.format("%s has left the game", userAuth.username()), auth);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void joinObserver(String message, Session session) {
        try {
            JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
            AuthData userAuth = databaseAuth.getAuth(joinObserver.getAuthString());
            GameData game = databaseGame.getGame(joinObserver.getGameID());
            if(game == null) {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Game not found")));
                return;
            }
            if(userAuth == null) {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Auth token not found")));
                return;
            }
            connections.add(game.gameID(), session, userAuth.authToken());
            connections.broadcastJoinGame(game.gameID(), String.format("%s has joined the game as an observer", userAuth.username()), userAuth.authToken());
            session.getRemote().sendString(new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.game(), null)));

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void joinPlayer(String msg, Session session) {
        try {
            JoinPlayer joinPlayer = new Gson().fromJson(msg, JoinPlayer.class);
            ChessGame.TeamColor color = joinPlayer.getColor();
            // check if auth exists in database
            if(databaseAuth.getAuth(joinPlayer.getAuthString()) == null) {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Auth token not found")));
                return;
            }
            if (databaseGame.getGame(joinPlayer.getGameID()) == null) {
                session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Game not found")));
                return;
            }
            AuthData userAuth = databaseAuth.getAuth(joinPlayer.getAuthString());
            // check if game exists
            GameData game = databaseGame.getGame(joinPlayer.getGameID());
            // if team color is white check the game to make sure the player has been correctly added to that spot
            if (joinPlayer.getColor() == ChessGame.TeamColor.WHITE) {
                if (game.whiteUsername() == null || !game.whiteUsername().equals(userAuth.username())) {
                    session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: White player is taken")));
                } else {
                    connections.add(game.gameID(), session, userAuth.authToken());
                    connections.broadcastJoinGame(game.gameID(), String.format("%s has joined the game", userAuth.username()), userAuth.authToken());
                    session.getRemote().sendString(new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.game(), color)));
                }
            }
            // if team color is black check the game to make sure the player has been correctly added to that spot
            if (joinPlayer.getColor() == ChessGame.TeamColor.BLACK) {
                if (game.blackUsername() == null || !game.blackUsername().equals(userAuth.username())) {
                    session.getRemote().sendString(new Gson().toJson(new Error(ServerMessage.ServerMessageType.ERROR, "error: Black player is taken")));
                } else {
                    connections.add(game.gameID(), session, userAuth.authToken());
                    connections.broadcastJoinGame(game.gameID(), String.format("%s has joined the game", userAuth.username()), userAuth.authToken());
                    session.getRemote().sendString(new Gson().toJson(new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.game(), color)));
                }
            }



        }   catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
