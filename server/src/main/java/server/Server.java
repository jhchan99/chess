package server;

import com.google.gson.Gson;
import model.AuthData;
import service.Service;
import dataAccess.DataAccessException;
import model.UserData;
import spark.*;

import javax.xml.crypto.Data;
import java.util.Map;

public class Server {

    private final Service service = new Service();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.init();

        Spark.delete("/db", this::deleteDatabase);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerUser(Request req, Response res) throws DataAccessException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            service.createUser(user);
            var auth = service.createAuth();
            res.status(200);
            return new Gson().toJson(Map.of("authToken", auth));
        }catch (DataAccessException e) {
            return "that's not good addUser might be broken oops";
        }
    }

    private Object loginUser(Request req, Response res) throws DataAccessException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            user = service.getUser(user);
            var auth = service.createAuth();
            res.status(200);
            return new Gson().toJson(Map.of("authToken", auth, "username", user.username()));
        } catch (DataAccessException e) {
            if(e.getMessage().equals("User not found")){
                res.status(401);
                return "User not found";
            }
        }
        return "Fix me";
    }

    private Object logoutUser(Request req, Response res) throws DataAccessException {
        try {
            service.deleteAuth(new Gson().fromJson(req.body(), AuthData.class));
            return "{}";
        } catch (DataAccessException e){
            return "Fix me";
        }
    }

    private Object deleteDatabase(Request req, Response res) throws DataAccessException {
        try {
            service.deleteDatabase();
            res.status(200);
            return "{}";
        } catch (DataAccessException e){
            return "Fix me";
        }
    }


}

