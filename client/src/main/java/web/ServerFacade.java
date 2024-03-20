package web;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;


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

    public String registerUser(String name, String pass, String email) throws ResponseException {
        var path = "/user";
        UserData user = new UserData(name, pass, email);
        AuthData newAuth = this.makeRequest("POST", path, user, AuthData.class);
        authToken = newAuth.authToken();
        return name;
    }

    public AuthData loginUser(String pass, String user) throws ResponseException {
        var path = String.format("/user/%s", authToken);
        return this.makeRequest("SESSION", path, null, null);
    }

    public void logoutUser(String auth) throws ResponseException {
        var path = String.format("/user/%s",auth );
        this.makeRequest("SESSION", path, null, null);
    }

    public void createGame(String auth, int gameID) {

    }

    public void joinGame(String auth, int gameID) {

    }

    public GameData[] listGames() throws ResponseException {
        var path = "/game";
        record listGameResponse(GameData[] game) {
        }
        var response = this.makeRequest("GET", path, null, listGameResponse.class);
        return response.game();
    }







    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
