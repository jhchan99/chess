package webSocketMessages.userCommands.commands;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.Objects;

public class JoinPlayer extends UserGameCommand {

    private final Integer gameID;
    private final ChessGame.TeamColor playerColor;

    public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(authToken, CommandType.JOIN_PLAYER);
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getColor() {
        return playerColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JoinPlayer that = (JoinPlayer) o;
        return Objects.equals(gameID, that.gameID) && playerColor == that.playerColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID, playerColor);
    }
}
