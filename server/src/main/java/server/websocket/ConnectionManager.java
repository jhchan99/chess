package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import javax.management.Notification;
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

    public void broadcast(Integer includeGameID, String notification) throws IOException {
        for (var c : connections.values()) {
            for (var connection : c.values()) {
                if (connection.session.isOpen()) {
                    if (connection.gameID.equals(includeGameID)) {
                        connection.send(notification);
                    }
                }
            }
        }
    }

}
