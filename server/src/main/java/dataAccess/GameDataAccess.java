package dataAccess;

public interface GameDataAccess {

    void createGame() throws DataAccessException;

    void deleteGame() throws DataAccessException;

    void getGame() throws DataAccessException;

    void listGames() throws DataAccessException;

    void updateGame() throws DataAccessException;

}
