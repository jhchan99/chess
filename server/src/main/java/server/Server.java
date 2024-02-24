package server;

import service.Service;
import dataAccess.DataAccessException;
import model.UserData;
import dataAccess.UserDataAccess;
import spark.*;

public class Server {

    private Service service;




    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.init();

        Spark.delete("/db", this::deleteDatabase);

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object deleteDatabase(Request req, Response res) throws DataAccessException {
        try {
            Service service = new Service();
            service.deleteDatabase();
            res.status(200);
            return "{}";
        } catch (DataAccessException e){
            return "Fix me";
        }
    }


}

