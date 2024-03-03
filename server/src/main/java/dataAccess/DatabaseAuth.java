package dataAccess;

import model.AuthData;
import model.UserData;

public class DatabaseAuth implements AuthDataAccess{
    @Override
    public String createAuth(UserData user) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String auth) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String auth) throws DataAccessException {

    }

    @Override
    public void deleteAllAuths() throws DataAccessException {

    }
}
