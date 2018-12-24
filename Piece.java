import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Supplier;

public class Piece {

    Icon icon;
    Color color;
    Position position;
    ArrayList<Move> movesPool = new ArrayList<Move>();

    Piece(Color c) {
        color = c;
    }

    public final void setImage(ImageIcon image) {
        this.icon = image;
    }

    public void showPaths() {
        for (Move move : this.movesPool)
            Game.chessBoard.tiles[move.position.width][move.position.height].changeColor(true);
    }

    /*
     * input: void Adds all the possible moves for this piece to movesPool return:
     * void
     */
    public void findPaths() {
        movesPool = findAllMoves(Game.chessBoard.tiles, this.position, "Find Moves");
    }

    public static ArrayList<Move> findAllMoves(Object[][] gameBoard, Position piece, String intention) {
        ArrayList<Move> moveList = new ArrayList<Move>();
        boolean[] flags = new boolean[8];
        for (int i = 1; i < gameBoard.length; i++) {
            for (int index = 0; index < flags.length; index++) {
            if(!check_flag(index, i, gameBoard, piece) || flags[index])
                continue;
            Position potentialMove = get_position(index, i, piece);
            if(GameTile[][].class.equals(gameBoard.getClass()))
                flags[index] = addMove((GameTile[][]) gameBoard, piece, potentialMove, moveList, intention);
            else
                flags[index] = addMove((String[][]) gameBoard, piece, potentialMove, moveList, intention);
            }
        }
        return moveList;
    }

    public static boolean check_flag(int flag_index, int i, Object[][] gameBoard, Position piece){
        switch(flag_index){
            case 0: return piece.width - i >= 0;
            case 1: return piece.width + i < gameBoard.length;
            case 2: return piece.height + i < gameBoard[i].length;
            case 3: return piece.height - i >= 0;
            case 4: return piece.width + i < gameBoard.length && piece.height + i < gameBoard[i].length;
            case 5: return piece.width + i < gameBoard.length && piece.height - i >= 0;
            case 6: return piece.width - i >= 0 && piece.height + i < gameBoard[i].length;
            case 7: return piece.width - i >= 0 && piece.height - i > 0;
        }
        return false;
    }

    public static Position get_position(int flag_index, int i, Position piece) {
        switch(flag_index){
            case 0: return new Position(piece.width - i, piece.height);
            case 1: return new Position(piece.width + i, piece.height);
            case 2: return new Position(piece.width, piece.height + i);
            case 3: return new Position(piece.width, piece.height - i);
            case 4: return new Position(piece.width + i, piece.height + i);
            case 5: return new Position(piece.width + i, piece.height - i);
            case 6: return new Position(piece.width - i, piece.height + i);
            case 7: return new Position(piece.width - i, piece.height - i);
        }
        return null;
    }

    public static boolean addMove(GameTile[][] gameBoard, Position piece, Position potential, ArrayList<Move> list, String intention){
        GameTile tile = gameBoard[potential.width][potential.height];
        if (tile.hasPiece || tile.wasShot) {
            return true;
        } else {
            Move move = new Move(tile.position);
            if("Find Moves".equals(intention)){
                move.findAllShots(gameBoard, piece);
                if (!move.shotsPool.isEmpty())
                    list.add(move);
            } else {
                list.add(move);
            }
            return false;
        }
    }

    public static boolean addMove(String[][] gameBoard, Position piece, Position potential, ArrayList<Move> list, String intention){
        String tile = gameBoard[potential.width][potential.height];
        if ("w".equals(tile) || "b".equals(tile) || "a".equals(tile)) {
            return true;
        } else {
            Move move = new Move(potential);
            if("Find Moves".equals(intention)){
                move.findAllShots(gameBoard, piece);
                if (!move.shotsPool.isEmpty())
                    list.add(move);
            } else {
                list.add(move);
            }
            return false;
        }
    }

    // Setters
    public void setPosition(Position pos) {
        this.position = pos;
    }

}