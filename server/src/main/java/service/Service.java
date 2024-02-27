package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import model.GameData;
import model.requests.JoinGameRequests;

import java.util.Collection;
import java.util.Objects;

public class Service {

    private final UserDataAccess userAccess = new MemoryUser();
    private final AuthDataAccess authAccess = new MemoryAuth();
    private final GameDataAccess gameAccess = new MemoryGame();


    public AuthData loginUser(UserData user) throws DataAccessException {
        // Check if user exists
        UserData user1 = userAccess.getUser(user);
        // If user exists and password matches, return new auth token
        if (user1 != null && Objects.equals(user1.password(), user.password())) {
            // get new auth token for user return auth token
            return new AuthData(user1.username(), createAuth(user1));
        }
        // If user does not exist or password does not match, return null
        throw new DataAccessException("User not found");
    }

    public AuthData registerUser(UserData user) throws DataAccessException {
        // check if user already exists
        if (userAccess.getUser(user) != null) {
            throw new DataAccessException("User already exists");
        }
        // create user
        userAccess.registerUser(user);
        // return new auth token
        return new AuthData(user.username(), createAuth(user));
    }



    public void deleteUser(UserData user) throws DataAccessException {
        userAccess.deleteUser(user);
    }

    public void deleteDatabase() throws DataAccessException {
        userAccess.deleteDatabase();
    }
    public String createAuth(UserData user) throws DataAccessException {
        return authAccess.createAuth(user);
    }

    public void deleteAuth(String auth) throws DataAccessException {
        // check if auth exists in database
        if (auth == null || authAccess.getAuth(auth) == null){
            throw new DataAccessException("Auth token not found");
        }
        // delete auth from database
        authAccess.deleteAuth(auth);
    }

    public GameData createGame(String auth, GameData game) throws DataAccessException {
        // check if auth exists in database
        if(auth == null ||  authAccess.getAuth(auth) == null) {
            throw new DataAccessException("Auth token not found");
        }
        // create game
        return gameAccess.createGame(game.gameName());
    }

    public GameData updateGame(String auth, JoinGameRequests joinReqs) throws DataAccessException {
        // check if auth exists in database
        if(auth == null ||  authAccess.getAuth(auth) == null) {
            throw new DataAccessException("Auth token not found");
        }
        // check if gameID exists in games database
        if(joinReqs.gameID() == 0 || gameAccess.getGame(joinReqs.gameID()).gameName() == null) {
            throw new DataAccessException("Game not found");
        }
        // get game based on gameID
        GameData game = gameAccess.getGame(joinReqs.gameID());
        // get user based on auth token
        UserData user = userAccess.getUser(new UserData(authAccess.getAuth(auth).username(), null, null));
        // if user is white color add user to game
        if(joinReqs.playerColor() == ChessGame.TeamColor.WHITE) {
            gameAccess.addWhitePlayer(game.gameID(), user.username());
        } else if(joinReqs.playerColor() == ChessGame.TeamColor.BLACK) {
            gameAccess.addBlackPlayer(game.gameID(), user.username());
        } else if(joinReqs.playerColor() == null) {
            return game;
        } else {
            throw new DataAccessException("Player color already taken");
        }

    }

    public Collection<GameData> listGames (String auth) throws DataAccessException {
        // check if auth exists in database
        if(auth == null ||  authAccess.getAuth(auth) == null) {
            throw new DataAccessException("Auth token not found");
        }
        // get list of games
        return gameAccess.listGames();
    }


}

