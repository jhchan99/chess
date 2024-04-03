package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import web.GameplayHandler;
import web.ServerFacade;

import java.util.Arrays;
import java.util.Objects;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import web.WebSocketFacade;

public class GamePlay implements GameplayHandler {
    private static BoardOrientation orientation = BoardOrientation.WHITE;

    private final ChessBoard board = new ChessBoard();

    public static void setOrientation(BoardOrientation orientation) {
        GamePlay.orientation = orientation;
    }

    public GamePlay(WebSocketFacade ws) {
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

    // update game

    private String move(String[] params) throws ResponseException {
        try {
            if (params.length >= 2) {
                // change to ChessPosition (row, col)
                for (var i = 0; i < params.length; i++) {
                    var token = params[i].toLowerCase();
                    var coord = token.split("");
                    var x = coord[0].charAt(0) - 'a';
                    var y = Integer.parseInt(coord[1]) - 1;
                    ChessPosition position = new ChessPosition(x, y);
                    System.out.println("Position: " + position);
                }
                return "Moved.";
            }
        } catch (Exception e) {
            throw new ResponseException(400, "Invalid move.");
        }
        throw new ResponseException(400, "Expected: <from> <to>");
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
                - move <from> <to> (e.g. move a2 a4)
                - highlightMoves <from>
                - leave
                - resign
                """;
    }


    @Override
    public String updateGame(Integer gameID, ChessPosition from, ChessPosition to, String promotion) {
        return null;
    }

    @Override
    public void printMessage(String message) {
    }
}
