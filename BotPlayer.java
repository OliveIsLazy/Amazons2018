import java.awt.*;
import java.util.Random;

public class BotPlayer extends Player{// implements Runnable{

    public Position move;
    public Piece pawn;
    Random rand = new Random();

    BotPlayer(Color color){
        super(color);
    }

    /*
    *input: void
    *   Method to be overriden to make new bots.
    *   Finds a random pawn from the players' collection
    *   and makes a random move
    *return: void
    */
    public void run(){
        pawn = this.pawns.get(rand.nextInt(4));
        move = pawn.movesPool.get(rand.nextInt(pawn.movesPool.size()));
    }
    
    /*
    *input: void
    *   Called from Game.letBotMakeItsMove()
    *   Calls upon BotPlayer.run() and returns the result of it
    *return: void
    */
    public Position findMove(){
        run();
        return move;
    }
    
}