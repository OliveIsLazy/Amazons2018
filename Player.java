import java.awt.*;

//Base class for HumanPlayer and BotPlayer
//Keeps track of basic information
//e.g. color, state, and currently selected tile

public abstract class Player {

    String state = "Moving";
    GameTile selectedTile = null;
    Color color;

    Player(Color c){
        color = c;
    }

}