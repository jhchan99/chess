package dataAccess;

import chess.ChessGame;
import model.GameData;

import com.google.gson.Gson;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseGame implements GameDataAccess {

    // add logging to all methods
    private static final Logger logger = Logger.getLogger(DatabaseGame.class.getName());

    public Collection<GameData> gameList = new ArrayList<>();

    public DatabaseGame() throws DataAccessException, SQLException {
        configureDatabase();
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
            e.printStackTrace();
            throw new DataAccessException("get user failed");
        }
        logger.info("Game created");
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
            e.printStackTrace();
            throw new DataAccessException("get user failed");
        }
        return null;
    }

    @Override
    public void addWhitePlayer(int gameId, String username) throws DataAccessException, SQLException {
        var statement = "INSERT INTO game (whiteUsername=?) WHERE gameId=?";
        executeUpdate(statement, username, gameId);
    }

    @Override
    public void addBlackPlayer(int gameId, String username) throws DataAccessException, SQLException {
        var statement = "INSERT INTO game (blackUsername=?) WHERE gameId=?";
        executeUpdate(statement, username, gameId);
    }

    @Override
    public void deleteGames() throws DataAccessException, SQLException {
        var statement = "DELETE FROM game";
        executeUpdate(statement);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, ChessGame FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame chessGame = new Gson().fromJson(rs.getString("ChessGame"), ChessGame.class);

                        gameList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("get user failed");
        }
        if(gameList.isEmpty()){
            return null;
        }
        return gameList;
    }

    private void executeUpdate(String statement, Object... params) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    rs.getInt(1);
                }

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
              `gameName` varchar(256) NOT NULL,
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
            ex.printStackTrace();
            throw new DataAccessException("problem configuring game database");
        }
    }




}
