import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

//Base class for HumanPlayer and BotPlayer
//Keeps track of basic information
//e.g. color, state, and currently selected tile

public abstract class AbstractPlayer {

    String state = "Moving";
    GameTile selectedTile = null;
    Color color;
    ArrayList<Piece> pawns = new ArrayList<Piece>();
    int numPieces = 4;

    AbstractPlayer(Color playerColor) {
        color = playerColor;
        for (int i = 0; i < numPieces; i++)
            this.pawns.add(new Piece(playerColor));
    }

    public final void setImage(ImageIcon image) {
        for (Piece piece : pawns)
            piece.setImage(image);
    }

    /*
     * input: void Checks if player currently playing has any moves remaining.
     * output: boolean did player win
     */
    public boolean checkWinningCondition() {
        for (Piece piece : pawns)
            if (piece.movesPool.isEmpty())
                return true;
        return false;
    }

}