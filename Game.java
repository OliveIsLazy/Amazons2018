import java.awt.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Stack;

public class Game {
    private boolean headless;
    private HashMap<String, AbstractPlayer> players = new HashMap<String, AbstractPlayer>();
    private String playing = "Black";
    public static Board chessBoard;
    private JLabel message;
    private Stack<String> history;

    Game(HashMap<String, String> commands) {
        headless = commands.get("-headless").equals("true");
        // Builds ChessGUI.chessBoardTiles array
        chessBoard = new Board(Integer.parseInt(commands.get("-size")), Integer.parseInt(commands.get("-size")), this);
        // Creates 2 players as requested by args
        AbstractPlayer white = commands.get("-player1").equals("bot")
                ? new BotPlayer(Color.WHITE, commands.get("-algorithm"), chessBoard)
                : new HumanPlayer(Color.WHITE);
        AbstractPlayer black = commands.get("-player2").equals("bot")
                ? new BotPlayer(Color.BLACK, commands.get("-algorithm"), chessBoard)
                : new HumanPlayer(Color.BLACK);
        players.put("White", white);
        players.put("Black", black);
        // ChessGUI.message
        message = new JLabel();
        history = new Stack();
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
        history.push(chessBoard.encode());
        String next = playing;
        if (playing == "White")
            next = "Black";
        else if (playing == "Black")
            next = "White";
        message.setText(next + "\'s turn");
        if (players.get(next).checkWinningCondition())
            endGame(next);
        if (players.get(next) instanceof BotPlayer)
            letBotMakeItsMove(next);
        else
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
        message.setText("Chess Champ is ready to play!");
        history.empty();
        history.push(chessBoard.encode());
        switchPlayer();
    }

    /*
     * input: void Finds all the possible paths of all the pieces by calling
     * Piece.findPaths() on all the pieces return: void
     */
    public void findAllPaths() {
        // initialize the pawns move pools
        for (String key : players.keySet().toArray(new String[players.size()]))
            for (Piece piece : players.get(key).pawns)
                piece.findPaths();
    }

    private void letBotMakeItsMove(String next) {
        this.playing = next;
        assert (players.get(playing) instanceof BotPlayer);
        BotPlayer bot = (BotPlayer) players.get(playing);
        try {
            // compute move
            assert bot.run();
            // compute all new moves
            findAllPaths();
            System.out.println(next + " bot has played.");
            // switch players
            switchPlayer();
        } catch (AssertionError e) {
            System.out.println("Bot Failed to run...");
            System.exit(1);
        }
    }

    /**
     * Shifts game back one move
     */
    public boolean moveBack() {
        if (history.empty() || history.size() <= 1)
            return false;
        if (players.get(playing).state == "Moving")
            history.remove(history.size() - 1);
        if (chessBoard.decode(history.pop())) {
            System.out.println("Moving back");
            switchPlayer();
            return true;
        } else
            return false;
    }

    /**
     * Ends game by setting message to winner color and disabling input
     */
    public void endGame(String next) {
        if (this.headless) {
            System.out.println(next + " has won the game.");
            System.exit(0);
        } else {
            this.message.setText(next + " won");
            // disable the board
            chessBoard.disable();
        }
    }

    public void endGame() {
        this.message.setText(playing + " has resigned.");
        // disable the board
        chessBoard.disable();
    }

    // Getters
    public final Board getBoard() {
        return chessBoard;
    }

    public final JLabel getMessage() {
        return this.message;
    }

    public final AbstractPlayer getPlayer(String color) {
        return players.get(color);
    }

}