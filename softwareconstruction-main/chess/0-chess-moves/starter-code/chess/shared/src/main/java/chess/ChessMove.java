package chess;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    public ChessPiece.PieceType type;
    public ChessPosition start;
    public ChessPosition end;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.type = promotionPiece;
        this.start = startPosition;
        this.end = endPosition;
    }


    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
       return start;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return end;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        if (type == ChessPiece.PieceType.PAWN) {
            return type;
        }
        else {
            return null;
        }
    }

}
