import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Math;

public class Node {
    public State state;
    public Node parent;
    public List<Node> children;

    public Node(String player) {
        this.state = new State(player);
        children = new ArrayList<Node>();
    }

    public Node(State state) {
        this.state = state;
        children = new ArrayList<Node>();
    }

    public Node(State state, Node parent, List<Node> children) {
        this.state = state;
        this.parent = parent;
        this.children = children;
    }

    public Node(Node node) {
        this.children = new ArrayList<Node>();
        this.state = new State(node.state);
        if (node.parent != null)
            this.parent = node.parent;
        for (Node child : node.children) {
            this.children.add(new Node(child));
        }
    }

    public Node getRandomChildNode() {
        int noOfPossibleMoves = this.children.size();
        int selectRandom = (int) (Math.random() * noOfPossibleMoves);
        return this.children.get(selectRandom);
    }

    public Node getChildWithMaxScore() {
        Node bestNode = null;
        double maxValue = Double.MIN_VALUE;
        for (Node child : this.children) {
            if (child.state.visitCount > maxValue) {
                bestNode = child;
                maxValue = child.state.visitCount;
            }
        }
        return bestNode;
    }
}