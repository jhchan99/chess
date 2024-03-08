package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseUser;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUserTest {

    private DatabaseUser databaseUser;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        // Create a new instance of the DatabaseUser class and delete the database before each test
        databaseUser = new DatabaseUser();
        databaseUser.deleteUsers();
    }

    @Test
    void registerUser_Success() throws SQLException, DataAccessException {
        // Test that registerUser works successfully
        UserData userData = new UserData("user", "password", "email");
        UserData registeredUser = databaseUser.registerUser(userData);
        assertNotNull(registeredUser);
        assertEquals(userData.username(), registeredUser.username());
    }

    @Test
    void registerUser_DuplicateUser() throws SQLException, DataAccessException {
        // Test that registerUser throws an exception when registering a duplicate user
        UserData userData = new UserData("user", "password", "email");
        databaseUser.registerUser(userData);
        assertThrows(DataAccessException.class, () -> databaseUser.registerUser(userData));
    }

    @Test
    void getUser_Success() throws SQLException, DataAccessException {
        // Test that getUser retrieves the correct user
        UserData userData = new UserData("user", "password", "email");
        databaseUser.registerUser(userData);
        UserData retrievedUser = databaseUser.getUser(userData);
        assertNotNull(retrievedUser);
        assertEquals(userData.username(), retrievedUser.username());
        assertEquals(userData.password(), retrievedUser.password());
        assertEquals(userData.email(), retrievedUser.email());
    }

    @Test
    void getUser_NonExistentUser() throws SQLException, DataAccessException {
        // Test that getUser returns null for a non-existent user
        UserData retrievedUser = databaseUser.getUser(new UserData("user", "password", "email"));
        assertNull(retrievedUser);
    }

    @Test
    void deleteUsers_Success() throws SQLException, DataAccessException {
        // Test that deleteUsers deletes all users successfully
        UserData user1 = new UserData("user1", "password1", "email1");
        UserData user2 = new UserData("user2", "password2", "email2");
        databaseUser.registerUser(user1);
        databaseUser.registerUser(user2);
        databaseUser.deleteUsers();
        assertNull(databaseUser.getUser(user1));
        assertNull(databaseUser.getUser(user2));
    }

    @Test
    void deleteUsers_EmptyDatabase() throws SQLException, DataAccessException {
        // Test that deleteUsers works correctly when the database is already empty
        databaseUser.deleteUsers();
        assertNull(databaseUser.getUser(new UserData("user", "password", "email")));
    }
}