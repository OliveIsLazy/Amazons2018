import java.awt.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Stack;

public class Game {
    private HashMap<String, Player> players = new HashMap<String, Player>();
    private String playing = "White";
    private HashMap<String, Image> pieceImages;
    private Board chessBoard;
    private JLabel message;
    private Stack<> history;

    Game(String player1, String player2, String width, String height, String algorithm) {
        // Builds ChessGUI.chessBoardTiles array
        chessBoard = new Board(Integer.parseInt(width), Integer.parseInt(height), this);
        // Creates 2 players as requested by args
        Player white = player1.equals("bot") ? new BotPlayer(Color.WHITE, algorithm, chessBoard.tiles)
                : new HumanPlayer(Color.WHITE);
        Player black = player2.equals("bot") ? new BotPlayer(Color.BLACK, algorithm, chessBoard.tiles)
                : new HumanPlayer(Color.BLACK);
        players.put("White", white);
        players.put("Black", black);
        // ChessGUI.message
        message = new JLabel();
    }

    /*
     * input: GameTile user interacted with Applies necessary logic is applied to
     * tile. Called when user interacts with tile return: void
     */
    public void selectTile(GameTile tile) {

        if (players.get(playing) instanceof BotPlayer)
            return;
        if (players.get(playing).selectedTile == null) {
            if (tile.hasPiece && players.get(playing).color.equals(tile.piece.color)) {
                // Select the tile and show the possible moves
                chessBoard.clear();
                players.get(playing).selectedTile = tile;
                tile.changeColor(false);
                tile.piece.showPaths();
            } else {
                return;
            }
        } else {
            if (players.get(playing).selectedTile.equals(tile)) {
                players.get(playing).selectedTile = null;
                chessBoard.clear();
            } else if (tile.hasPiece && players.get(playing).color.equals(tile.piece.color)) {
                // Select the tile and show the possible moves
                chessBoard.clear();
                players.get(playing).selectedTile = tile;
                tile.changeColor(false);
                tile.piece.showPaths();
            } else if (players.get(playing).selectedTile.piece.movesPool.contains(tile.position)) {
                // Move piece to that tile
                switch (players.get(playing).state) {
                case "Moving":
                    movePieceTo(tile);
                    break;
                case "Shooting":
                    shootArrowTo(tile);
                    players.get(playing).selectedTile = null;
                    chessBoard.clear();
                    break;
                default:
                    System.out.println("There was an error");
                    return;
                }
            } else {
                chessBoard.clear();
                players.get(playing).selectedTile = null;
                return;
            }
        }
    }

    /*
     * input: GameTile user selected piece should move to Moves the currently
     * selected piece to newTile return: void
     */
    private void movePieceTo(GameTile newTile) {
        System.out.println("Moving to " + newTile.position);
        assert (players.get(playing).selectedTile != null);
        Piece piece = players.get(playing).selectedTile.removePiece();
        newTile.setPiece(piece);
        players.get(playing).state = "Shooting";
        players.get(playing).selectedTile = newTile;
        newTile.changeColor(false);
        findAllPaths();
        chessBoard.clear();
        piece.showPaths();
    }

    /*
     * input: GameTile to shoot at Makes shotTile a restricted area so player cannot
     * move there return: void
     */
    private void shootArrowTo(GameTile shotTile) {
        System.out.println("Shooting at " + shotTile.position);
        shotTile.shoot();
        players.get(playing).state = "Moving";
        findAllPaths();
        switchPlayer();
    }

    /*
     * input: void Switches playing variable and sets ChessGUI.message in
     * accordance. Used to switch player turns return: void
     */
    private void switchPlayer() {
        String next = playing;
        if (playing == "White")
            next = "Black";
        else if (playing == "Black")
            next = "White";
        message.setText(next + "\'s turn");
        if (players.get(next).checkWinningCondition())
            endGame(next);
        if (players.get(next) instanceof BotPlayer)
            letBotMakeItsMove();
        playing = next;
    }

    /*
     * input: void Gets called by ChessGUI.setupNewGame() Initializes all the pieces
     * and assigns them to a player return: void
     */
    public void startNewGame() {
        int BOARDSIZE = chessBoard.size;
        chessBoard.clear();
        if (!chessBoard.enabled)
            chessBoard.enable();
        // create and assign pawns to the players
        for (String key : players.keySet().toArray(new String[players.size()])) {
            players.get(key).pawns.clear();
            for (int i = 0; i < 4; i++)
                players.get(key).pawns.add(new Piece(new ImageIcon(pieceImages.get(key)),
                        (key == "Black") ? Color.BLACK : Color.WHITE, chessBoard.tiles));
        }
        // set up the black pieces
        chessBoard.tiles[2][0].setPiece(players.get("Black").pawns.get(0));
        chessBoard.tiles[BOARDSIZE - 3][0].setPiece(players.get("Black").pawns.get(1));
        chessBoard.tiles[0][2].setPiece(players.get("Black").pawns.get(2));
        chessBoard.tiles[BOARDSIZE - 1][2].setPiece(players.get("Black").pawns.get(3));
        // set up white pieces
        chessBoard.tiles[0][BOARDSIZE - 3].setPiece(players.get("White").pawns.get(0));
        chessBoard.tiles[BOARDSIZE - 1][BOARDSIZE - 3].setPiece(players.get("White").pawns.get(1));
        chessBoard.tiles[2][BOARDSIZE - 1].setPiece(players.get("White").pawns.get(2));
        chessBoard.tiles[BOARDSIZE - 3][BOARDSIZE - 1].setPiece(players.get("White").pawns.get(3));
        // initialize the pawns move pools
        findAllPaths();

        // initialize other variables
        playing = "White";
        message.setText("Chess Champ is ready to play!");
        history = new Stack();
    }

    /*
     * input: void Finds all the possible paths of all the pieces by calling
     * Piece.findPaths() on all the pieces return: void
     */
    private void findAllPaths() {
        // initialize the pawns move pools
        for (String key : players.keySet().toArray(new String[players.size()]))
            for (Piece piece : players.get(key).pawns)
                piece.findPaths();
    }

    private void letBotMakeItsMove() {
        assert (players.get(playing) instanceof BotPlayer);
        BotPlayer bot = (BotPlayer) players.get(playing);
        // compute move
        bot.run();
        // execute the move
        // remove pawn from current spot
        Piece piece = chessBoard.tiles[bot.pawn.position.width][bot.pawn.position.height].removePiece();
        // move it to next spot
        chessBoard.tiles[bot.move.width][bot.move.height].setPiece(piece);
        // compute all new moves
        findAllPaths();
        System.out.println(bot.move);
        // compute shot
        if (bot.algorithm.equals("Random"))
            bot.runAgain();
        // execute the shot
        // shoot at tile
        chessBoard.tiles[bot.shot.width][bot.shot.height].shoot();
        // compute all new moves
        findAllPaths();
        System.out.println(bot.shot);
        // switch players
        switchPlayer();
    }

    /**
     * Ends game by setting message to winner color and disabling input
     */
    public void endGame(String next) {
        this.message.setText(next + " won");
        // disable the board
        this.chessBoard.disable();
    }

    public void endGame() {
        this.message.setText(playing + " has resigned.");
        // disable the board
        this.chessBoard.disable();
    }

    // Getters
    public final Board getBoard() {
        return this.chessBoard;
    }

    public final JLabel getMessage() {
        return this.message;
    }

    // Setters
    public final void setImages(HashMap<String, Image> images) {
        this.pieceImages = images;
    }

    public final void setBoard(Board b) {
        this.chessBoard = b;
    }
}