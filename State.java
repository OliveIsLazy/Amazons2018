import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class State {
    public TestBoard board;
    public String playing;
    public int visitCount;
    public float winScore;

    public State(State state) {
        this.board = state.board; // potential source of error
        this.playing = Cycle.getOpponent(state.playing);
        this.visitCount = state.visitCount;
        this.winScore = state.winScore;
    }

    public State(TestBoard board) {
        this.board = new TestBoard(board.toString()); // potential source of error
        this.playing = "";
        this.visitCount = 0;
        this.winScore = 0;
    }

    public State(String _player) {
        this.board = new TestBoard(Game.chessBoard.encode());
        this.playing = _player;
        this.visitCount = 0;
        this.winScore = 0;
    }

    public List<State> findAllDescendants() {
        // Make sure to add the opponent string to the states being made
        List<State> possibleStates = new ArrayList<State>();
        // Get all possible moves from player

        HashMap<Position, ArrayList<Move>> availableMoves = this.board.getMoves(this.playing);
        // System.out.println(this.board);
        availableMoves.forEach((k, v) -> {
            v.forEach(m -> {
                m.shotsPool.forEach(s -> {
                    // System.out.println(k + "," + m.position + "," + s);
                    State newState = new State(this.board);
                    newState.playing = Cycle.getOpponent(this.playing);
                    newState.board.performMove(k, m.position, s);
                    // System.out.println(this.board == newState.board);
                    possibleStates.add(newState);
                });
            });
        });
        return possibleStates;
    }

    public void incrementVisit() {
        this.visitCount++;
    }

    public void addScore(double score) {
        if (this.winScore != Integer.MIN_VALUE)
            this.winScore += score;
    }
}