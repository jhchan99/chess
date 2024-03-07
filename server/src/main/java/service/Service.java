package service;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import model.GameData;
import model.requests.JoinGameRequests;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

public class Service {

    private final UserDataAccess userAccess;

    {
        try {
            userAccess = new DatabaseUser();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final AuthDataAccess authAccess;

    {
        try {
            authAccess = new DatabaseAuth();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final GameDataAccess gameAccess;

    {
        try {
            gameAccess = new DatabaseGame();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public AuthData loginUser(UserData user) throws DataAccessException, SQLException {
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

    public AuthData registerUser(UserData user) throws DataAccessException, SQLException {
        try {
        // check if user already exists
        if (userAccess.getUser(user) != null) {
            throw new DataAccessException("User already exists");
        }
        // check if user is valid
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException("Invalid user data");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String secret = encoder.encode(user.password());
        user = new UserData(user.username(), secret, user.email());
        // create user
        userAccess.registerUser(user);
        // return new auth token
        return new AuthData(user.username(), createAuth(user));
        } catch (SQLException e) {
            throw new DataAccessException("User already exists");
        }
    }

    public void deleteDatabase() throws DataAccessException, SQLException {
        userAccess.deleteUsers();
        authAccess.deleteAllAuths();
        gameAccess.deleteGames();
    }

    public String createAuth(UserData user) throws DataAccessException {
        try {
            return authAccess.createAuth(user);
        }catch (DataAccessException | SQLException e) {
            return "something went wrong while trying to create auth";
        }
    }

    public void deleteAuth(String auth) throws DataAccessException, SQLException {
        // check if auth exists in database
            if (auth == null || authAccess.getAuth(auth) == null) {
                throw new DataAccessException("Auth token not found");
            }
            // delete auth from database
            authAccess.deleteAuth(auth);
    }

    public GameData createGame(String auth, GameData game) throws DataAccessException, SQLException{
        // check if auth exists in database
            if (auth == null || authAccess.getAuth(auth) == null) {
                throw new DataAccessException("Auth token not found");
            }
            // if gameName is null throw error
            if(game.gameName() == null) {
                throw new DataAccessException("Game name is null");
            }
            // create game
            return gameAccess.createGame(game.gameName());

    }

    public void updateGame(String auth, JoinGameRequests joinReqs) throws DataAccessException, SQLException {
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
        // check if game is full
        if(game.whiteUsername() != null && game.blackUsername() != null) {
            throw new DataAccessException("Game is full");
        }
        // if player wants to be a watcher
        if(joinReqs.playerColor() == null){
            return;
        }

        if(joinReqs.playerColor() == ChessGame.TeamColor.WHITE) {
            if(game.whiteUsername() != null){
                throw new DataAccessException("White player is taken");
            }
            gameAccess.addWhitePlayer(game.gameID(), user.username());
        } else {
            if(game.blackUsername() != null){
                throw new DataAccessException("Black player is taken");
            }
            gameAccess.addBlackPlayer(game.gameID(), user.username());
        }
    }

    public Collection<GameData> listGames (String auth) throws DataAccessException, SQLException {
        // check if auth exists in database
        if(auth == null ||  authAccess.getAuth(auth) == null) {
            throw new DataAccessException("Auth token not found");
        }
        // get list of games
        return gameAccess.listGames();

    }


}

