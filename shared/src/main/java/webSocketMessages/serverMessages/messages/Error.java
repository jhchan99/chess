package webSocketMessages.serverMessages.messages;

import webSocketMessages.serverMessages.ServerMessage;

public class Error extends ServerMessage {

    private final String errorMessage;

    public Error(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    public String getError() {
        return errorMessage;
    }

}
