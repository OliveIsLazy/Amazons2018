import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;

import java.net.URL;
import javax.imageio.ImageIO;

//Class used to represent a tile on the chess board
public class GameTile extends JButton {

    public boolean hasPiece = false;
    public boolean inPath = false;
    public boolean wasShot = false;
    private ImageIcon defaultIcon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
    public Piece piece;
    private Color defaultColor;
    private Game parentGame;
    public Position position;

    GameTile(Color color, int j, int i, Game game) {
        super();
        this.setMargin(new Insets(0, 0, 0, 0)); // sets icon to have no margin
        this.setIcon(defaultIcon);
        this.setBackground(color);
        // this.setOpaque(true);
        // this.setBorderPainted(false);
        defaultColor = color;
        parentGame = game;
        position = new Position(j, i);
        this.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifySelection();
            }
        });
    }

    /*
     * input: void Notifies the parentGame that a tile was clicked on / selected
     * return: void
     */
    public void notifySelection() {
        parentGame.selectTile(this);
    }

    /*
     * input: boolean flag showing whether tile is in a path or not Changes color of
     * the tile if its in the path return: void
     */
    public void changeColor(boolean setPath) {
        inPath = setPath;
        this.setBackground(Color.blue);
    }

    /*
     * input: void Resets color to default color return: void
     */
    public void setToDefaultColor() {
        inPath = false;
        if (!this.wasShot)
            this.setBackground(this.defaultColor);
    }

    /*
     * input: void Empties a tile and sets it to default color return: void
     */
    public void empty() {
        this.setIcon(defaultIcon);
        this.setBackground(this.defaultColor);
        inPath = false;
        hasPiece = false;
        wasShot = false;
    }

    /*
     * input: Piece to show up on the tile Sets piece to be present of this tile
     * return: void
     */
    public void setPiece(Piece piece) {
        hasPiece = true;
        this.piece = piece;
        this.setIcon(piece.icon);
        piece.setPosition(this.position);
    }

    /*
     * input: void Empties a tile and returns the removed piece return: Piece that
     * was removed
     */
    public Piece removePiece() {
        this.setIcon(defaultIcon);
        hasPiece = false;
        return this.piece;
    }

    /*
     * input: void Marks a tile as having been shot return: void
     */
    public void shoot() {
        this.wasShot = true;
        this.setBackground(Color.red);
    }
}