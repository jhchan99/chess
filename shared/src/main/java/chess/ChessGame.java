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
        // create a new set of valid moves
        Collection<ChessMove> validMoves = new HashSet<>();
        // get the current board state
        ChessBoard board = getBoard();
        // get the piece at the start position
        ChessPiece piece = board.getPiece(startPosition);
        // get the possible moves for the piece
        Collection<ChessMove> possibleMoves = new HashSet<>();
        possibleMoves = piece.pieceMoves(board, startPosition);
        // check every tile on the board
        for(ChessMove move : possibleMoves) {
            // make the move on the board and check if the king is in check
            ChessPiece pieceAtEnd = board.getPiece(move.end);
            board.addPiece(move.end, piece);
            board.addPiece(move.start, null);
            if(!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }
            // if the king is in check
            board.addPiece(move.start, piece);
            // remove the piece from the old position
            board.addPiece(move.end, pieceAtEnd);
        }
        return validMoves;
    }


    private boolean isMoveValid(ChessPosition startPosition, ChessPosition endPosition, ChessBoard board, ChessPiece.PieceType pieceType) {
        return false;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // check if the move is in the set of valid moves
        if(validMoves(move.start).contains(move)){
            // add the piece to the new position
            ChessPiece piece = getBoard().getPiece(move.start);
            // if the piece is a pawn and it is moving to the last row, promote it
            getBoard().addPiece(move.end, piece);
            // remove the piece from the old position
            getBoard().addPiece(move.start, null);
        }
        else {
            throw new InvalidMoveException("Invalid move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // get the current board state
        ChessBoard board = getBoard();
        // check every tile on the board
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++) {
                // get the position of the current tile
                ChessPosition newPosition = new ChessPosition(i, j);
                // if there is a piece on the tile get the piece on the current tile
                ChessPiece piece = board.getPiece(newPosition);
                // get the location of the king
                ChessPosition kingPosition = findKing(teamColor);
                // get the possible moves for the piece
                Collection<ChessMove> possibleMoves = piece.pieceMoves(board, newPosition);
                // check if the king is in check
                // if the kings position is in the possible moves of the piece
                for(ChessMove move : possibleMoves) {
                    if(move.end.equals(kingPosition)) {
                        return true;
                    }
                }
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
//        // get the current board state
//        ChessBoard board = getBoard();
//        // check every tile on the board
//        for(int i = 0; i < 8; i++){
//            for(int j = 0; j < 8; j++) {
//                // get the position of the current tile
//                ChessPosition newPosition = new ChessPosition(i, j);
//                // get the piece on the current tile
//                ChessPiece piece = board.getPiece(newPosition);
//
//            }
//        }
        throw new RuntimeException("Not implemented");

    }

    private ChessPosition findKing(TeamColor teamColor) {
        ChessBoard board = getBoard();

        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++) {
                ChessPosition kingPosition = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(kingPosition);
                if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && board.getPiece(kingPosition).getTeamColor() == teamColor) {
                    return kingPosition;
                }

            }
        }
        return null;
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
