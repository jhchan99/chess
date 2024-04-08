package server.websocket;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseAuth;
import dataAccess.DatabaseGame;
import dataAccess.DatabaseUser;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

// this is also my websocket handler

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebSocket
public class WebSocketServer {

    DatabaseAuth databaseAuth = new DatabaseAuth();
    DatabaseGame databaseGame = new DatabaseGame();
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketServer() throws DataAccessException, SQLException {
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(command.gameID(), command.getAuthString(), command.teamColor(), session);
//            case JOIN_OBSERVER -> joinObserver(command.gameID(), command.getAuthString(), session);
//            case LEAVE -> leave(command.gameID(), command.getAuthString());
//            case MAKE_MOVE -> makeMove(command.gameID(), command.getAuthString(), command.getMove());
//            case RESIGN -> resign(command.gameID(), command.getAuthString());
        }
    }

    private void joinPlayer(Integer gameID, String auth, ChessGame.TeamColor teamColor, Session session) {
        try {

            AuthData userAuth = databaseAuth.getAuth(auth);
            GameData game =  databaseGame.getGame(gameID);
            // if team color is white check the game to make sure the player has been correctly added to that spot
            if (teamColor.equals(ChessGame.TeamColor.WHITE) &&
                    Objects.equals(game.whiteUsername(), userAuth.username())) {
                connections.add(gameID, session, auth);
                connections.broadcast(gameID, String.format("%s has been added to the game as white", game.whiteUsername()));
            } else if (teamColor.equals(ChessGame.TeamColor.BLACK) &&
            Objects.equals(game.blackUsername(), userAuth.username())) {
                connections.add(gameID, session, auth);
                connections.broadcast(gameID, String.format("%s has been added to the game", game.blackUsername()));
            } else {
                // send error message back as server message
                ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, "You are not a player in this game");
                session.getRemote().sendString(new Gson().toJson(serverMessage));
            }

        }   catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
