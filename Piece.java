import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;;

public class Piece {

    Icon icon;
    Color color;
    Position position;
    ArrayList<Position> movesPool = new ArrayList<Position>();

    Piece(Color c) {
        color = c;
    }

    public final void setImage(ImageIcon image) {
        this.icon = image;
    }

    public void showPaths() {
        for (Position pos : this.movesPool)
            Game.chessBoard.tiles[pos.width][pos.height].changeColor(true);
    }

    /*
     * input: void Adds all the possible moves for this piece to movesPool return:
     * void
     */
    public void findPaths() {
        movesPool.clear();
        boolean top_flag, bottom_flag, right_flag, left_flag, bottom_right_flag, top_right_flag, bottom_left_flag,
                top_left_flag;
        top_flag = bottom_flag = right_flag = left_flag = bottom_right_flag = top_right_flag = bottom_left_flag = top_left_flag = false;
        for (int i = 1; i < Game.chessBoard.tiles.length; i++) {
            // vertical and horizontal paths
            top_flag = conditionalAdditionToMovesPool(this.position.width - i, this.position.height,
                    this.position.width - i >= 0, top_flag);
            bottom_flag = conditionalAdditionToMovesPool(this.position.width + i, this.position.height,
                    this.position.width + i < Game.chessBoard.tiles.length, bottom_flag);
            right_flag = conditionalAdditionToMovesPool(this.position.width, this.position.height + i,
                    this.position.height + i < Game.chessBoard.tiles[i].length, right_flag);
            left_flag = conditionalAdditionToMovesPool(this.position.width, this.position.height - i,
                    this.position.height - i >= 0, left_flag);
            // diagonal paths
            bottom_right_flag = conditionalAdditionToMovesPool(this.position.width + i, this.position.height + i,
                    (this.position.width + i < Game.chessBoard.tiles.length
                            && this.position.height + i < Game.chessBoard.tiles[i].length),
                    bottom_right_flag);
            top_right_flag = conditionalAdditionToMovesPool(this.position.width + i, this.position.height - i,
                    (this.position.width + i < Game.chessBoard.tiles.length && this.position.height - i >= 0),
                    top_right_flag);
            bottom_left_flag = conditionalAdditionToMovesPool(this.position.width - i, this.position.height + i,
                    (this.position.width - i >= 0 && this.position.height + i < Game.chessBoard.tiles[i].length),
                    bottom_left_flag);
            top_left_flag = conditionalAdditionToMovesPool(this.position.width - i, this.position.height - i,
                    (this.position.width - i >= 0 && this.position.height - i > 0), top_left_flag);
        }
    }

    /*
     * input: colum index of tile to check, row index of tile to check, boolean
     * check if indices exist, stopping flag Adds tile at [i_index, j_index] to
     * movesPool depending on whether index_check and flag are true and false
     * respectively return: boolean that represents whether to stop the path in flag
     * direction or not
     */
    boolean conditionalAdditionToMovesPool(int i_index, int j_index, boolean index_check, boolean flag) {
        if (index_check && !flag) {
            if (Game.chessBoard.tiles[i_index][j_index].hasPiece || Game.chessBoard.tiles[i_index][j_index].wasShot) {
                return true;
            } else {
                this.movesPool.add(Game.chessBoard.tiles[i_index][j_index].position);
            }
        }
        return flag;
    }

    // Setters
    public void setPosition(Position pos) {
        this.position = pos;
    }

}