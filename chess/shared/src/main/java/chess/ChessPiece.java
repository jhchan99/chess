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


        if(type == PieceType.KNIGHT) {
            return knightMoves(board, myPosition);
        }

        if(type == PieceType.ROOK) {
            return rookMoves(board, myPosition);
        }

        if(type == PieceType.QUEEN){
            return queenMoves(board, myPosition);
        }

        if (type == PieceType.PAWN) {
            return pawnMoves(board, myPosition);
        }

        return null;


    }




    // moves for the king class
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> kingMoves = new HashSet<>();
       int row = myPosition.getRow();
       int column = myPosition.getColumn();

        if(row+1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
       }

        if(row-1 > -1) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn());
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(column+1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(column-1 > -1) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row-1 > -1 && column+1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row+1 < 8 && column-1 > -1) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row+1 < 8 && column+1 < 8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row-1 > -1 && column-1 > -1) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
            if(board.getPiece(newPosition) == null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                kingMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
       return kingMoves;
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> pawnMoves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // up once - not enemy not friend not blocked
        // up twice
        // attack
        // promote

//        if(row == 2 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
//            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
//            ChessPosition blockedDouble = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
//            if(board.getPiece(newPosition)==null){
//                pawnMoves.add(new ChessMove(myPosition, newPosition, null));
//            } else if(blockedDouble.getRow()==0){
//               pawnMoves.add(new ChessMove(myPosition, blockedDouble, null));
//            }
//        }

        if(row == 2 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
            ChessPosition blockedFront = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn());
            if(board.getPiece(newPosition)==null && board.getPiece(blockedFront) == null){
                pawnMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        return pawnMoves;

    }

//    public void addPawnMovesInDirection(Collection<ChessMove> pawnMoves, ChessBoard board, ChessPosition myPosition, int startRow, int startCol, int rowDelta, int colDelta) {
//        int row = startRow;
//        int col = startCol;
//
//        row += rowDelta;
//        col += colDelta;
//
//        if (row < 1 || row > 8 || col < 1 || col > 8) {
//            return;
//        }
//
//        ChessPosition newPosition = new ChessPosition(row, col);
//        ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
//
//        if (myPosition.getRow() == 2) {
//
//        }
//}


    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> rookMoves = new HashSet<>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();

        addRookMovesInDirection(rookMoves, board, myPosition, startRow, startCol, 1,0);
        addRookMovesInDirection(rookMoves, board, myPosition, startRow, startCol, 0,1);
        addRookMovesInDirection(rookMoves, board, myPosition, startRow, startCol, -1,0);
        addRookMovesInDirection(rookMoves, board, myPosition, startRow, startCol, 0,-1);

        return rookMoves;

        }

    public void addRookMovesInDirection(Collection<ChessMove> rookMoves, ChessBoard board, ChessPosition myPosition, int startRow, int startCol, int rowDelta, int colDelta) {
        int row = startRow;
        int col = startCol;

        while (row >= 1 && row < 8 && col >= 1 && col < 8) {
            row += rowDelta;
            col += colDelta;

            if (row < 1 || row > 8 || col < 1 || col > 8) {
                break; // Ensure the position is within the board
            }

            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
            if (pieceAtNewPosition == null) {
                rookMoves.add(new ChessMove(myPosition, newPosition, null));
            } else {
                if (pieceAtNewPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        rookMoves.add(new ChessMove(myPosition, newPosition, null));
                    }
                break;
            }

        }
    }
    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> queenMoves = new HashSet<>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();

        addQueenMovesInDirection(queenMoves, board, myPosition, startRow, startCol, 1, 1);
        addQueenMovesInDirection(queenMoves, board, myPosition, startRow, startCol, -1, -1);
        addQueenMovesInDirection(queenMoves, board, myPosition, startRow, startCol, -1, 1);
        addQueenMovesInDirection(queenMoves, board, myPosition, startRow, startCol, 1, -1);
        addQueenMovesInDirection(queenMoves, board, myPosition, startRow, startCol, 1,0);
        addQueenMovesInDirection(queenMoves, board, myPosition, startRow, startCol, 0,1);
        addQueenMovesInDirection(queenMoves, board, myPosition, startRow, startCol, -1,0);
        addQueenMovesInDirection(queenMoves, board, myPosition, startRow, startCol, 0,-1);


        return queenMoves;
    }

    private void addQueenMovesInDirection(Collection<ChessMove> queenMoves, ChessBoard board, ChessPosition myPosition, int startRow, int startCol, int rowDelta, int colDelta) {
        int row = startRow;
        int col = startCol;

        while (row >= 1 && row < 8 && col >= 1 && col < 8) {
            row += rowDelta;
            col += colDelta;

            if (row < 1 || row > 8 || col < 1 || col > 8) {
                break; // Ensure the position is within the board
            }

            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
            if (pieceAtNewPosition == null) {
                queenMoves.add(new ChessMove(myPosition, newPosition, null));
            } else {
                if (pieceAtNewPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    queenMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                break; // Stop if there's a piece (either capture it or blocked by it)
            }
        }
    }

    // moves for the bishop class
    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = new HashSet<>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();

        addBishopMovesInDirection(bishopMoves, board, myPosition, startRow, startCol, 1, 1);
        addBishopMovesInDirection(bishopMoves, board, myPosition, startRow, startCol, -1, -1);
        addBishopMovesInDirection(bishopMoves, board, myPosition, startRow, startCol, -1, 1);
        addBishopMovesInDirection(bishopMoves, board, myPosition, startRow, startCol, 1, -1);

        return bishopMoves;
    }

    private void addBishopMovesInDirection(Collection<ChessMove> bishopMoves, ChessBoard board, ChessPosition myPosition, int startRow, int startCol, int rowDelta, int colDelta) {
        int row = startRow;
        int col = startCol;

        while (row >= 1 && row < 8 && col >= 1 && col < 8) {
            row += rowDelta;
            col += colDelta;

            if (row < 1 || row > 8 || col < 1 || col > 8) {
                break; // Ensure the position is within the board
            }

            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
            if (pieceAtNewPosition == null) {
                bishopMoves.add(new ChessMove(myPosition, newPosition, null));
            } else {
                if (pieceAtNewPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    bishopMoves.add(new ChessMove(myPosition, newPosition, null));
                }
                break; // Stop if there's a piece (either capture it or blocked by it)
            }
        }
    }


    //moves for the knight class
    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> knightMoves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // codes for 1 row up 2 col each side
        if(row+1<8 && col+2 <=8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+2);
            if(board.getPiece(newPosition)==null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                knightMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row+1<8 && col-2 >0) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-2);
            if(board.getPiece(newPosition)==null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                knightMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        //----------------------------------

        // 2 rows up 1 col each side
        if(row+2<8 && col-1 >0) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()-1);
            if(board.getPiece(newPosition)==null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                knightMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row+2<8 && col+1 <8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn()+1);
            if(board.getPiece(newPosition)==null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                knightMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        //---------------------------------

        // 1 row down 2 col each side
        if(row-1>0 && col+2 <8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+2);
            if(board.getPiece(newPosition)==null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                knightMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row-1>0 && col-2 >0) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-2);
            if(board.getPiece(newPosition)==null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                knightMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        //--------------------------------

        //2 rows down 1 col each side
        if(row-2>0 && col+1<8) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()+1);
            if(board.getPiece(newPosition)==null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                knightMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        if(row-2>0 && col-1 >0) {
            ChessPosition newPosition = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn()-1);
            if(board.getPiece(newPosition)==null || board.getPiece(newPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                knightMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }

        return knightMoves;
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
