package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.JoinGameRequests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Service;

import java.sql.SQLException;


class ServiceTest {

    private Service service;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        // Create a new instance of the Service class and delete the database before each test
        service = new Service();
        service.deleteDatabase();
    }

    @Test
    void listGames() throws DataAccessException, SQLException {
        // Create a test user
        AuthData testUser = service.registerUser(new UserData("user", "password", "email"));
        // Test for empty list of games
        Assertions.assertEquals(0, service.listGames(testUser.authToken()).size());
        // Create a test game
        service.createGame(testUser.authToken(), new GameData(0, null, null, "gameName", null));
        // Test for list of games with one game
        Assertions.assertEquals(1, service.listGames(testUser.authToken()).size());
    }

    @Test
    void testListGames() throws DataAccessException, SQLException {
        // Test that listGames will fail with an exception when using an invalid auth token
        Assertions.assertThrows(DataAccessException.class, () -> service.listGames("badAuthToken"));
    }

    @Test
    void loginUser() throws DataAccessException, SQLException {
        // Create a test user
        service.registerUser(new UserData("user", "password", "email"));
        // Test for a user that exists with the correct username
        Assertions.assertEquals("user", service.loginUser(new UserData("user", "password", "email")).username());
        // Test for a non-null auth token
        Assertions.assertNotNull(service.loginUser(new UserData("user", "password", "email")).authToken());
    }

    @Test
    void testLoginUser() throws DataAccessException, SQLException {
        // Create a test user
        service.registerUser(new UserData("user", "password", "email"));
        // Test that loginUser will fail with an exception when using an incorrect password
        Assertions.assertThrows(DataAccessException.class, () -> service.loginUser(new UserData("user", "badPassword", "email")));
    }

    @Test
    void registerUser() throws DataAccessException, SQLException {
        // Create a test user
        UserData testUser = new UserData("user", "password", "email");
        // Test that the user is created with the correct username
        Assertions.assertEquals("user", service.registerUser(testUser).username());
        // Test that the user is created with a non-null auth token
        Assertions.assertNotNull(service.registerUser(testUser).authToken());
    }

    @Test
    void testRegisterUser() throws DataAccessException, SQLException {
        // Create a test user
        UserData testUser = new UserData("user", "password", "email");
        // Register the user
        service.registerUser(testUser);
        // Test that registerUser will fail with an exception when trying to register the same user twice
        Assertions.assertThrows(DataAccessException.class, () -> service.registerUser(testUser));
    }

    @Test
    void deleteDatabase() throws DataAccessException, SQLException {
        // Create a test user and game
        AuthData testUser = service.registerUser(new UserData("user", "password", "email"));
        service.createGame(testUser.authToken(), new GameData(0, null, null, "gameName", null));
        // Test that the database is cleared
        service.deleteDatabase();
        // Test that auth tokens are deleted by verifying that listGames fails with an exception
        Assertions.assertThrows(DataAccessException.class, () -> service.listGames(testUser.authToken()));
    }

    @Test
    void createAuth() throws DataAccessException, SQLException {
        // Create a test user
        UserData testUser = new UserData("user", "password", "email");
        // Register the user
        service.registerUser(testUser);
        // Test that the auth token is created and is not null
        Assertions.assertNotNull(service.createAuth(testUser));
    }

    @Test
    void testCreateAuth() throws DataAccessException, SQLException {
        // Create a test user
        service.registerUser(new UserData("user", "password", "email"));
        // Test that createAuth will fail with an exception when trying to log in with an incorrect password
        Assertions.assertThrows(DataAccessException.class, () -> service.loginUser(new UserData("user", "badPassword", "email")));
    }

    @Test
    void deleteAuth() throws DataAccessException, SQLException {
        // Create a test user and log in
        UserData testUser = new UserData("user", "password", "email");
        AuthData authData = service.registerUser(testUser);
        // Delete the auth token
        service.deleteAuth(authData.authToken());
        // Assert that the auth token is deleted by verifying that listGames fails with an exception
        Assertions.assertThrows(DataAccessException.class, () -> service.listGames(authData.authToken()));
    }

    @Test
    void createGame() throws DataAccessException, SQLException {
        // Create a test user and game
        AuthData testUser = service.registerUser(new UserData("user", "password", "email"));
        GameData testGame = new GameData(0, null, null, "gameName", null);
        // Test that the game is created with the correct game name
        Assertions.assertEquals("gameName", service.createGame(testUser.authToken(), testGame).gameName());
    }

    @Test
    void testCreateGame() throws DataAccessException, SQLException {
        GameData testGame = new GameData(0, null, null, "gameName", null);
        // Test that createGame will fail with an exception when using an invalid auth token
        Assertions.assertThrows(DataAccessException.class, () -> service.createGame("badAuthToken", testGame));
    }

    @Test
    void updateGame() throws DataAccessException, SQLException {
        // Create a test user and game
        AuthData testUser = service.registerUser(new UserData("user", "password", "email"));
        GameData testGame = service.createGame(testUser.authToken(), new GameData(0, null, null, "gameName", null));
        // Update the game by joining as the white player
        service.updateGame(testUser.authToken(), new JoinGameRequests(ChessGame.TeamColor.WHITE, testGame.gameID()));
        // Test that the game is updated with the correct white player username
        Assertions.assertEquals("user", service.createGame(testUser.authToken(), testGame).whiteUsername());
    }

    @Test
    void testUpdateGame() throws DataAccessException, SQLException {
        // Test that updateGame will fail with an exception when using an invalid auth token
        Assertions.assertThrows(DataAccessException.class, () -> service.updateGame("badAuthToken", new JoinGameRequests(ChessGame.TeamColor.WHITE, 1)));
    }

    @Test
    public void testUpdateGame_InvalidAuthToken() throws DataAccessException, SQLException {
        // Arrange
        String invalidAuth = "invalidAuthToken";
        JoinGameRequests joinReqs = new JoinGameRequests(ChessGame.TeamColor.WHITE, 1);

        // Act & Assert
        // Test that updateGame will fail with an exception when using an invalid auth token
        Assertions.assertThrows(DataAccessException.class, () -> service.updateGame(invalidAuth, joinReqs));
    }
}