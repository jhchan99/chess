package web;


import chess.ChessGame;

public interface GameplayHandler {
    // update game
    void updateGame(ChessGame game, ChessGame.TeamColor color);

    // printMessage
   void printMessage(String message);
}
