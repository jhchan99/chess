package web;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.JoinGameRequests;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    private String authToken;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void deleteData() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public String registerUser(String name, String pass, String email) throws ResponseException {
        authToken = null;
        var path = "/user";
        var user = new UserData(name, pass, email);
        record registerUserResponse(String authToken) {
        }
        var response = this.makeRequest("POST", path, user, registerUserResponse.class);
        this.authToken = response.authToken();
        return user.username();
    }

    public String loginUser(String user, String pass) throws ResponseException {
        authToken = null;
        var path = "/session";
        var userAuth = new UserData(user, pass, null);
        record loginUserResponse(String authToken) {
        }
        var response = this.makeRequest("POST", path, userAuth, loginUserResponse.class);
        this.authToken = response.authToken();
        return authToken;
    }

    public Boolean logoutUser() throws ResponseException {
        var path =  String.format("/session?auth=%s", this.authToken);
        System.out.println(this.authToken);
        this.makeRequest("DELETE", path, null, null);
        authToken = null;
        return true;
    }

    public int createGame(String gameName) throws ResponseException {
        var path = String.format("/game?auth=%s", this.authToken);
        var game = new GameData(3, null, null, gameName, null);
        record createGameResponse(int gameID) {
        }
        System.out.println(authToken);
        var response = this.makeRequest("POST", path, game, createGameResponse.class);
        // issue with the response id it's not getting a game
        return (response.gameID());
    }

    public void joinGame(int gameID, ChessGame.TeamColor teamColor) throws ResponseException {
        var path = String.format("/game?auth=%s", authToken);
        var game = new JoinGameRequests(teamColor, gameID);
        record joinGameResponse() {
        }
        this.makeRequest("PUT", path, game, joinGameResponse.class);
    }

    public GameData[] listGames() throws ResponseException {
        var path = String.format("/game?auth=%s", authToken);
        record listGameResponse(GameData[] game) {
        }
        var response = this.makeRequest("GET", path, null, listGameResponse.class);
        return response.game();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            // if the http request has an auth token, add auth to the header
            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }
            http.setRequestMethod(method);
            http.setDoOutput(!method.equals("GET"));

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }


}
