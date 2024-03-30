package ui;

import static ui.EscapeSequences.*;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class DrawBoard {

    // Draw the board from the perspective of the white player with alternating colors
    public static void drawWhite(ChessBoard board) {
        System.out.println(ERASE_SCREEN);
        System.out.print("  ");

        // Print the top row letters
        for (char colLabel = 'a'; colLabel <= 'h'; colLabel++) {
            System.out.print(" " + colLabel + " ");
        }
        System.out.println();

        for (int row = 8; row >= 1; row--) {
            System.out.print(row + " ");
            for (int col = 1; col <= 8; col++) {
                // Determine color based on row and column for alternating colors
                String color = ((row + col) % 2 == 0) ? SET_BG_COLOR_BLUE : SET_BG_COLOR_MAGENTA;
                System.out.print(color + " " + getPieceSymbol(board.getPiece(new ChessPosition(row, col))) + " " + RESET_BG_COLOR);
            }
            System.out.println(" " + row);
        }

        System.out.print("  ");
        for (char colLabel = 'a'; colLabel <= 'h'; colLabel++) {
            System.out.print(" " + colLabel + " ");
        }
        System.out.println();
    }

    // Draw the board from the perspective of the black player with alternating colors
    public static void drawBlack(ChessBoard board) {
        System.out.println(ERASE_SCREEN);
        System.out.print("  ");

        // Print the top row letters in reverse
        for (char colLabel = 'h'; colLabel >= 'a'; colLabel--) {
            System.out.print(" " + colLabel + " ");
        }
        System.out.println();

        for (int row = 1; row <= 8; row++) {
            System.out.print(row + " ");
            for (int col = 8; col >= 1; col--) {
                // Determine color based on row and column for alternating colors
                String color = ((row + col) % 2 == 0) ? SET_BG_COLOR_BLUE : SET_BG_COLOR_MAGENTA;
                System.out.print(color + " " + getPieceSymbol(board.getPiece(new ChessPosition(row, 9 - col))) + " " + RESET_BG_COLOR);
            }
            System.out.println(" " + row);
        }

        System.out.print("  ");
        for (char colLabel = 'h'; colLabel >= 'a'; colLabel--) {
            System.out.print(" " + colLabel + " ");
        }
        System.out.println();
    }

    private static String getPieceSymbol(ChessPiece piece) {
        if (piece == null) {
            return " ";
        }
        // Switch case for each piece type with corrected symbols for team colors
        return switch (piece.getPieceType()) {
            case KING -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? "♚" : "♔";
            case QUEEN -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? "♛" : "♕";
            case ROOK -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? "♜" : "♖";
            case BISHOP -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? "♝" : "♗";
            case KNIGHT -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? "♞" : "♘";
            case PAWN -> piece.getTeamColor() == ChessGame.TeamColor.BLACK ? "♟" : "♙";
        };
    }
}
