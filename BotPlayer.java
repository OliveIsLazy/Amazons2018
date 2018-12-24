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
        algorithms.put("minimax", () -> runMiniMax());
        algorithms.put("k_nearest", () -> runK_Nearest());
        algorithms.put("mcts", () -> runMCTS());
        algorithms.put("q_learning", () -> runPythonAlgorithm());
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
            this.brain = new RandomMover(this.pawns);
        return this.brain.findBestMove();
    }

    public String runMiniMax() {
        if (this.brain == null)
            this.brain = new Minimax(this.pawns);
        return this.brain.findBestMove();
    }

    public String runK_Nearest() {
        if (this.brain == null)
            this.brain = new KNearestNeighbour(this.pawns);
        return this.brain.findBestMove();
    }

    public String runMCTS() {
        if (this.brain == null)
            this.brain = new MonteCarloTreeSearch((this.color.equals(Color.BLACK) ? "Black" : "White"));
        return this.brain.findBestMove();
    }

    public String runPythonAlgorithm() {
        if (this.brain == null) {
            PythonAlgorithm brainThread = new PythonAlgorithm(this.algorithm, this.pawns);
            brainThread.run();
            this.brain = brainThread;
        }
        try {
            return this.brain.findBestMove();
        } catch (Exception e) {
            System.out.println(e.getCause());
            System.exit(2);
            return null;
        }
    }

}