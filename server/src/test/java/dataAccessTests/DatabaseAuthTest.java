package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseAuth;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseAuthTest {

    private DatabaseAuth databaseAuth;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        // Create a new instance of the DatabaseAuth class and delete all auths before each test
        databaseAuth = new DatabaseAuth();
        databaseAuth.deleteAllAuths();
    }

    @Test
    void createAuth_Success() throws SQLException, DataAccessException {
        // Test that createAuth works successfully
        UserData userData = new UserData("user", "password", "email");
        String createdAuth = databaseAuth.createAuth(userData);
        assertNotNull(createdAuth);
    }


    @Test
    void getAuth_NonExistentAuth() throws SQLException, DataAccessException {
        // Test that getAuth returns null for a non-existent auth
        AuthData retrievedAuth = databaseAuth.getAuth("nonExistentUsername");
        assertNull(retrievedAuth);
    }

    @Test
    void deleteAuth_Success() throws SQLException, DataAccessException {
        // Test that deleteAuth deletes the auth successfully
        UserData userData = new UserData("user", "password", "email");
        databaseAuth.createAuth(userData);
        databaseAuth.deleteAuth(userData.username());
        assertNull(databaseAuth.getAuth(userData.username()));
    }

    @Test
    void deleteAuth_NonExistentAuth() throws SQLException, DataAccessException {
        // Test that deleteAuth does not throw an exception for a non-existent auth
        assertDoesNotThrow(() -> databaseAuth.deleteAuth("nonExistentUsername"));
    }

    @Test
    void deleteAllAuths_Success() throws SQLException, DataAccessException {
        // Test that deleteAllAuths deletes all auths successfully
        UserData userData = new UserData("user", "password", "email");
        UserData userData2 = new UserData("user2", "password2", "email2");
        databaseAuth.createAuth(userData);
        databaseAuth.createAuth(userData2);
        databaseAuth.deleteAllAuths();
        assertNull(databaseAuth.getAuth(userData.username()));
        assertNull(databaseAuth.getAuth(userData2.username()));
    }

    @Test
    void deleteAllAuths_EmptyDatabase() throws SQLException, DataAccessException {
        // Test that deleteAllAuths works correctly when the database is already empty
        databaseAuth.deleteAllAuths();
        assertNull(databaseAuth.getAuth("nonExistentUsername"));
    }
}