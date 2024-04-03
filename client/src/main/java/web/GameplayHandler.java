package web;


import chess.ChessPosition;

public interface GameplayHandler {
    // update game
    String updateGame(Integer gameID, ChessPosition from, ChessPosition to, String promotion);

    // printMessage
   void printMessage(String message);
}
