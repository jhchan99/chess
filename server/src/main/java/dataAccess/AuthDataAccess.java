package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDataAccess {

    String createAuth(UserData user) throws DataAccessException;

    AuthData getAuth(String auth) throws DataAccessException;

    void deleteAuth(String auth) throws DataAccessException;

    void deleteAllAuths() throws DataAccessException;

}
