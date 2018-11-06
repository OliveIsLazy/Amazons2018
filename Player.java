import java.awt.*;
import java.util.ArrayList;

//Base class for HumanPlayer and BotPlayer
//Keeps track of basic information
//e.g. color, state, and currently selected tile

public abstract class Player {

    String state = "Moving";
    GameTile selectedTile = null;
    Color color;
    ArrayList<Piece> pawns = new ArrayList<Piece>();

    Player(Color c){
        color = c;
    }

    /*
    *input: void
    *   Checks if player currently playing has any moves remaining.
    *output: boolean did player win
    */
    public boolean checkWinningCondition(){
        for(Piece piece : pawns)
            if(piece.movesPool.isEmpty()) return true;
        return false;
    }

}