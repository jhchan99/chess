package dataAccess;

import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
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

    private void executeUpdate(String statement, Object ... params) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for(int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i+1, p);
                    if (param instanceof Integer p) ps.setInt(i+1, p);
                    if (param instanceof )
                }
            }

        }
    }
}
