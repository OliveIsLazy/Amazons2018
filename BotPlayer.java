import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

public class BotPlayer extends Player{// implements Runnable{

    public Position move;
    public Position shot;
    public Piece pawn;
    private HashMap<String, Supplier<ArrayList<Position>>> algorithms = new HashMap<>();
    public String algorithm;
    private Random rand = new Random();
    private GameTile[][] board;

    BotPlayer(Color color, String algorithm, GameTile[][] board){
        super(color);
        this.algorithm = algorithm;
        this.board = board;
        algorithms.put("Random", () -> runRandom());
        algorithms.put("MiniMax", () -> runMiniMax());
        //Add conversion from GameTile array to int array
    }

    /*
    *input: void
    *   Method to be overriden to make new bots.
    *   Finds a random pawn from the players' collection
    *   and makes a random move
    *return: void
    */
    public void run(){
        ArrayList<Position> move_and_shot = algorithms.get(algorithm).get();
        if(move_and_shot.size() == 2){
            this.move = move_and_shot.get(0);
            this.shot = move_and_shot.get(1);
        } else if(move_and_shot.size() < 2){
            this.move = move_and_shot.get(0);
        } else {
            System.out.println("There's a bug here");
        }
    }

    public void runAgain(){
        ArrayList<Position> move_and_shot = algorithms.get(algorithm).get();
        this.shot = move_and_shot.get(0);
    }

    public ArrayList<Position> runRandom(){
        ArrayList<Position> returnValue = new ArrayList<Position>();
        this.pawn = this.pawns.get(rand.nextInt(4));
        returnValue.add(pawn.movesPool.get(rand.nextInt(pawn.movesPool.size())));
        return returnValue;
    }

    public ArrayList<Position> runMiniMax(){
        ArrayList<Position> returnValue = new ArrayList<Position>();
        MiniMax miniMax = new MiniMax(board, (color.equals(Color.BLACK)) ? "Black" : "White", 3);
        for(Piece piece : this.pawns){
            if(piece.position.equals(miniMax.bestMove[0]))
                this.pawn = piece;
        }
        returnValue.add(miniMax.bestMove[1]);
        returnValue.add(miniMax.bestMove[2]);
        return returnValue;
    }
    
}