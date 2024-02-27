package model.requests;

import chess.ChessGame;

public record JoinGameRequests(ChessGame.TeamColor playerColor, int gameID) {
}
