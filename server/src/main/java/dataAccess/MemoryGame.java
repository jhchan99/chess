package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGame implements GameDataAccess{

    private final HashMap<Integer,GameData> games = new HashMap<>();

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        // create new game with name gameName
        int gameID = games.size() + 1;
        games.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame()));
        return games.get(gameID);
    }

    @Override
    public void addWhitePlayer(int gameID, String username) throws DataAccessException{
        // add white player to game
        GameData getGame = games.get(gameID);
        // if white player is taken throw exception
        if(getGame.whiteUsername() != null) {
            throw new DataAccessException("White player is taken");
        }
        games.put(gameID, new GameData(getGame.gameID(), username, getGame.blackUsername(), getGame.gameName(), getGame.game()));
    }
    public void addBlackPlayer(int gameID, String username) throws DataAccessException{
        // add black player to game
        GameData getGame = games.get(gameID);
        // if black player is taken throw exception
        if(getGame.blackUsername() != null) {
            throw new DataAccessException("Black player is taken");
        }
        games.put(gameID, new GameData(getGame.gameID(), getGame.whiteUsername(), username, getGame.gameName(), getGame.game()));
    }

    @Override
    public GameData getGame(int gameID) {
        // return game with name gameName
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames(){
        // return all games in games
        return games.values();
    }

    @Override
    public void deleteGames() {
        // remove game from games
        games.clear();
    }


}
