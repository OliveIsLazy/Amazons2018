import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ChessGUI {

    public static final String COLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int QUEEN = 0;
    public static final int BLACK = 0, WHITE = 1, IMAGE_SIZE = 64;
    public static int BOARDSIZE;
    public static HashMap<String, Image> chessPieceImages = new HashMap<String, Image>();

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private Board board;
    private JPanel chessBoard;
    private JLabel message;
    private Game parentGame;

    ChessGUI(Game game) {
        this.parentGame = game;
        BOARDSIZE = game.getBoard().tiles.length;
        board = game.getBoard(); // Game tiles created by the game
        message = game.getMessage(); // Message showing whos move it is etc
        initializeGui();
    }

    public final void initializeGui() {
        // create the images for the chess pieces
        createImages();
        setPieceImages();

        // set up the main GUI
        setUpGUI();

        // make the chess board from GameTile
        makeChessBoard();
    }

    public final JComponent getGui() {
        return gui;
    }

    public final HashMap<String, Image> getImages() {
        return chessPieceImages;
    }

    private final void setPieceImages() {
        parentGame.getPlayer("Black").setImage(new ImageIcon(chessPieceImages.get("Black")));
        parentGame.getPlayer("White").setImage(new ImageIcon(chessPieceImages.get("White")));
    }

    private final void createImages() {
        try {
            URL url = new URL("http://i.stack.imgur.com/memI0.png");
            BufferedImage bi = ImageIO.read(url);
            chessPieceImages.put("Black", bi.getSubimage(0, 0, IMAGE_SIZE, IMAGE_SIZE));
            chessPieceImages.put("White", bi.getSubimage(0, IMAGE_SIZE, IMAGE_SIZE, IMAGE_SIZE));

        } catch (Exception e) {
            System.out.println("Game images couldn't be found.");
            System.exit(1);
        }
    }

    private final void setUpGUI() {
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        Action newGameAction = new AbstractAction("New") {
            private static final long serialVersionUID = 42L;

            @Override
            public void actionPerformed(ActionEvent e) {
                setupNewGame();
            }

        };
        tools.add(newGameAction);
        tools.addSeparator();
        Action newSaveAction = new AbstractAction("Save") {
            private static final long serialVersionUID = 43L;

            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }

        };
        tools.add(newSaveAction);
        Action newLoadAction = new AbstractAction("Load Game") {
            private static final long serialVersionUID = 44L;

            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        };
        tools.add(newLoadAction);
        tools.addSeparator();
        Action newResignAction = new AbstractAction("Resign") {
            private static final long serialVersionUID = 45L;

            @Override
            public void actionPerformed(ActionEvent e) {
                parentGame.endGame();
            }
        };
        tools.add(newResignAction);
        tools.addSeparator();
        Action newBackAction = new AbstractAction("Back") {
            private static final long serialVersionUID = 46L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!parentGame.moveBack())
                    JOptionPane.showMessageDialog(gui, "Cannot go back further.");
            }
        };
        tools.add(newBackAction);
        tools.addSeparator();
        tools.add(message);

    }

    private final void makeChessBoard() {
        chessBoard = new JPanel(new GridLayout(BOARDSIZE + 2, 0)) { // + 2 because there's 2 columns and 2 rows on each
                                                                    // side of the board
            private static final long serialVersionUID = 47L;

            /**
             * Override the preferred size to return the largest it can, in a square shape.
             * Must (must, must) be added to a GridBagLayout as the only component (it uses
             * the parent as a guide to size) with no GridBagConstaint (so it is centered).
             */
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize = null;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension((int) d.getWidth(), (int) d.getHeight());
                } else if (c != null && c.getWidth() > d.getWidth() && c.getHeight() > d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                // the smaller of the two sizes
                int s = (w > h ? h : w);
                return new Dimension(s, s);
            }
        };
        // Create board contour/border
        chessBoard.setBorder(new CompoundBorder(new EmptyBorder(8, 8, 8, 8), new LineBorder(Color.BLACK)));

        // Set the BG to be ochre
        Color ochre = new Color(204, 119, 34);
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
            chessBoard.add(new JLabel(COLS.substring(ii, ii + 1), SwingConstants.CENTER)); // Horiztontal legend A to J
                                                                                           // on 10x10
        chessBoard.add(new JLabel(""));

        // fill in the chess board (first and last index of the row is the vertical
        // legend of the board)
        for (int ii = 0; ii < BOARDSIZE; ii++) {
            for (int jj = 0; jj < BOARDSIZE; jj++) {
                if (jj == 0) {
                    chessBoard.add(new JLabel("" + (BOARDSIZE + 1 - (ii + 1)), SwingConstants.CENTER));
                    chessBoard.add(board.tiles[jj][ii]);
                } else if (jj == BOARDSIZE - 1) {
                    chessBoard.add(board.tiles[jj][ii]);
                    chessBoard.add(new JLabel("" + (BOARDSIZE + 1 - (ii + 1)), SwingConstants.CENTER));
                } else {
                    chessBoard.add(board.tiles[jj][ii]);
                }
            }
        }

        // fill the bottom row
        chessBoard.add(new JLabel(""));
        for (int ii = 0; ii < BOARDSIZE; ii++)
            chessBoard.add(new JLabel(COLS.substring(ii, ii + 1), SwingConstants.CENTER)); // Horiztontal legend A to J
                                                                                           // on 10x10
        chessBoard.add(new JLabel(""));
    }

    /**
     * Initializes the icons of the initial chess board piece places
     */
    public final void setupNewGame() {
        try {
            board.assertEmptiness();
        } catch (AssertionError e) {
            board.empty();
        }
        this.parentGame.startNewGame();
    }

    /**
     * Pops up a FileChooser and saves game to the selected file
     */
    private final void saveGame() {
        JFileChooser fc = new JFileChooser();
        CustomFileFilter ff = new CustomFileFilter();
        fc.setFileFilter(ff);
        int returnVal = fc.showDialog(this.gui, "Save");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fc.getSelectedFile();
                if (!file.getName().endsWith(".amazons"))
                    file = new File(file + ".amazons");
                // Write board content etc to the file
                System.out.println("Opening: " + file.getName());
                FileWriter writer = new FileWriter(file);
                writer.write(board.encode());
                writer.flush();
                writer.close();
                // popup saying it was saved successfully
                JOptionPane.showMessageDialog(this.gui, "Game was saved successfully.");
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * Pops up a FileChooser and loads game from the selected file
     */
    private final void loadGame() {
        JFileChooser fc = new JFileChooser();
        CustomFileFilter ff = new CustomFileFilter();
        fc.setFileFilter(ff);
        int returnVal = fc.showDialog(this.gui, "Load");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                assert (file.exists() && file.getName().endsWith(".amazons"));
                // Write board content etc to the file
                FileReader reader = new FileReader(file);
                int c;
                String boardString = "";
                while ((c = reader.read()) != -1)
                    boardString += (char) c;
                reader.close();
                // transform string into Board class
                assert (parentGame.getBoard().decode(boardString));
                // popup saying it was saved successfully
                JOptionPane.showMessageDialog(this.gui, "Game was loaded successfully.");
            } catch (IOException e) {
                System.out.println(e.toString());
            } catch (AssertionError e) {
                JOptionPane.showMessageDialog(this.gui,
                        file.getName() + " is not a valid game file.\nPlease specify a new file name.");
            }
        }
    }
}