import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.Random;

public class RandomMover implements Algorithm {

    private Random rand;
    private static ArrayList<Piece> pawns;
    private static Board board;
    private static Piece selectedPawn;

    RandomMover(Board _board, ArrayList<Piece> _pawns) {
        rand = new Random();
        pawns = _pawns;
        board = _board;
    }

    @Override
    public Piece pickPawn() {
        return pawns.get(rand.nextInt(4));
    }

    @Override
    public String findBestMove() {
        // choose a pawn
        selectedPawn = this.pickPawn();
        // calculate its move and shot
        Position move = selectedPawn.movesPool.get(rand.nextInt(selectedPawn.movesPool.size()));
        Move(move);
        selectedPawn.findPaths();
        Position shot = selectedPawn.movesPool.get(rand.nextInt(selectedPawn.movesPool.size()));
        Shoot(shot);
        // return result
        return board.encode();
    }

    private void Move(Position move) {
        selectedPawn = board.tiles[selectedPawn.position.width][selectedPawn.position.height].removePiece();
        GameTile tile = board.tiles[move.width][move.height];
        tile.setPiece(selectedPawn);
    }

    private void Shoot(Position shot) {
        GameTile tile = board.tiles[shot.width][shot.height];
        tile.shoot();
    }
}