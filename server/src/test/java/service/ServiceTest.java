package service;

import chess.ChessGame;
import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.JoinGameRequests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;


class ServiceTest {

    private final Service service = new Service();


    @Test
    // Test that listGames works
    void listGames() throws DataAccessException, SQLException {
        // create test user
        AuthData testUser = service.registerUser(new UserData("user", "password", "email"));
        // Test for empty list of games
        Assertions.assertEquals(0, service.listGames(testUser.authToken()).size());
        // create test game
        service.createGame(testUser.authToken(), new GameData(0, null, null, "gameName", null));
        // Test for list of games with one game
        Assertions.assertEquals(1, service.listGames(testUser.authToken()).size());
    }

    @Test
    // test listGames can fail with an exception
    void testListGames() throws DataAccessException, SQLException {
        // Test that list will fail with an exception
        Assertions.assertThrows(DataAccessException.class, () -> service.listGames("badAuthToken"));
    }

    @Test
    void loginUser() throws DataAccessException, SQLException {
        // Test for a user that exists with username
        Assertions.assertEquals("user", service.loginUser(new UserData("user", "password", "email")).username());
        // Test for not null auth token
        Assertions.assertNotNull(service.loginUser(new UserData("user", "password", "email")).authToken());
    }

    @Test
    // test loginUser can fail with an exception
    void testLoginUser() throws DataAccessException, SQLException {
        // Test that loginUser will fail with an exception
        Assertions.assertThrows(DataAccessException.class, () -> service.loginUser(new UserData("user", "badPassword", "email")));
    }

    @Test
    void registerUser() throws DataAccessException, SQLException {
        // first delete database
        service.deleteDatabase();
        // create test user
        UserData testUser = new UserData("user", "password", "email");
        // Test that the user is created with the correct username
        Assertions.assertEquals("user", service.registerUser(testUser).username());
        // clear the database
        service.deleteDatabase();
        // Test that the user is created with the correct auth token
        Assertions.assertNotNull(service.registerUser(testUser).authToken());
    }

    @Test
    // test registerUser can fail with an exception
    void testRegisterUser() throws DataAccessException, SQLException {
        // first clear database
        service.deleteDatabase();
        // create test user
        UserData testUser = new UserData("user", "password", "email");
        // Test that registerUser will fail with an exception try to register the same user twice
        // create another test user
        service.registerUser(testUser);
        Assertions.assertThrows(DataAccessException.class, () -> service.registerUser(testUser));
    }

    @Test
    void deleteDatabase() throws DataAccessException, SQLException {
        // create test user and game
        AuthData testUser = service.registerUser(new UserData("user", "password", "email"));
        service.createGame(testUser.authToken(), new GameData(0, null, null, "gameName", null));
        // test that the database is cleared
        service.deleteDatabase();
        // test that auth tokens are deleted
        Assertions.assertThrows(DataAccessException.class, () -> service.listGames(testUser.authToken()));
    }

    @Test
    void createAuth() throws DataAccessException{
        // create test user
        UserData testUser = new UserData("user", "password", "email");
        // Test that the auth token is created
        Assertions.assertNotNull(service.createAuth(testUser));
    }

    @Test
    // test createAuth can fail with an exception
    void testCreateAuth() throws DataAccessException, SQLException {
        // create test user
        service.registerUser(new UserData("user", "password", "email"));
        // Test that createAuth will fail with an exception by trying to log in with wrong password
        Assertions.assertThrows(DataAccessException.class, () -> service.loginUser(new UserData("user", "badPassword", "email")));
    }

    @Test
    void deleteAuth() throws DataAccessException{
        // assert that the auth token is deleted by logging in and assertThrows
    }

    @Test
    void createGame() throws DataAccessException, SQLException {
        // create test user and game
        AuthData testUser = service.registerUser(new UserData("user", "password", "email"));
        GameData testGame = new GameData(0, null, null, "gameName", null);
        // Test that the game is created with the correct game name
        Assertions.assertEquals("gameName", service.createGame(testUser.authToken(), testGame).gameName());
    }

    @Test
    // test createGame can fail with an exception
    void testCreateGame() throws DataAccessException{
        GameData testGame = new GameData(0, null, null, "gameName", null);
        // Test that createGame will fail with an exception
        Assertions.assertThrows(DataAccessException.class, () -> service.createGame("badAuthToken", testGame));
    }

    @Test
    void updateGame() throws DataAccessException, SQLException {
        // create test user and game
        AuthData testUser = service.registerUser(new UserData("user", "password", "email"));
        GameData testGame = new GameData(0, null, null, "gameName", null);
        // Test that the game is updated with the correct game name
        Assertions.assertEquals("gameName", service.createGame(testUser.authToken(), testGame).gameName());
    }

    @Test
    // test updateGame can fail with an exception
    void testUpdateGame() throws DataAccessException{
        // Test that updateGame will fail with an exception
        Assertions.assertThrows(DataAccessException.class, () -> service.updateGame("badAuthToken", new JoinGameRequests(ChessGame.TeamColor.WHITE, 1)));
    }

    @Test()
    public void testUpdateGame_InvalidAuthToken() throws DataAccessException {
        // Arrange
        String invalidAuth = "invalidAuthToken";
        JoinGameRequests joinReqs = new JoinGameRequests(ChessGame.TeamColor.WHITE, 1);


        // Act & Assert
        Assertions.assertThrows(DataAccessException.class, () -> service.updateGame(invalidAuth, joinReqs));
    }
}