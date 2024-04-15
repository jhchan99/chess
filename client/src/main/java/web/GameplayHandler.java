package web;


import chess.ChessGame;

public interface GameplayHandler {
    // update game
    void updateGame(ChessGame game);

    // printMessage
   void printMessage(String message);
}
