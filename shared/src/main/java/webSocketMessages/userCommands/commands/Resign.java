package webSocketMessages.userCommands.commands;

import webSocketMessages.userCommands.UserGameCommand;

public class Resign extends UserGameCommand {

    private final Integer gameID;

    public Resign(String authToken, Integer gameID) {
        super(authToken, CommandType.RESIGN);
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
