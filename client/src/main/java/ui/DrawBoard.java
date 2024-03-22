package ui;

import static ui.EscapeSequences.*;

public class DrawBoard {

    // draw the board from the perspective of the white player
    public void drawWhite() {
        System.out.println(ERASE_SCREEN);
        System.out.print("  ");

        // Print the top row letters
        for (char colLabel = 'a'; colLabel <= 'h'; colLabel++) {
            System.out.print(" " + colLabel + " "); // Print column labels with padding
        }
        System.out.println();

        for (int row = 8; row >= 1; row--) {
            System.out.print(row + " ");

            for (int col = 0; col < 8; col++) {
                String tileColor = (row + col) % 2 == 0 ? SET_BG_COLOR_BLUE : SET_BG_COLOR_MAGENTA;
                System.out.print(tileColor);

                String piece = getPiece(row, col);

                System.out.print(piece);

                System.out.print(RESET_BG_COLOR);
            }
            System.out.println(" " + row);
        }

        System.out.print("  ");
        for (char colLabel = 'a'; colLabel <= 'h'; colLabel++) {
            System.out.print(" " + colLabel + " ");
        }
        System.out.println();
    }

    // draw the board from the perspective of the black player
    public void drawBlack() {
        System.out.println(ERASE_SCREEN);
        System.out.print("  ");

        // Print the top row letters in reverse order for black player
        for (char colLabel = 'h'; colLabel >= 'a'; colLabel--) {
            System.out.print(" " + colLabel + " ");
        }
        System.out.println();

        for (int row = 1; row <= 8; row++) {
            System.out.print(row + " ");

            for (int col = 7; col >= 0; col--) {
                String tileColor = (row + col) % 2 == 0 ? SET_BG_COLOR_BLUE : SET_BG_COLOR_MAGENTA;
                System.out.print(tileColor);

                String piece = getPiece(row, col);

                System.out.print(piece);

                System.out.print(RESET_BG_COLOR);
            }
            System.out.println(" " + row);
        }

        System.out.print("  ");
        for (char colLabel = 'h'; colLabel >= 'a'; colLabel--) {
            System.out.print(" " + colLabel + " ");
        }
        System.out.println();
    }

    private String getPiece(int row, int col) {
        // Define the initial positions of the pieces for both players
        String[][] pieces = {
                {"♜", "♞", "♝", "♛", "♚", "♝", "♞", "♜"},
                {"♟", "♟", "♟", "♟", "♟", "♟", "♟", "♟"},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {" ", " ", " ", " ", " ", " ", " ", " "},
                {"♙", "♙", "♙", "♙", "♙", "♙", "♙", "♙"},
                {"♖", "♘", "♗", "♕", "♔", "♗", "♘", "♖"}
        };

        // Adjust for zero-based indexing
        int adjustedRow = 8 - row;

        return " " + pieces[adjustedRow][col] + " ";
    }
}

