package service;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.UserDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.Collection;

public class WebSocketService {

    public final UserDataAccess userAccess = new UserDataAccess() {
        @Override
        public UserData registerUser(UserData user) throws SQLException, DataAccessException {
            return null;
        }

        @Override
        public UserData getUser(UserData user) throws DataAccessException, SQLException {
            return null;
        }

        @Override
        public void deleteUsers() throws DataAccessException {

        }
    };
    public final AuthDataAccess authAccess = new AuthDataAccess() {
        @Override
        public String createAuth(UserData user) throws DataAccessException, SQLException {
            return null;
        }

        @Override
        public AuthData getAuth(String auth) throws DataAccessException, SQLException {
            return null;
        }

        @Override
        public void deleteAuth(String auth) throws DataAccessException, SQLException {

        }

        @Override
        public void deleteAllAuths() throws DataAccessException {

        }
    };
    public final GameDataAccess gameAccess = new GameDataAccess() {
        @Override
        public GameData createGame(String gameName) throws DataAccessException {
            return null;
        }

        @Override
        public GameData getGame(int game) throws DataAccessException {
            return null;
        }

        @Override
        public void addWhitePlayer(int gameId, String username) throws DataAccessException, SQLException {

        }

        @Override
        public void addBlackPlayer(int gameId, String username) throws DataAccessException, SQLException {

        }

        @Override
        public void deleteGames() throws DataAccessException, SQLException {

        }

        @Override
        public Collection<GameData> listGames() throws DataAccessException {
            return null;
        }
    };



}
