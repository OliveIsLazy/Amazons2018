public abstract class Tree {
    public Node root;
    String player;
    String opponent;

    Tree(String _player) {
        this.root = new Node(_player);
        this.player = _player;
        this.opponent = Cycle.getOpponent(_player);
    }

    public void traverse() {
        // use this to test if tree is empty
    }
}
