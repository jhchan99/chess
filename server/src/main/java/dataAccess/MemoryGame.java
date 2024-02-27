package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGame implements GameDataAccess{

    private final HashMap<Integer,GameData> games = new HashMap<>();

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        int nextID = 1;
        GameData game = new GameData(nextID++, null, null, gameName, new ChessGame());
        games.put(game.gameID(), game);
        return game;
    }

    @Override
    public GameData addWhitePlayer(int gameID, String username) {
        // add white player to game
        GameData getGame = games.get(gameID);
        return new GameData(getGame.gameID(), username, getGame.blackUsername(), getGame.gameName(), getGame.game());
    }
    public GameData addBlackPlayer(int gameID, String username) {
        // add black player to game
        GameData getGame = games.get(gameID);
        return new GameData(getGame.gameID(), getGame.whiteUsername(), username, getGame.gameName(), getGame.game());
    }

    @Override
    public GameData getGame(int gameID) {
        // return game with name gameName
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames(){
        // return all games
        return games.values();
    }

    @Override
    public void updateGame(GameData game){
        // update game with new game data
        games.put(game.gameID(), game);
    }


}
