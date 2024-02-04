package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor teamTurn;
    ChessBoard board;


    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessBoard board = getBoard();
        ChessPiece piece = board.getPiece(startPosition);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                ChessPosition endPosition = new ChessPosition(i, j);
                if(isMoveValid(startPosition, endPosition, board, piece.getPieceType())){
                    validMoves.add(new ChessMove(startPosition, endPosition, piece.getPieceType()));
                }
            }
        }
        return validMoves;
    }

    private boolean isMoveValid(ChessPosition startPosition, ChessPosition endPosition, ChessBoard board, ChessPiece.PieceType pieceType) {
        if(startPosition.equals(endPosition)) {
            return false;
        }
        ChessPiece startPiece = board.getPiece(startPosition);
        ChessPiece endPiece = board.getPiece(endPosition);
        if(startPiece.getTeamColor() == endPiece.getTeamColor()) {
            return false;
        }
        return true;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        for(ChessMove validMove : validMoves(move.start)) {
            if(validMove.equals(move)) {
                return;
            }
        }
        throw new InvalidMoveException();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard board = getBoard();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                ChessPosition newPosition = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(newPosition);

            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessBoard board = getBoard();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                ChessPosition kingPosition = new ChessPosition(i, j);
                if(board.getPiece(kingPosition).getTeamColor() == teamColor) {
                    for(ChessMove validMove : validMoves(kingPosition)) {
                        if(validMove.end == kingPosition) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
