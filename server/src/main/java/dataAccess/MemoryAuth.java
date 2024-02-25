package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuth implements AuthDataAccess{

    final private HashMap<String, MemoryAuth> auths = new HashMap<>();

    @Override
    public String createAuth() throws DataAccessException {
        String auth = UUID.randomUUID().toString();
        return auth;
    }

    @Override
    public AuthData getAuth(AuthData auth) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String auth) throws DataAccessException {

    }
}
