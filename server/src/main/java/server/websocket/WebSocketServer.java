package server.websocket;

import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import service.WebSocketService;
import webSocketMessages.userCommands.UserGameCommand;

// this is also my websocket handler

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

@WebSocket
public class WebSocketServer {

    WebSocketService webSocketService = new WebSocketService();
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(command.gameID(), command.getAuthString(), session);
//            case JOIN_OBSERVER -> joinObserver(command.gameID(), command.getAuthString(), session);
//            case LEAVE -> leave(command.gameID(), command.getAuthString());
//            case MAKE_MOVE -> makeMove(command.gameID(), command.getAuthString(), command.getMove());
//            case RESIGN -> resign(command.gameID(), command.getAuthString());
        }
    }

    private void joinPlayer(Integer gameID, String auth, Session session){
        try {
            connections.add(gameID, session, auth);
            GameData game = webSocketService.gameAccess.getGame(gameID);
            connections.broadcast(gameID, String.format("%s joined the game", user.username()));
        }   catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
