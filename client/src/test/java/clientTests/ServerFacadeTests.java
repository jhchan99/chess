package clientTests;

import chess.ChessGame;
import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import web.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:" + port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() throws ResponseException {
        serverFacade.deleteData();
        server.stop();
    }

    @Test
    public void testDeleteData() throws ResponseException {
        // test that no exception is thrown
        serverFacade.deleteData();
    }
    @Test
    public void testTryLoginAfterDeleteData() throws ResponseException {
        // test that exception is thrown
        serverFacade.deleteData();
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.loginUser("password", "user"));
    }

    @Test
    public void testRegisterUser() throws ResponseException {
        // test that auth token is returned
        serverFacade.deleteData();
        var token = serverFacade.registerUser("user", "password", "email");
        Assertions.assertNotNull(token);
    }

    @Test
    public void testLoginUser() throws ResponseException {
        // test that auth token is returned
        serverFacade.deleteData();
        serverFacade.registerUser("user", "password", "email");
        var token = serverFacade.loginUser("user", "password");
        Assertions.assertNotNull(token);
    }

    @Test
    public void testLoginUserWrongPassword() throws ResponseException {
        // test that exception is thrown
        serverFacade.registerUser("user", "password", "email");
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.loginUser("wrongpassword", "user"));
    }

    @Test
    public void testLogoutUser() throws ResponseException {
        // test that no exception is thrown
        serverFacade.deleteData();
        serverFacade.registerUser("user", "password", "email");
        serverFacade.loginUser("user", "password");
        serverFacade.logoutUser();
    }

    @Test
    public void testCreateGame() throws ResponseException {
        // test that game id is returned
        serverFacade.deleteData();
        serverFacade.registerUser("user", "password", "email");
        var gameId = serverFacade.createGame("game");
        Assertions.assertTrue(gameId > 0);
    }

    @Test
    public void testCreateGameAfterLogin() throws ResponseException {
        // test that game id is returned
        serverFacade.deleteData();
        serverFacade.registerUser("user", "password", "email");
        serverFacade.logoutUser();
        serverFacade.loginUser("user", "password");
        var gameId = serverFacade.createGame("game");
        Assertions.assertTrue(gameId > 0);
    }

    @Test
    public void testJoinGame() throws ResponseException {
        // test that no exception is thrown
        serverFacade.deleteData();
        serverFacade.registerUser("user", "password", "email");
        var gameId = serverFacade.createGame("game");
        serverFacade.joinGame(gameId, ChessGame.TeamColor.WHITE);
    }

    @Test
    public void testListGames() throws ResponseException {
        // test that no exception is thrown
        serverFacade.deleteData();
        serverFacade.registerUser("user", "password", "email");
        serverFacade.createGame("game");
        serverFacade.listGames();
    }

    @Test
    public void testListGamesReturnsGames() throws ResponseException {
        // test that no exception is thrown
        serverFacade.deleteData();
        serverFacade.registerUser("user", "password", "email");
        serverFacade.createGame("game");
        serverFacade.createGame("game2");
        serverFacade.createGame("game3");
        var games = serverFacade.listGames();
        Assertions.assertTrue(games.length > 0);
    }

    @Test
    public void testJoinGameAsObserver() throws ResponseException {
        // test that no exception is thrown
        serverFacade.deleteData();
        serverFacade.registerUser("user", "password", "email");
        var gameId = serverFacade.createGame("game");
        serverFacade.joinGame(gameId, null);
    }

    @Test
    // test that exception is thrown
    public void testJoinGameWithTakenColor() throws ResponseException {
        serverFacade.deleteData();
        serverFacade.registerUser("user", "password", "email");
        var gameId = serverFacade.createGame("game");
        serverFacade.joinGame(gameId, ChessGame.TeamColor.WHITE);
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.joinGame(gameId, ChessGame.TeamColor.WHITE));
    }



}
