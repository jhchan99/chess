package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.messages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session, String auth) {
        var connection = new Connection(gameID, session);
        connections.computeIfAbsent(gameID, k -> new ConcurrentHashMap<>()).put(auth, connection);
    }

    public void remove(Integer gameID, String auth) {
        ConcurrentHashMap<String, Connection> gameConnections = connections.get(gameID);
        if (gameConnections != null) {
            gameConnections.remove(auth);
        }
    }

    public Integer getGameID(String auth) {
        for (Integer gameID : connections.keySet()) {
            if (connections.get(gameID).containsKey(auth)) {
                return gameID;
            }
        }
        return null;
    }

    public void broadcastJoinGame(Integer gameID, String notification, String excludeUserByAuth) throws IOException {
        // get the connection connected to the gameID
        var userConnection = connections.get(gameID);
        var msg = new Gson().toJson(new Notification(ServerMessage.ServerMessageType.NOTIFICATION, notification)) ;
        for(String connection : userConnection.keySet()) {
            if (!connection.equals(excludeUserByAuth)) {
                // send notification to users that do not match given auth
                userConnection.get(connection).send(msg);
            }
        };
    }



}
