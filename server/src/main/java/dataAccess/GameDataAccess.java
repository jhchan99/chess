package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDataAccess {

    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame(int game) throws DataAccessException;
    GameData addWhitePlayer(int gameId, String username) throws DataAccessException;
    GameData addBlackPlayer(int gameId, String username) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException;

}
