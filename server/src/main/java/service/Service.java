package service;

import dataAccess.*;
import model.UserData;
import model.AuthData;
import org.eclipse.jetty.server.Authentication;

import java.util.Map;

public class Service {

    private final UserDataAccess userAccess = new MemoryUser();
    private final AuthDataAccess authAccess = new MemoryAuth();


    public UserData getUser(UserData user) throws DataAccessException {
        if(userAccess.getUser(user) != null && userAccess.getUser(user).password().equals(user.password())) {
            return user;
        }else {
            return createUser(user);
        }
    }

    public UserData createUser(UserData user) throws DataAccessException {
        return userAccess.createUser(user);
    }
    public void deleteDatabase() throws DataAccessException {
        userAccess.deleteDatabase();
    }

    public String createAuth() throws DataAccessException {
        authAccess.createAuth();

    }


}

