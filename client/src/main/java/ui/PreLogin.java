package ui;

import exception.ResponseException;
import model.GameData;
import web.ServerFacade;

import java.util.Arrays;

public class PreLogin {
    private String username;
    private final ServerFacade serverFacade;

    public PreLogin(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);;
            return switch (cmd) {
                case "signup" -> signUp(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }


    private String signUp(String[] params) throws ResponseException {
        if (params.length >= 1) {
            var response = serverFacade.registerUser(params[0], params[1], params[2]);
            if(response != null) {
                Repl.setState(State.SIGNEDIN);
            }
            return String.format("You're signed in as %s.", response);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");

    }

    private String login(String[] params) throws ResponseException {
        if (params.length >= 2) {
            var response  = serverFacade.loginUser(params[0], params[1]);
            if(response != null) {
                Repl.setState(State.SIGNEDIN);
            }
            return String.format("You signed in as %s.", response);
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



}
