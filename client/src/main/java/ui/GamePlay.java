package ui;

import chess.ChessBoard;
import chess.ChessPosition;
import web.ServerFacade;

import java.util.Arrays;
import java.util.Objects;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;

public class GamePlay {
    private final ServerFacade serverFacade;
    private static BoardOrientation orientation = BoardOrientation.WHITE;
    private final ChessBoard board = new ChessBoard();

    public static void setOrientation(BoardOrientation orientation) {
        GamePlay.orientation = orientation;
    }

    public GamePlay(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
        this.board.resetBoard();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redrawBoard(board);
                case "move" -> move(params);
                case "leave" -> leaveGame();
//                case "resign" -> resignGame();
//                case "highlightMoves" -> highlightMoves(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }


    private String highlightMoves(String[] params) throws ResponseException {
        if (params.length >= 1) {
            var from = params[0];
            // convert from to ChessPosition position
            var coord = from.toLowerCase().split("");
            var x = coord[0].charAt(0) - 'a';
        }
        throw new ResponseException(400, "Expected: <from>");
    }

    private String move(String[] params) throws ResponseException {
        var to = params[0];
        var coordinates = inputToPosition(to);
        System.out.println("Coordinates: " + coordinates);
        throw new ResponseException(400, "Expected: <to>");
    }

    private ChessPosition inputToPosition(String input) {
        var b = input.toLowerCase().split("");
        // change first letter to number
        var x = b[0].charAt(0) - 'a';
        var y = Integer.parseInt(b[1]) - 1;
        return new ChessPosition(x, y);
    }

    private String leaveGame() {
        Repl.setState(State.SIGNEDIN);
        return "You left the game.";
    }


    public String redrawBoard(ChessBoard board) {
        // if player is black, redraw black board
        if(orientation == BoardOrientation.BLACK) {
            DrawBoard.drawBlack(board);
        } else if(orientation == BoardOrientation.WHITE) {
            // else redraw white board
            DrawBoard.drawWhite(board);
        }
        return "Board redrawn.";
    }

    public String help() {
        return"""
                - redraw
                - move <to>
                - highlightMoves <from>
                - leave
                - resign
                """;
    }


}
