package webSocketMessages.serverMessages;

import chess.ChessGame;
import model.GameData;
import webSocketMessages.userCommands.UserGameCommand;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

//    public ServerMessage(ServerMessageType type, String message) {
//        this.serverMessageType = type;
//        this.message = message;
//    }
//
//    public ServerMessage(ServerMessageType type, ChessGame game, ChessGame.TeamColor color) {
//        this.serverMessageType = type;
//        this.game = game;
//        this.color = color;
//    }

//    public String getMessage() {
//        return message;
//    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

//    private String message;
//
//    private ChessGame game;
//
//    private ChessGame.TeamColor color;

//    public ChessGame.TeamColor getColor() {
//        return color;
//    }
//
//    public ChessGame getGame() {
//        return game;
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
