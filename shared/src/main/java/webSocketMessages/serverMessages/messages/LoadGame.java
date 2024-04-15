package webSocketMessages.serverMessages.messages;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Objects;

public class LoadGame extends ServerMessage {

    private final ChessGame game;

    public LoadGame(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoadGame loadGame = (LoadGame) o;
        return Objects.equals(game, loadGame.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), game);
    }
}
