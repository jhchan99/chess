package chess;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public PieceType piece;
    public ChessGame.TeamColor color;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        piece = type;
        color = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return piece;
    }


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();

        if(type == PieceType.KING) {
            return kingMoves(board, myPosition);
        }

        if(type == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        }

        return null;

    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> kingMoves = new HashSet<>();
       int row = myPosition.getRow();
       int column = myPosition.getColumn();

        if(row+1 > -1 && row+1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
       }

        if(row-1 > -1 && row-1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(column+1 > -1 && column+1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(column-1 > -1 && column-1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row-1 > -1 && row-1 < 8 && column+1 > -1 && column+1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row+1 > -1 && row+1 < 8 && column-1 > -1 && column-1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row+1 > -1 && row+1 < 8 && column+1 > -1 && column+1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row-1 > -1 && row-1 < 8 && column-1 > -1 && column-1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
       return kingMoves;
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        while(row<8 && col<8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            while(board.getPiece(newPosition)==null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                bishopMoves.add(new ChessMove(myPosition, newPosition, null));
            }
            row++;
            col++;
        }

        return bishopMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return piece == that.piece && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, color);
    }

}
