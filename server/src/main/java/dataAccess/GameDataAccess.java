package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDataAccess {

    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame(int game) throws DataAccessException;
    void addWhitePlayer(int gameId, String username) throws DataAccessException;
    void addBlackPlayer(int gameId, String username) throws DataAccessException;
    void deleteGames() throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;

}
