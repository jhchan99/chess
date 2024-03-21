package ui;

import exception.ResponseException;
import web.ServerFacade;

import java.util.Arrays;

public class PreLogin {
    private String username;
    private final ServerFacade server;
    private final String serverURL;
    private State state = State.SIGNEDOUT;

    public PreLogin(String serverURL) {
        server = new ServerFacade(serverURL);
        this.serverURL = serverURL;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signup" -> signUp(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String signUp(String[] params) throws ResponseException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            username = String.join("-", params);
            return String.format("You signed in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <yourname>");

    }

    private String login(String[] params) throws ResponseException {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            username = String.join("-", params);
            return String.format("You signed in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <yourname>");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <yourname>
                    - quit
                    """;
        }
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

}
