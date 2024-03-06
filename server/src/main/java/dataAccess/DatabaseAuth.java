package dataAccess;

import model.AuthData;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static java.sql.Types.NULL;

public class DatabaseAuth implements AuthDataAccess{

    public DatabaseAuth() throws DataAccessException {
        configureDatabase();
    }
    @Override
    public String createAuth(UserData user) throws DataAccessException, SQLException {
        // create new auth for user
        String authToken = UUID.randomUUID().toString();
        // add auth to database
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        executeUpdate(statement, user.username(), authToken);
        return authToken;

    }

    @Override
    public AuthData getAuth(String auth) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setString(1, auth);
                try(var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String authToken = rs.getString("authToken");
                        String username = rs.getString("username");
                        return new AuthData(username, authToken);
                    }
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("getAuth failed while trying to retrieve from DatabaseAuth");
        }
        return null;
    }

    @Override
    public void deleteAuth(String auth) throws DataAccessException, SQLException {
        // delete a single authToken
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE authToken FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("delete users failed while trying in DatabaseAuth");
        }

    }

    @Override
    public void deleteAllAuths() throws DataAccessException {
        // delete all authTokens this is mainly for testing
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM auth";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("delete users failed");
        }
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try(var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for(int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if(param == null) ps.setNull(i+1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    rs.getInt(1);
                }

            }
        } catch (SQLException e){
            throw new DataAccessException("trouble updating table");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `username` varchar(256) NOT NULL PRIMARY KEY,
              `authToken` varchar(256) NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("there was an issue configuring the database");
        }
    }
}
