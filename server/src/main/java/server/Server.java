package server;

import com.google.gson.Gson;


import dataAccess.UserDataAccess;
import service.userservice;
import spark.*;

public class Server {

    private final userservice service;

    public Server(UserDataAccess userData){
        service = new userservice(userData);
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.init();

        Spark.delete("/db", this::deleteAllUsers);

        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object deleteAllUsers(Request req, Response res) {
        service.deleteAllUsers();
    }


}

