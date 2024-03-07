package dataAccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public interface GameDataAccess {

    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame(int game) throws DataAccessException;
    void addWhitePlayer(int gameId, String username) throws DataAccessException, SQLException;
    void addBlackPlayer(int gameId, String username) throws DataAccessException, SQLException;
    void deleteGames() throws DataAccessException, SQLException;
    Collection<GameData> listGames() throws DataAccessException;

}
