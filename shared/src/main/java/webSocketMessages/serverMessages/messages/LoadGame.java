package webSocketMessages.serverMessages.messages;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Objects;

public class LoadGame extends ServerMessage {

    private final ChessGame game;
    private final ChessGame.TeamColor color;

    public LoadGame(ServerMessageType type, ChessGame game, ChessGame.TeamColor color) {
        super(type);
        this.game = game;
        this.color = color;
    }

    public ChessGame getGame() {
        return game;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoadGame loadGame = (LoadGame) o;
        return Objects.equals(game, loadGame.game) && color == loadGame.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), game, color);
    }
}
