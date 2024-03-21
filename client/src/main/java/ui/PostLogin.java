package ui;

import exception.ResponseException;
import model.GameData;
import web.ServerFacade;

import java.util.Arrays;

public class PostLogin {
//
//    private String username;
//    private final ServerFacade server;
//    private final String serverURL;
//    private State state = State.SIGNEDIN;
//
//
//    public String eval(String input) {
//        try {
//            var tokens = input.toLowerCase().split(" ");
//            var cmd = (tokens.length > 0) ? tokens[0] : "help";
//            var params = Arrays.copyOfRange(tokens, 1, tokens.length);;
//            return switch (cmd) {
//                case "logout" -> logout();
//                case "create" -> createGame(params);
//                case "join" -> joinGame(params);
//                case "list" -> listGames();
//                case "quit" -> "quit";
//                default -> help();
//            };
//        } catch (ResponseException ex) {
//            return ex.getMessage();
//        }
//    }
//
//
//
//    private GameData[] listGames() throws ResponseException {
//        assertSignedIn();
//        return server.listGames();
//    }
//
//    private void joinGame(params) throws ResponseException {
//        assertSignedIn();
//        if (params.length >= 1) {
//            server.joinGame(params[0]);
//        }
//        throw new ResponseException(400, "Expected: <gameID>");
//    }
//
//    private int createGame(String[] params) throws ResponseException {
//        assertSignedIn();
//        if (params.length >= 1) {
//            return server.createGame(params[0]);
//        }
//        throw new ResponseException(400, "Expected: <gameName>");
//    }
}
