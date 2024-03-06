package dataAccessTests;

import dataAccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.Service;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseUserTest {

    private final Service service = new Service();

    @Test
    void registerUser() throws SQLException, DataAccessException {
        // Test that registerUser works
        service.registerUser(new UserData("user", "password", "email"));

    }

    @Test
    void getUser() {
    }

    @Test
    void deleteUsers() {
    }
}