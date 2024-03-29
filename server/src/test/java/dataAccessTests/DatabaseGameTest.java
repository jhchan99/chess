package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.DatabaseGame;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseGameTest {

    private DatabaseGame databaseGame;

    @BeforeEach
    void setUp() throws DataAccessException, SQLException {
        // Create a new instance of the DatabaseGame class and delete all games before each test
        databaseGame = new DatabaseGame();
        databaseGame.deleteGames();
    }

    @Test
    void createGame_Success() throws SQLException, DataAccessException {
        // Test that createGame works successfully
        GameData gameData = new GameData(0, null, null, "testGame", new ChessGame());
        GameData createdGame = databaseGame.createGame(gameData.gameName());
        assertNotNull(createdGame);
        assertTrue(createdGame.gameID() > 0);
        assertNull(createdGame.whiteUsername());
        assertNull(createdGame.blackUsername());
        assertEquals(gameData.gameName(), createdGame.gameName());
    }

    @Test
    void getGame_Success() throws SQLException, DataAccessException {
        // Test that getGame retrieves the correct game
        GameData gameData = new GameData(0, null, null, "testGame", new ChessGame());
        GameData createdGame = databaseGame.createGame(gameData.gameName());
        GameData retrievedGame = databaseGame.getGame(createdGame.gameID());
        assertNotNull(retrievedGame);
        assertEquals(createdGame.gameID(), retrievedGame.gameID());
        assertEquals(createdGame.whiteUsername(), retrievedGame.whiteUsername());
        assertEquals(createdGame.blackUsername(), retrievedGame.blackUsername());
        assertEquals(createdGame.gameName(), retrievedGame.gameName());
    }

    @Test
    void createGame_DuplicateGame() throws SQLException, DataAccessException {
        // Test that createGame throws an exception when creating a duplicate game
        GameData gameData = new GameData(0, null, null, "testGame", new ChessGame());
        databaseGame.createGame(gameData.gameName());
        assertThrows(DataAccessException.class, () -> databaseGame.createGame(gameData.gameName()));
    }

    @Test
    void getGame_NonExistentGame() throws SQLException, DataAccessException {
        // Test that getGame returns null for a non-existent game
        GameData retrievedGame = databaseGame.getGame(0);
        assertNull(retrievedGame);
    }






    @Test
    void deleteGames_Success() throws SQLException, DataAccessException {
        // Test that deleteGames deletes all games successfully
        GameData game1 = new GameData(0, null, null, "game1", new ChessGame());
        GameData game2 = new GameData(0, null, null, "game2", new ChessGame());
        databaseGame.createGame(game1.gameName());
        databaseGame.createGame(game2.gameName());
        databaseGame.deleteGames();
        assertNull(databaseGame.getGame(game1.gameID()));
        assertNull(databaseGame.getGame(game2.gameID()));
    }

    @Test
    void listGames_Success() throws SQLException, DataAccessException {
        // Test that listGames returns all games successfully
// Test that listGames returns all games successfully
        GameData game1 = new GameData(0, null, null, "game1", new ChessGame());
        GameData game2 = new GameData(0, null, null, "game2", new ChessGame());
        GameData createdGame1 = databaseGame.createGame(game1.gameName());
        GameData createdGame2 = databaseGame.createGame(game2.gameName());
        Collection<GameData> gameList = databaseGame.listGames();
        assertNotNull(gameList);
        assertEquals(2, gameList.size());
        assertTrue(gameList.stream().anyMatch(game -> game.equals(createdGame1)));
        assertTrue(gameList.stream().anyMatch(game -> game.equals(createdGame2)));
    }
}