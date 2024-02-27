package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuth implements AuthDataAccess{

    final private HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public String createAuth(UserData user) throws DataAccessException {
        // create new auth token for user
        String authToken = UUID.randomUUID().toString();
        // add auth token to auths
        auths.put(authToken, new AuthData(user.username(), authToken));
        return authToken;
    }

    @Override
    public AuthData getAuth(String auth) throws DataAccessException {
        return auths.get(auth);
    }

    @Override
    public void deleteAuth(String auth){
        // remove auth token from auths
        auths.remove(auth);
    }
}
