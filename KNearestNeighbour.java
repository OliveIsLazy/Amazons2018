import java.util.ArrayList;
import java.util.Random;

public class KNearestNeighbour implements Algorithm {
    private static ArrayList<Piece> pawns;
    private static Board board;
    private static Piece selectedPawn;

    KNearestNeighbour(Board _board, ArrayList<Piece> _pawns) {
        pawns = _pawns;
        board = _board;
    }

    @Override
    public Piece pickPawn() {
        return null;
    }

    @Override
    public String findBestMove() {
        // choose a pawn
        selectedPawn = this.pickPawn();
        // calculate its move and shot
        /*
         * Get inspiration from RandomMover.java for this part if you need to act out a
         * move before choosing where to shoot
         */
        // return result
        return board.encode();
    }

}