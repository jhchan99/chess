package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Connection> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        var connection = new Connection(gameID, session);
        connections.put(gameID, connection);
    }

    public void remove(Integer gameID) {
        connections.remove(gameID);
    }

    public void broadcast(Integer excludeGameID, Notification notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for(var c : connections.values()) {
            if(c.session.isOpen()){
                if(!c.gameID.equals(excludeGameID)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }
        // clean up left over connections
        for(var c : removeList) {
            connections.remove(c.gameID);
        }
    }

}
