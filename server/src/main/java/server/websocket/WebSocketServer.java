package server.websocket;

import webSocketMessages.userCommands.UserGameCommand;

// this is also my websocket server

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(command.getAuthString(), session);

        }
    }

    private void joinPlayer(String auth, Session session){
        connections.add();
        try {

        }
    }
}
