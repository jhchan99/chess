package ui;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import server.Server;
import web.NotificationHandler;
import web.ServerFacade;
import web.WebSocketFacade;

import java.util.Arrays;
import java.util.Objects;

public class PostLogin {
    private final ServerFacade serverFacade;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;



    public PostLogin(ServerFacade serverFacade, NotificationHandler notificationHandler) {
        this.serverFacade = serverFacade;
        this.notificationHandler = notificationHandler;
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
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }


    private String quit() throws ResponseException {
        serverFacade.logoutUser();
        return "quit";
    }


    private String listGames() throws ResponseException {
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


    private String joinGame(String[] params) throws Exception {
        var gameid = Integer.parseInt(params[0]);
        if (params.length >= 2) {
            if (Objects.equals(params[1], "white")) {
                serverFacade.joinGame(gameid, ChessGame.TeamColor.WHITE);
                ws.joinPlayer(gameid, ChessGame.TeamColor.WHITE);
                GamePlay.setOrientation(BoardOrientation.WHITE);
            } else if (Objects.equals(params[1], "black")) {
                serverFacade.joinGame(gameid, ChessGame.TeamColor.BLACK);
                ws.joinPlayer(gameid, ChessGame.TeamColor.BLACK);
                GamePlay.setOrientation(BoardOrientation.BLACK);
            } else {
                throw new ResponseException(400, "Expected: <gameID> <white|black>");
            }
        } else if(params.length == 1) {
            serverFacade.joinGame(Integer.parseInt(params[0]), null);
            GamePlay.setOrientation(BoardOrientation.WHITE);
            return String.format("You have joined game %s as an observer", params[0]);
        } else {throw new ResponseException(400, "Expected: <gameID>");}
        Repl.setState(State.INGAME);
        return String.format("You have joined game %s as %s.", params[0], params[1]);
    }


    private String createGame(String[] params) throws ResponseException {
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
                - (as observer) join <gameID>
                - list
                - quit
                """;
    }


}
