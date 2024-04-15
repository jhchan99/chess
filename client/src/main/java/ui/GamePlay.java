package ui;

import chess.*;
import web.GameplayHandler;
import web.ServerFacade;

import java.io.IOException;
import java.util.Arrays;



import exception.ResponseException;
import web.WebSocketFacade;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.commands.MakeMove;

public class GamePlay implements GameplayHandler {

    private static BoardOrientation orientation = BoardOrientation.WHITE;

    private final ChessBoard board = new ChessBoard();

    private final WebSocketFacade webSocketFacade;

    public static void setOrientation(BoardOrientation orientation) {
        GamePlay.orientation = orientation;
    }

    public GamePlay(WebSocketFacade ws) {
        this.webSocketFacade = ws;
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
//                case "leave" -> leaveGame();
//                case "resign" -> resignGame();
//                case "highlightMoves" -> highlightMoves(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

//    private String highlightMoves(String[] params) throws ResponseException {
//        if (params.length >= 1) {
//            var from = params[0];
//            // convert from to ChessPosition position
//            var coord = from.toLowerCase().split("");
//            var x = coord[0].charAt(0) - 'a';
//        }
//        throw new ResponseException(400, "Expected: <from>");
//    }

    // update game

    private int getXCoordinate(String coord) {
        return coord.charAt(0) - 'a';
    }

    private int getYCoordinate(String coord) {
        return Integer.parseInt(coord.substring(1)) - 1;
    }

    private ChessPiece.PieceType getPromotionPiece(String piece) {
        return switch (piece) {
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> null;
        };
    }

    private String move(String[] params) throws ResponseException {
        try {
            ChessPiece.PieceType promotion = null;
            if (params.length == 0) {
                throw new ResponseException(400, "Expected: <from> <to>");
            }
            if (params.length >= 2) {
                // change to ChessPosition (row, col)
                if (params[2] != null) {
                     promotion = getPromotionPiece(params[2]);
                }
                int[] from = new int[2];
                int[] to = new int[2];
                for (int i = 0; i < 2; i++) {
                    var x = getXCoordinate(params[i]);
                    var y = getYCoordinate(params[i]);
                    if (i == 0) {
                        from[0] = x;
                        from[1] = y;
                    } else {
                        to[0] = x;
                        to[1] = y;
                    }
                }
                var move = new ChessMove(new ChessPosition(from[0], from[1]), new ChessPosition(to[0], to[1]), promotion);
                webSocketFacade.sendMessage(new MakeMove(ServerFacade.getAuthToken(), PostLogin.getGameID(), move));
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

    // Root Client sends LEAVE
    //
    // If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.
    // Server sends a Notification message to all other
    // clients in that game informing them that the root client left. This applies to both players and observers.

//    private String leaveGame() {
//        try {
//            webSocketFacade.sendMessage(new UserGameCommand(UserGameCommand.CommandType.LEAVE, ServerFacade.getAuthToken()));
//            Repl.setState(State.SIGNEDIN);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "You left the game.";
//    }


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
    public void updateGame(ChessGame game) {
        redrawBoard(board);
    }

    @Override
    public void printMessage(String message) {
    }
}
