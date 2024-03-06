package dataAccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public interface AuthDataAccess {

    String createAuth(UserData user) throws DataAccessException, SQLException;

    AuthData getAuth(String auth) throws DataAccessException, SQLException;

    void deleteAuth(String auth) throws DataAccessException, SQLException;

    void deleteAllAuths() throws DataAccessException;

}
