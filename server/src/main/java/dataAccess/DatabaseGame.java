package dataAccess;

import chess.ChessGame;
import model.GameData;

import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseGame implements GameDataAccess {

    private final ArrayList<GameData> games = new ArrayList<>();

    public DatabaseGame() throws DataAccessException, SQLException {
        configureDatabase();
    }

    public void deleteGame(int gameId) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM game WHERE gameId=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("delete user failed");
        }
    }

    public void updateGame(GameData game) throws DataAccessException {
        // only update game not players
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE game SET ChessGame=? WHERE gameId=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, new Gson().toJson(game.game()));
                ps.setInt(2, game.gameID());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("update user failed");
        }
    }


    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO game (gameName, ChessGame) VALUES(?, ?)";
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, gameName);
                ps.setString(2, new Gson().toJson(new ChessGame())); // Gson.tojson...ChessGame
                ps.executeUpdate();
                try (var rs = ps.getGeneratedKeys()) {
                    if(rs.next()){
                        int gameID = rs.getInt(1);
                        return new GameData(gameID, null, null, gameName, new ChessGame());
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("get user failed");
        }
        return null;
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, ChessGame FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameId);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame chessGame = new Gson().fromJson(rs.getString("ChessGame"), ChessGame.class);

                        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("get user failed");
        }
        return null;
    }

    @Override
    public void addWhitePlayer(int gameId, String username) throws DataAccessException, SQLException {
        // if player already exists throw exception
        var statement = "UPDATE game SET whiteUsername=? WHERE gameId=?";
        executeUpdate(statement, username, gameId);
    }

    @Override
    public void addBlackPlayer(int gameId, String username) throws DataAccessException, SQLException {
        // if player already exists throw exception
        var statement = "UPDATE game SET blackUsername=? WHERE gameId=?";
        executeUpdate(statement, username, gameId);
    }

    @Override
    public void deleteGames() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("delete users failed");
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var games = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, ChessGame FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame chessGame = new Gson().fromJson(rs.getString("ChessGame"), ChessGame.class);

                        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("get user failed");
        }
        // return all games in games then clear games
        return games;
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case ChessGame p -> ps.setString(i + 1, p.toString());
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("problem in executeUpdate");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
              `gameId` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL UNIQUE,
              `ChessGame` TEXT NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("problem configuring game database");
        }
    }




}
