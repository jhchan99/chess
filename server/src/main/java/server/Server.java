package server;

// this is also my handler

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.requests.JoinGameRequests;
import service.Service;
import dataAccess.DataAccessException;
import model.UserData;
import spark.*;

import java.sql.SQLException;
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
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.get("/game", this::listGames);



        // Register your endpoints and handle exceptions here.

        Spark.awaitInitialization();

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerUser(Request req, Response res) {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            var newAuth = service.registerUser(user);
            res.status(200);
            return new Gson().toJson(Map.of("username", user.username(), "authToken", newAuth.authToken()));
        }catch (DataAccessException | SQLException e) {
            if (e.getMessage().equals("User already exists")) {
                res.status(403);
                return new Gson().toJson(Map.of("message", "Error: already taken"));
            }
            if (e.getMessage().equals("Invalid user data")) {
                res.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
        }
        return null;
    }

    private Object loginUser(Request req, Response res){
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            var auth = service.loginUser(user);
            res.status(200);
            return new Gson().toJson(Map.of("username", user.username(), "authToken", auth.authToken()));
        } catch (DataAccessException | SQLException e) {
            if(e.getMessage().equals("User not found")){
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
            if(e.getMessage().equals("Password incorrect")){
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
        }
        return "Fix me";
    }

    private Object logoutUser(Request req, Response res){
        try {
            // Get the auth token
            String authToken = req.headers("Authorization");
            // Delete the auth token
            service.deleteAuth(authToken);
            return "{}";
        } catch (DataAccessException | SQLException e){
            if(e.getMessage().equals("Auth token not found")){
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
        }
        return null;
    }

    private Object createGame(Request req, Response res){
        try {
            String authToken = req.headers("Authorization");
            GameData game = new Gson().fromJson(req.body(), GameData.class);
            game = service.createGame(authToken, game);
            res.status(200);
            return new Gson().toJson(Map.of("gameID", game.gameID()));
        } catch (DataAccessException | SQLException e){
            if(e.getMessage().equals("Auth token not found")){
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
            if(e.getMessage().equals("User not found")){
                res.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
            if(e.getMessage().equals("Game name is null")){
                res.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
        }
        return "Fix createGame";
    }


    private Object joinGame(Request req, Response res){
        // get the auth token
        String authToken = req.headers("Authorization");
        // get the user
        JoinGameRequests player = new Gson().fromJson(req.body(), JoinGameRequests.class);
        try {
            // join the game
            service.updateGame(authToken, player);
            res.status(200);
            return "{}";
        } catch (DataAccessException | SQLException e){
            if(e.getMessage().equals("White player is taken")){
                res.status(403);
                return new Gson().toJson(Map.of("message", "Error: already taken"));
            }
            if(e.getMessage().equals("Black player is taken")){
                res.status(403);
                return new Gson().toJson(Map.of("message", "Error: already taken"));
            }
            if(e.getMessage().equals("Auth token not found")){
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
            if(e.getMessage().equals("Game not found")){
                res.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
            if(e.getMessage().equals("User not found")){
                res.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
            if(e.getMessage().equals("Invalid player color")){
                res.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
            if(e.getMessage().equals("Game is full")) {
                res.status(403);
                return new Gson().toJson(Map.of("message", "Error: already taken"));
            }

        }
        return "Fix me joinGame";
    }

    private Object listGames(Request req, Response res){
        try {
            // get auth token
            String authToken = req.headers("Authorization");
            // get the list of games
            var games = service.listGames(authToken);
            res.status(200);
            return new Gson().toJson(Map.of("games", games));
        } catch (DataAccessException | SQLException e){
            if(e.getMessage().equals("Auth token not found")){
                res.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
        }
        return "Fix me listGames";
    }

    private Object deleteDatabase(Request req, Response res){
        try {
            service.deleteDatabase();
            res.status(200);
            return "{}";
        } catch (DataAccessException | SQLException e){
            return "Fix deleteDatabase";
        }
    }


}

