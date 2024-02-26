package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class Service {

    private final UserDataAccess userAccess = new MemoryUser();
    private final AuthDataAccess authAccess = new MemoryAuth();


    public UserData getUser(UserData user) throws DataAccessException {
        UserData retrievedUser = userAccess.getUser(user);
        if (retrievedUser == null) {
            throw new DataAccessException("User not found");
        }
        if (!Objects.equals(retrievedUser.password(), user.password())) {
            throw new DataAccessException("Password incorrect");
        }
        return retrievedUser;
    }

    public void deleteUser(UserData user) throws DataAccessException {
        userAccess.deleteUser(user);
    }

    public void createUser(UserData user) throws DataAccessException {
        userAccess.createUser(user);
    }

    public void deleteDatabase() throws DataAccessException {
        userAccess.deleteDatabase();
    }
    public String createAuth() throws DataAccessException {
        return authAccess.createAuth();
    }

    public void deleteAuth(AuthData auth) throws DataAccessException {
        authAccess.deleteAuth(auth.authToken());
    }

    public ChessGame createGame(AuthData auth) throws DataAccessException {
        if(auth == null) {
            throw new DataAccessException("Auth token not found");
        }
        return new ChessGame();
    }


}

