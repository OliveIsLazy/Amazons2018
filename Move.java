import java.util.ArrayList;

public class Move {
    Position position;
    ArrayList<Position> shotsPool = new ArrayList<Position>();

    Move(Position pos) {
        this.position = pos;
    }

    @Override
    public boolean equals(Object other) {
        Move o = (Move) other;
        return this.position.equals(o.position);
    }

    public void findAllShots(Object[][] gameBoard, Position piece_startPosition) {
        this.shotsPool.clear();
        this.shotsPool.add(piece_startPosition);
        ArrayList<Move> shots = Piece.findAllMoves(gameBoard, this.position, "Find Shots");
        shots.forEach(m -> {
            this.shotsPool.add(m.position);
        });
    }

    public void setShot(Position shot) {
        this.shotsPool.add(shot);
    }
}