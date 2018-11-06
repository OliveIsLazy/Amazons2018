import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

public class Game {
    private HashMap<String, Player> players = new HashMap<String,Player>();
    private String playing = "White";
    private HashMap<String, Image> pieceImages;
    private GameTile[][] chessBoard;
    private JLabel message;
    
    Game(String player1, String player2, String width, String height, String algorithm){
        //Builds ChessGUI.chessBoardTiles array
        buildBoard(Integer.parseInt(width), Integer.parseInt(height));
        //Creates 2 players as requested by args
        Player white = player1.equals("bot") ? new BotPlayer(Color.WHITE, algorithm, chessBoard) : new HumanPlayer(Color.WHITE);
        Player black = player2.equals("bot") ? new BotPlayer(Color.BLACK, algorithm, chessBoard) : new HumanPlayer(Color.BLACK);
        players.put("White", white);
        players.put("Black", black);
        //ChessGUI.message
        message = new JLabel("Chess Champ is ready to play!");
    }

    /*
    * input: number of columns, number of rows
    *   Builds a args[2] x args[3] size chess board using GameTiles
    * return: void
    */
    private void buildBoard(Integer width, Integer height){
        this.chessBoard = new GameTile[width.intValue()][height.intValue()];
        for (int ii = 0; ii < this.chessBoard.length; ii++) {
            for (int jj = 0; jj < this.chessBoard[ii].length; jj++) {
                if ((jj % 2 == 1 && ii % 2 == 1) || (jj % 2 == 0 && ii % 2 == 0)) this.chessBoard[jj][ii] = new GameTile(Color.WHITE, jj, ii, this);
                else this.chessBoard[jj][ii] = new GameTile(Color.BLACK, jj, ii, this);
            }
        }
    }

    /*
    *input: GameTile user interacted with
    *   Applies necessary logic is applied to tile. Called when user interacts with tile
    *return: void
    */
    public void selectTile(GameTile tile){

        if(players.get(playing) instanceof BotPlayer) return;
        if(players.get(playing).selectedTile == null){
            if(tile.hasPiece && players.get(playing).color.equals(tile.piece.color)){
                //Select the tile and show the possible moves
                clearBoard();
                players.get(playing).selectedTile = tile;
                tile.changeColor(false);
                tile.piece.showPaths();
            } else {
                return;
            }
        } else {
            if(players.get(playing).selectedTile.equals(tile)){
                players.get(playing).selectedTile = null;
                clearBoard();
            } else if(tile.hasPiece && players.get(playing).color.equals(tile.piece.color)){
                //Select the tile and show the possible moves
                clearBoard();
                players.get(playing).selectedTile = tile;
                tile.changeColor(false);
                tile.piece.showPaths();
            } else if(players.get(playing).selectedTile.piece.movesPool.contains(tile.position)){
                //Move piece to that tile
                switch(players.get(playing).state){
                    case "Moving" : movePieceTo(tile); break;
                    case "Shooting" : shootArrowTo(tile); players.get(playing).selectedTile = null; clearBoard(); break;
                    default : System.out.println("There was an error"); return;
                }
            } else {
                clearBoard();
                players.get(playing).selectedTile = null;
                return;
            }
        }
    }

    /*
    *input: GameTile user selected piece should move to
    *   Moves the currently selected piece to newTile
    *return: void
    */
    private void movePieceTo(GameTile newTile){
        System.out.println("Moving to " + newTile.position);
        assert(players.get(playing).selectedTile != null);
        Piece piece = players.get(playing).selectedTile.removePiece();
        newTile.setPiece(piece);
        players.get(playing).state = "Shooting";
        players.get(playing).selectedTile = newTile;
        newTile.changeColor(false);
        findAllPaths();
        clearBoard();
        piece.showPaths();
    }

    /*
    *input: GameTile to shoot at
    *   Makes shotTile a restricted area so player cannot move there
    *return: void
    */
    private void shootArrowTo(GameTile shotTile){
        System.out.println("Shooting at " + shotTile.position);
        shotTile.wasShot();
        players.get(playing).state = "Moving";
        findAllPaths();
        switchPlayer();
    }

    /*
    *input: void
    *   Switches playing variable and sets ChessGUI.message in accordance. Used to switch player turns
    *return: void
    */
    private void switchPlayer(){
        if(playing == "White") playing = "Black";
        else if(playing == "Black") playing = "White";
        message.setText(playing + "\'s turn");
        if(players.get(playing).checkWinningCondition()) System.out.println("You win!");
        if(players.get(playing) instanceof BotPlayer) letBotMakeItsMove();
    }

    /*
    *input: void
    *   Resets board to not show any paths
    *return: void
    */
    private void clearBoard(){
        for(GameTile[] row : chessBoard)
            for(GameTile tile : row)
                tile.setToDefaultColor();
    }

    /*
    *input: void
    *   Gets called by ChessGUI.setupNewGame()
    *   Initializes all the pieces and assigns them to a player
    *return: void
    */
    public void startNewGame(){
        int BOARDSIZE = chessBoard.length;
        // create and assign pawns to the players
        for(String key : players.keySet().toArray(new String[players.size()]))
            for(int i = 0; i < 4; i++)
                players.get(key).pawns.add(new Piece(new ImageIcon(pieceImages.get(key)), (key=="Black")?Color.BLACK:Color.WHITE, chessBoard));
        // set up the black pieces
        chessBoard[2][0].setPiece(players.get("Black").pawns.get(0));
        chessBoard[BOARDSIZE-3][0].setPiece(players.get("Black").pawns.get(1));
        chessBoard[0][2].setPiece(players.get("Black").pawns.get(2));
        chessBoard[BOARDSIZE-1][2].setPiece(players.get("Black").pawns.get(3));
        // set up white pieces
        chessBoard[0][BOARDSIZE-3].setPiece(players.get("White").pawns.get(0));
        chessBoard[BOARDSIZE-1][BOARDSIZE-3].setPiece(players.get("White").pawns.get(1));
        chessBoard[2][BOARDSIZE-1].setPiece(players.get("White").pawns.get(2));
        chessBoard[BOARDSIZE-3][BOARDSIZE-1].setPiece(players.get("White").pawns.get(3));
        // initialize the pawns move pools
        findAllPaths();
    }

    /*
    *input: void
    *   Finds all the possible paths of all the pieces
    *   by calling Piece.findPaths() on all the pieces
    *return: void
    */
    private void findAllPaths(){
        // initialize the pawns move pools
        for(String key : players.keySet().toArray(new String[players.size()]))
            for(Piece piece : players.get(key).pawns)
                piece.findPaths();
    }

    private void letBotMakeItsMove(){
        assert(players.get(playing) instanceof BotPlayer);
        BotPlayer bot = (BotPlayer) players.get(playing);
        //compute move
        bot.run();
        //execute the move
            //remove pawn from current spot
        Piece piece = chessBoard[bot.pawn.position.width][bot.pawn.position.height].removePiece();
            //move it to next spot
        chessBoard[bot.move.width][bot.move.height].setPiece(piece);
            //compute all new moves
        findAllPaths();
        System.out.println(bot.move);
        //compute shot
        if(bot.algorithm.equals("Random")) bot.runAgain();
        //execute the shot
            //shoot at tile
        chessBoard[bot.shot.width][bot.shot.height].wasShot();
            //compute all new moves
        findAllPaths();
        System.out.println(bot.shot);
        //switch players
        switchPlayer();
    }

    //Getters
    public final GameTile[][] getBoard(){return this.chessBoard;}
    public final JLabel getMessage(){return this.message;}

    //Setters
    public final void setImages(HashMap<String, Image> images){this.pieceImages=images;}
}