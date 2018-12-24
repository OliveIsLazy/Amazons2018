import java.util.ArrayList;
import java.util.HashMap;

public class TestBoard {

    private static final int BOARD_WIDTH = 10;
    private static final int NUM_PAWNS = 8;
    String[][] tiles = new String[BOARD_WIDTH][BOARD_WIDTH];
    HashMap<String, ArrayList<Position>> pieces = new HashMap<String, ArrayList<Position>>();
    HashMap<Position, ArrayList<Move>> moves = new HashMap<Position, ArrayList<Move>>();

    public TestBoard(String initialBoard) {
        /*
         * eebeeeeaeeeeeeeeeweebeeeeeeeeweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeewbeeeeeeeeaeeeeeeeeeeeebeeeewee
         * eebeeeeaeeeeeeeeeweebeeeeeeeeweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeewaeeeeeeeeaeeeeeeeeeeeebeeeewee
         *
         * Black piece becomes an Arrow after performing a test move
         */
        pieces.put("w", new ArrayList<Position>());
        pieces.put("b", new ArrayList<Position>());
        try {
            int index = 0;
            for (int row = 0; row < BOARD_WIDTH; row++) {
                for (int tile = 0; tile < BOARD_WIDTH; tile++) {
                    String character = initialBoard.substring(index, index + 1);
                    tiles[row][tile] = character;
                    if ("w".equals(tiles[row][tile]) || "b".equals(tiles[row][tile]))
                        pieces.get(character).add(new Position(row, tile));
                    index++;
                }
            }
            pieces.forEach((k, v) -> {
                assert v.size() == NUM_PAWNS / 2;
            });
        } catch (AssertionError e) {
            System.out.println(initialBoard);
            System.out.println("There was an error in the translation of the board.");
            System.exit(1);
        }
    }

    public boolean checkForMovesLeft(String player) {
        int moveCount = 0;
        moves.clear();
        for (Position p : pieces.get(player.toLowerCase().substring(0, 1))) {
            ArrayList<Move> movesLeft = Piece.findAllMoves(this.tiles, p, "Find Moves");
            moves.put(p, movesLeft);
            moveCount += movesLeft.size();
        }
        return moveCount > 0;
    }

    public void performMove(Position piece_startPosition, Position move, Position shot) {
        String piece = tiles[piece_startPosition.width][piece_startPosition.height];
        tiles[piece_startPosition.width][piece_startPosition.height] = "e";
        tiles[move.width][move.height] = piece;
        tiles[shot.width][shot.height] = "a";
    }

    public HashMap<Position, ArrayList<Move>> getMoves(String player) {
        return moves;
    }

    @Override
    public String toString() {
        String returnValue = "";
        for (String[] row : tiles)
            for (String tile : row)
                returnValue += tile;
        return returnValue;
    }

    public static void main(String[] args) {
        String board = "eebeeeeeeeeeeeeeeeeebeeeeeeeeweeeeweeeeeeeeeeeeeeeeeeeeeeeeeeeeeaeeeeebeeeeeeeeweeeeeeeeeeeebeeeewee";
        TestBoard simulator = new TestBoard(board);
        simulator.checkForMovesLeft("White");
    }

}