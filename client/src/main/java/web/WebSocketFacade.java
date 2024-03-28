package web;

import com.google.gson.Gson;

import javax.management.Notification;
import javax.websocket.*;
import java.net.URI;
import java.net.URISyntaxException;



public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    // websocket commands are seperate from http requests !!!!!!!!



    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws Exception{
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url +"/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            // set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (URISyntaxException ex) {
            throw new Exception("something websocket");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}
