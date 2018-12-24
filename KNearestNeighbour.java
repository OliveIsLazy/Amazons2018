import java.util.ArrayList;
import java.util.Random;

public class KNearestNeighbour implements Algorithm {
    private static ArrayList<Piece> pawns;
    private static Piece selectedPawn;

    KNearestNeighbour(ArrayList<Piece> _pawns) {
        pawns = _pawns;
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
        return Game.chessBoard.encode();
    }

}