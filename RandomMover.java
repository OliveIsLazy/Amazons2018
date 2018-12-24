import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.Random;

public class RandomMover implements Algorithm {

    private Random rand;
    private static ArrayList<Piece> pawns;
    private static Piece selectedPawn;

    RandomMover(ArrayList<Piece> _pawns) {
        rand = new Random();
        pawns = _pawns;
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
        Move move = selectedPawn.movesPool.get(rand.nextInt(selectedPawn.movesPool.size()));
        perform(move);
        selectedPawn.findPaths();
        move.findAllShots(Game.chessBoard.tiles, selectedPawn.position);
        Position shot = move.shotsPool.get(rand.nextInt(move.shotsPool.size()));
        shoot(shot);
        // return result
        return Game.chessBoard.encode();
    }

    private void perform(Move move) {
        selectedPawn = Game.chessBoard.tiles[selectedPawn.position.width][selectedPawn.position.height].removePiece();
        GameTile tile = Game.chessBoard.tiles[move.position.width][move.position.height];
        tile.setPiece(selectedPawn);
    }

    private void shoot(Position shot) {
        GameTile tile = Game.chessBoard.tiles[shot.width][shot.height];
        tile.shoot();
    }
}