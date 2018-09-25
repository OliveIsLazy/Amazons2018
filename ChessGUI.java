import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;

import java.net.URL;
import javax.imageio.ImageIO;

public class ChessGUI {

    public static final String COLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int QUEEN = 0;
    public static final int BLACK = 0, WHITE = 1, IMAGE_SIZE = 64;
    public static int BOARDSIZE;
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private GameTile[][] chessBoardSquares;
    private Image[][] chessPieceImages = new Image[2][6];
    private JPanel chessBoard;
    private JLabel message;
    
    ChessGUI(GameTile[][] b, JLabel m) {
        BOARDSIZE = b.length;

        chessBoardSquares = b; //Game tiles created by the game
        message = m; //Message showing whos move it is etc
        initializeGui();
    }

    public final void initializeGui() {
        // create the images for the chess pieces
        createImages();

        // set up the main GUI
        setUpGUI();

        // make the chess board from GameTile
        makeChessBoard();
    }

    public final JComponent getGui() {
        return gui;
    }

    private final void createImages() {
        try {
            URL url = new URL("http://i.stack.imgur.com/memI0.png");
            BufferedImage bi = ImageIO.read(url);
            for (int ii = 0; ii < 2; ii++) {
                for (int jj = 0; jj < 6; jj++) {
                    chessPieceImages[ii][jj] = bi.getSubimage(
                            jj * IMAGE_SIZE, ii * IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Game images couldn't be found.");
            System.exit(1);
        }
    }

    private final void setUpGUI(){
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        Action newGameAction = new AbstractAction("New") {

            @Override
            public void actionPerformed(ActionEvent e) {
                setupNewGame();
            }
            
        };
        tools.add(newGameAction);
        tools.add(new JButton("Save")); // TO.DO - add functionality!
        tools.add(new JButton("Restore")); // TO.DO - add functionality!
        tools.addSeparator();
        tools.add(new JButton("Resign")); // TO.DO - add functionality!
        tools.addSeparator();
        tools.add(message);

    }

    private final void makeChessBoard(){
        chessBoard = new JPanel(new GridLayout(BOARDSIZE + 2, 0)) { // + 2 because there's 2 columns and 2 rows on each side of the board

            /**
             * Override the preferred size to return the largest it can, in
             * a square shape.  Must (must, must) be added to a GridBagLayout
             * as the only component (it uses the parent as a guide to size)
             * with no GridBagConstaint (so it is centered).
             */
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize = null;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension(
                            (int)d.getWidth(),(int)d.getHeight());
                } else if (c!=null &&
                        c.getWidth()>d.getWidth() &&
                        c.getHeight()>d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                // the smaller of the two sizes
                int s = (w>h ? h : w);
                return new Dimension(s,s);
            }
        };
        // Create board contour/border
        chessBoard.setBorder(
            new CompoundBorder(
                new EmptyBorder(8,8,8,8),
                new LineBorder(Color.BLACK)
            )
        );

        // Set the BG to be ochre
        Color ochre = new Color(204,119,34);
        chessBoard.setBackground(ochre);
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(ochre);
        boardConstrain.add(chessBoard);
        gui.add(boardConstrain);

        /*
         * fill the chess board
         */
        // fill the top row
        chessBoard.add(new JLabel(""));
        for (int ii = 0; ii < BOARDSIZE; ii++)
            chessBoard.add(new JLabel(COLS.substring(ii, ii + 1), SwingConstants.CENTER)); // Horiztontal legend A to J on 10x10
        chessBoard.add(new JLabel(""));

        // fill in the chess board (first and last index of the row is the vertical legend of the board)
        for (int ii = 0; ii < BOARDSIZE; ii++) {
            for (int jj = 0; jj < BOARDSIZE; jj++) {
                if(jj==0) {
                    chessBoard.add(new JLabel("" + (BOARDSIZE+1-(ii + 1)), SwingConstants.CENTER));
                    chessBoard.add(chessBoardSquares[jj][ii]);
                }
                else if(jj==BOARDSIZE-1){
                    chessBoard.add(chessBoardSquares[jj][ii]);
                    chessBoard.add(new JLabel("" + (BOARDSIZE+1-(ii + 1)), SwingConstants.CENTER));
                }
                else {
                    chessBoard.add(chessBoardSquares[jj][ii]);
                }
            }
        }

        // fill the bottom row
        chessBoard.add(new JLabel(""));
        for (int ii = 0; ii < BOARDSIZE; ii++)
            chessBoard.add(new JLabel(COLS.substring(ii, ii + 1), SwingConstants.CENTER)); // Horiztontal legend A to J on 10x10
        chessBoard.add(new JLabel(""));
    }

    /**
     * Initializes the icons of the initial chess board piece places
     */
    private final void setupNewGame() {
        try{
            for(GameTile[] row: chessBoardSquares)
                for(GameTile tile: row)
                    assert(tile.hasPiece == false);
        }catch(AssertionError e){
            for(GameTile[] row: chessBoardSquares)
                for(GameTile tile: row)
                    tile.empty();
        }
        message.setText("White's move!");
        // set up the black pieces
        chessBoardSquares[2][0].setPiece(new Piece(new ImageIcon(chessPieceImages[BLACK][QUEEN]), Color.BLACK));
        chessBoardSquares[BOARDSIZE-3][0].setPiece(new Piece(new ImageIcon(chessPieceImages[BLACK][QUEEN]), Color.BLACK));
        chessBoardSquares[0][2].setPiece(new Piece(new ImageIcon(chessPieceImages[BLACK][QUEEN]), Color.BLACK));
        chessBoardSquares[BOARDSIZE-1][2].setPiece(new Piece(new ImageIcon(chessPieceImages[BLACK][QUEEN]), Color.BLACK));
        
        chessBoardSquares[0][BOARDSIZE-3].setPiece(new Piece(new ImageIcon(chessPieceImages[WHITE][QUEEN]), Color.WHITE));
        chessBoardSquares[BOARDSIZE-1][BOARDSIZE-3].setPiece(new Piece(new ImageIcon(chessPieceImages[WHITE][QUEEN]), Color.WHITE));
        chessBoardSquares[2][BOARDSIZE-1].setPiece(new Piece(new ImageIcon(chessPieceImages[WHITE][QUEEN]), Color.WHITE));
        chessBoardSquares[BOARDSIZE-3][BOARDSIZE-1].setPiece(new Piece(new ImageIcon(chessPieceImages[WHITE][QUEEN]), Color.WHITE));
    }
}