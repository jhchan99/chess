package webSocketMessages.userCommands.commands;

import chess.ChessMove;
import webSocketMessages.userCommands.UserGameCommand;

public class MakeMove extends UserGameCommand {

    private final Integer gameID;
    private final ChessMove move;

    public MakeMove(String authToken, Integer gameID, ChessMove move) {
        super(authToken, CommandType.MAKE_MOVE);
        this.gameID = gameID;
        this.move = move;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }


}
