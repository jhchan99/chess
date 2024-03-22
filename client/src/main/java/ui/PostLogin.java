package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import server.Server;
import web.ServerFacade;

import java.util.Arrays;
import java.util.Objects;

public class PostLogin {

    private String username;
    private final ServerFacade serverFacade;
    private final State state = State.SIGNEDIN;
    private final DrawBoard board = new DrawBoard();

    public PostLogin(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
    }


    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);;
            return switch (cmd) {
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "join" -> joinGame(params);
                case "list" -> listGames();
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    private String quit() throws ResponseException {
        serverFacade.logoutUser();
        return "quit";
    }

    private String listGames() throws ResponseException {
        assertSignedIn();
        var games = serverFacade.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        result.append("Games:\n");
        for (var game : games) {
            var name = game.gameName();
            var id = game.gameID();
            var white = game.whiteUsername();
            var black = game.blackUsername();
            result.append(String.format("gameID: %s, whiteUsername: %s, blackUsername: %s, gameName: %s\n", id, white, black, name));
        }
        return result.toString();
    }

    private String joinGame(String[] params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 2) {
            if (Objects.equals(params[1], "white")) {
                serverFacade.joinGame(Integer.parseInt(params[0]), ChessGame.TeamColor.WHITE);
                board.drawWhite();
            } else if (Objects.equals(params[1], "black")) {
                serverFacade.joinGame(Integer.parseInt(params[0]), ChessGame.TeamColor.BLACK);
                board.drawBlack();
            } else {
                throw new ResponseException(400, "Expected: <gameID> <white|black>");
            }
        } else if(params.length == 1) {
            serverFacade.joinGame(Integer.parseInt(params[0]), null);
            board.drawWhite();
            board.drawBlack();
            return String.format("You have joined game %s as an observer", params[0]);
        } else {throw new ResponseException(400, "Expected: <gameID>");}
        return String.format("You have joined game %s as %s.", params[0], params[1]);
    }

    private String createGame(String[] params) throws ResponseException {
        assertSignedIn();
        if(params.length >= 1) {
            return String.valueOf(serverFacade.createGame(params[0]));
        }
        throw new ResponseException(400, "Expected: <gameName>");
    }

    private String logout() throws ResponseException {
        if(serverFacade.logoutUser()){
            Repl.setState(State.SIGNEDOUT);
        }
        return "You have been signed out.";
    }

    public String help() {
        return"""
                - logout
                - create <gameName>
                - join <gameID> <white|black>
                - list
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state != State.SIGNEDIN) {
            throw new ResponseException(401, "You must be signed in to do that.");
        }
    }
}
