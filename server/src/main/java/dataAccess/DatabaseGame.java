package dataAccess;

import model.GameData;

import java.util.Collection;

public class DatabaseGame implements GameDataAccess{
    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int game) throws DataAccessException {
        return null;
    }

    @Override
    public void addWhitePlayer(int gameId, String username) throws DataAccessException {

    }

    @Override
    public void addBlackPlayer(int gameId, String username) throws DataAccessException {

    }

    @Override
    public void deleteGames() throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }
}
