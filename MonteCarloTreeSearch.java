import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;

public class MonteCarloTreeSearch extends Tree implements Algorithm {

    private static final int WIN_SCORE = 10;
    private int level = 3;

    public MonteCarloTreeSearch(String _player) {
        super(_player);
    }

    @Override
    public Piece pickPawn() {
        return null;
    }

    @Override
    public String findBestMove() {
        long start = System.currentTimeMillis();
        long end = start + 60 * (2 * (this.level - 1) + 1);

        this.root.state = new State(this.player);

        while (System.currentTimeMillis() < end) {
            // Phase 1 - Selection"
            Node promisingNode = selectPromisingNode(this.root);

            // Phase 2 - Expansion"
            if (promisingNode.state.board.checkForMovesLeft(this.player) == true)
                expandNode(promisingNode);

            // Phase 3 - Simulation"
            Node nodeToExplore = promisingNode;
            if (promisingNode.children.size() > 0)
                nodeToExplore = promisingNode.getRandomChildNode();
            String playoutResult = simulateRandomPlayout(nodeToExplore);

            // Phase 4 - Update"
            backPropogation(nodeToExplore, playoutResult);
        }

        this.traverse();// for testing purposes only
        Node winnerNode = this.root.getChildWithMaxScore();
        this.root = winnerNode;
        System.out.println("final board: " + winnerNode.state.board.toString());
        return winnerNode.state.board.toString();
    }

    private Node selectPromisingNode(Node rootNode) {
        Node node = rootNode;
        while (node.children.size() != 0)
            node = UCT.findBestNodeWithUCT(node);
        return node;
    }

    private void expandNode(Node node) {
        List<State> possibleStates = node.state.findAllDescendants();
        possibleStates.forEach(state -> {
            Node newNode = new Node(state);
            newNode.parent = node;
            node.children.add(newNode);
        });
    }

    private String simulateRandomPlayout(Node node) {
        return (Math.random() > 0.5) ? this.opponent : this.player;
        // TODO: Rewrite simulation of random playout
        // so it returns the winner of the simulation
    }

    private void backPropogation(Node nodeToExplore, String winner) {
        Node temp = nodeToExplore;
        while (temp != null) {
            temp.state.incrementVisit();
            if (temp.state.playing == winner)
                temp.state.addScore(WIN_SCORE);
            temp = temp.parent;
        }
    }
}