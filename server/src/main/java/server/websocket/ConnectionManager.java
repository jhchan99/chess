package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

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
        if(gameConnections != null) {
            connections.remove(auth);
            if(gameConnections.isEmpty()) {
                connections.remove(gameID);
            }
        }

    }

    public void broadcast(Integer excludeGameID, String notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for(var c : connections.values()) {
            for (var connection : c.values()) {
                if (connection.session.isOpen()) {
                    if (!connection.gameID.equals(excludeGameID)) {
                        connection.send(notification);
                    }
                } else {
                    removeList.add(connection);
                }
            }
        }
        // clean up left over connections
        for(var c : removeList) {
            connections.remove(c.gameID);
        }
    }

}
