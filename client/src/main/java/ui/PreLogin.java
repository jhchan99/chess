package ui;

import exception.ResponseException;
import model.GameData;
import web.ServerFacade;

import java.util.Arrays;

public class PreLogin {
    private String username;
    private final ServerFacade server;
    private final String serverURL;
    private State state = State.SIGNEDOUT;

    public PreLogin(String serverURL) {
        server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);;
            return switch (cmd) {
                case "signup" -> signUp(params);
                case "login" -> login(params);
                case "help" -> help();
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }


    private String logout() throws ResponseException {
        state = State.SIGNEDOUT;
        server.logoutUser();
        return "You have been signed out.";
    }

    private String signUp(String[] params) throws ResponseException {
        if (params.length >= 1) {
            var response = server.registerUser(params[0], params[1], params[2]);
            if(response != null) {
                state = State.SIGNEDIN;
            }
            return String.format(response);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");

    }

    private String login(String[] params) throws ResponseException {
        if (params.length >= 2) {
            state = State.SIGNEDIN;
            server.loginUser(params[0], params[1]);
            return String.format("You signed in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username>");
    }

    public String help() {
        return"""
                - login <username> <password>
                - signup <username> <password> <email>
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

}
