package web;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.DrawBoard;
import ui.GamePlay;
import ui.PrintToInterface;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.messages.LoadGame;
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
                        case LOAD_GAME -> loadGame(message);
                        case ERROR -> PrintToInterface.printError(message);
                        case NOTIFICATION -> PrintToInterface.printMessage(message);
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

    // send game to game play
    private void loadGame(String msg) {
        LoadGame loadGame = new Gson().fromJson(msg, LoadGame.class);
        ChessGame game = loadGame.getGame();

        gameplayHandler.updateGame(game);

     }


//
//    private void printOut() {
//        // print out server message
//
//    }

}
