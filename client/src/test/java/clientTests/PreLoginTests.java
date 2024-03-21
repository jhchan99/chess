package clientTests;

import exception.ResponseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;
import ui.Repl;
import web.ServerFacade;

public class PreLoginTests {
    private static Server server;



    @BeforeAll
    public static void init() throws ResponseException {
        var serverUrl = "http://localhost:8080";
        new Repl(serverUrl).run();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void signIn() throws ResponseException {
        // test t
    }

}
