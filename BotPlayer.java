import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Supplier;

public class BotPlayer extends AbstractPlayer {

    private HashMap<String, Supplier<String>> algorithms = new HashMap<>();
    public String algorithm;
    private Algorithm brain;
    private Board board;

    BotPlayer(Color color, String algorithm, Board board) {
        super(color);
        this.algorithm = algorithm;
        this.board = board;
        algorithms.put("random", () -> runRandom());
        algorithms.put("miniMax", () -> runMiniMax());
        algorithms.put("k_nearest", () -> runK_Nearest());
    }

    /*
     * input: void Method to be overriden to make new bots. Finds a random pawn from
     * the players' collection and makes a random move return: void
     */
    public boolean run() {
        // run the right algorithm
        String newBoard = algorithms.get(algorithm).get();
        // display the new board
        return this.board.decode(newBoard);
    }

    public String runRandom() {
        if (this.brain == null)
            this.brain = new RandomMover(this.board, this.pawns);
        return this.brain.findBestMove();
    }

    public String runMiniMax() {
        if (this.brain == null)
            this.brain = new Minimax(this.board, this.pawns);
        return this.brain.findBestMove();
    }

    public String runK_Nearest() {
        if (this.brain == null)
            this.brain = new KNearestNeighbour(this.board, this.pawns);
        return this.brain.findBestMove();
    }

}