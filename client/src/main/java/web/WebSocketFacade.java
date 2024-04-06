package web;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class WebSocketFacade extends Endpoint {

    Session session;
    GameplayHandler gameplayHandler;

    // websocket commands are seperate from http requests !!!!!!!!

    public WebSocketFacade(String url, GameplayHandler gameplayHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url +"/connect");
            this.gameplayHandler = gameplayHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
            // set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch(serverMessage.getServerMessageType()) {
                        case LOAD_GAME -> loadGame();
                        case ERROR -> System.out.println("oopsie dasie: " + serverMessage.getServerMessageType().toString());
                        case NOTIFICATION -> printOut();
                    }
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void sendMessage(UserGameCommand command) throws IOException {
        try {
            String msg = new Gson().toJson(command);
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void loadGame() {

    }

    private void printOut() {
        // print out server message
    }

}
