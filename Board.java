import java.awt.Color;

import javafx.scene.layout.Border;

public class Board {
    public GameTile[][] tiles;
    public int size;
    public boolean enabled = true;

    /*
     * input: number of columns, number of rows Builds a args[2] x args[3] size
     * chess board using GameTiles return: void
     */
    Board(Integer width, Integer height, Game game) {
        size = width.intValue();
        this.tiles = new GameTile[width.intValue()][height.intValue()];
        for (int ii = 0; ii < this.tiles.length; ii++) {
            for (int jj = 0; jj < this.tiles[ii].length; jj++) {
                if ((jj % 2 == 1 && ii % 2 == 1) || (jj % 2 == 0 && ii % 2 == 0))
                    this.tiles[jj][ii] = new GameTile(Color.WHITE, jj, ii, game);
                else
                    this.tiles[jj][ii] = new GameTile(Color.BLACK, jj, ii, game);
            }
        }
    }

    Board(String board) {
        this.decode(board);
    }

    public String encode() {
        String board = "";
        for (GameTile[] row : tiles) {
            for (GameTile tile : row) {
                if (tile.wasShot)
                    board += "a";
                else if (tile.hasPiece)
                    board += "p";
                else
                    board += "e";
            }
        }
        System.out.println(board);
        return board;
    }

    private void decode(String board) {
        System.out.println(board);
        // TODO: implement loading of a game
        // this.size = this.tiles = new GameTile[size][size];
    }

    /*
     * input: void Resets board to not show any paths return: void
     */
    public void clear() {
        for (GameTile[] row : tiles)
            for (GameTile tile : row)
                tile.setToDefaultColor();
    }

    public void empty() {
        for (GameTile[] row : tiles)
            for (GameTile tile : row)
                tile.empty();
    }

    public void assertEmptiness() {
        for (GameTile[] row : tiles)
            for (GameTile tile : row)
                assert (tile.hasPiece == false);
    }

    public void disable() {
        // disable the whole board
        enabled = false;
        for (GameTile[] row : tiles)
            for (GameTile tile : row)
                tile.setEnabled(false);
    }

    public void enable() {
        enabled = true;
        // disable the whole board
        for (GameTile[] row : tiles)
            for (GameTile tile : row)
                tile.setEnabled(true);
    }

}