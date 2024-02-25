package dataAccess;

import model.AuthData;

public interface AuthDataAccess {

    String createAuth() throws DataAccessException;

    AuthData getAuth(AuthData auth) throws DataAccessException;

    void deleteAuth(String auth) throws DataAccessException;

}
