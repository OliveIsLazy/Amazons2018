import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

public class Game {
    private HashMap<String, Player> players = new HashMap<String,Player>();
    private String playing = "White";
    private GameTile[][] chessBoard;
    private JLabel message;
    
    Game(String player1, String player2, String width, String height){
        //Creates 2 players as requested by args
        Player white = player1.equals("Bot") ? new BotPlayer(Color.WHITE) : new HumanPlayer(Color.WHITE);
        Player black = player2.equals("Bot") ? new BotPlayer(Color.BLACK) : new HumanPlayer(Color.BLACK);
        players.put("White", white);
        players.put("Black", black);
        //Builds ChessGUI.chessBoardTiles array
        buildBoard(Integer.parseInt(width), Integer.parseInt(height));
        //ChessGUI.message
        message = new JLabel("Chess Champ is ready to play!");
    }

    /*
    * input: number of columns, number of rows
    * Builds a args[2] x args[3] size chess board using GameTiles
    * return: void
    */
    private void buildBoard(Integer width, Integer height){
        this.chessBoard = new GameTile[width.intValue()][height.intValue()];
        for (int ii = 0; ii < this.chessBoard.length; ii++) {
            for (int jj = 0; jj < this.chessBoard[ii].length; jj++) {
                if ((jj % 2 == 1 && ii % 2 == 1) || (jj % 2 == 0 && ii % 2 == 0)) this.chessBoard[jj][ii] = new GameTile(Color.WHITE, ii, jj, this);
                else this.chessBoard[jj][ii] = new GameTile(Color.BLACK, ii, jj, this);
            }
        }
    }

    /*
    *input: GameTile user interacted with
    *Applies necessary logic is applied to tile. Called when user interacts with tile
    *return: void
    */
    public void selectTile(GameTile tile){ //SELECTING ANOTHER TILE WHILE BEING ASKED TO SHOOT RESULTS IN A BUGGY BEHAVIOUR
        if (tile.inPath){
            System.out.println("moving to: " + tile.y + " " + tile.x);
            if(players.get(playing).state.equals("Moving")){ movePieceTo(tile); return; }
            else if(players.get(playing).state.equals("Shooting")) shootArrowTo(tile);
        } else if(tile.hasPiece && players.get(playing).color.equals(tile.piece.color) && players.get(playing).state.equals("Moving")){
            clearBoard();
            if(!tile.equals(players.get(playing).selectedTile)){
                System.out.println(tile.y + " " + tile.x);            
                players.get(playing).selectedTile = tile;
                tile.changeColor(false);
                showPaths(tile.y, tile.x);
                return;
            }
        }
        clearBoard();
        players.get(playing).selectedTile = null;
    }

    /*
    *input: GameTile user selected piece should move to
    *Moves the currently selected piece to newTile
    *return: void
    */
    private void movePieceTo(GameTile newTile){
        clearBoard();
        assert(players.get(playing).selectedTile != null);
        Piece piece = players.get(playing).selectedTile.removePiece();
        newTile.setPiece(piece);
        players.get(playing).state = "Shooting";
        players.get(playing).selectedTile = newTile;
        newTile.changeColor(false);
        showPaths(newTile.y, newTile.x);
    }

    /*
    *input: GameTile to shoot at
    *Makes shotTile a restricted area so player cannot move there
    *return: void
    */
    private void shootArrowTo(GameTile shotTile){
        shotTile.wasShot();
        players.get(playing).state = "Moving";
        switchPlayer();
    }

    /*
    *input: void
    *Switches playing variable and sets ChessGUI.message in accordance. Used to switch player turns
    *return: void
    */
    private void switchPlayer(){
        if(playing == "White") playing = "Black";
        else if(playing == "Black") playing = "White";
        message.setText(playing + "\'s turn");
    }

    /*
    *input: void
    *Resets board to not show any paths
    *return: void
    */
    private void clearBoard(){
        for(GameTile[] row : chessBoard)
            for(GameTile tile : row)
                tile.setToDefaultColor();
    }
    
    /*
    *input: column index, row index of currently selected tile
    *Changes color of tiles that are in the currently selected Piece's path
    *return: void
    */
    private void showPaths(int ii, int jj){
        boolean top_flag, bottom_flag, right_flag, left_flag, bottom_right_flag, top_right_flag, bottom_left_flag, top_left_flag;
        top_flag = bottom_flag = right_flag = left_flag = bottom_right_flag = top_right_flag = bottom_left_flag = top_left_flag = false;
        for(int i = 1; i < chessBoard.length; i++){
            //vertical and horizontal paths
            top_flag = conditionalColoring(ii-i, jj, ii-i>=0, top_flag);
            bottom_flag = conditionalColoring(ii+i, jj, ii+i<chessBoard.length, bottom_flag);
            right_flag = conditionalColoring(ii, jj+i, jj+i<chessBoard[i].length, right_flag);
            left_flag = conditionalColoring(ii, jj-i, jj-i>=0, left_flag);
            //diagonal paths
            bottom_right_flag = conditionalColoring(ii+i, jj+i, (ii+i < chessBoard.length && jj+i < chessBoard[i].length), bottom_right_flag);
            top_right_flag = conditionalColoring(ii+i, jj-i, (ii+i < chessBoard.length && jj-i >= 0), top_right_flag);
            bottom_left_flag = conditionalColoring(ii-i, jj+i, (ii-i >= 0 && jj+i < chessBoard[i].length), bottom_left_flag);
            top_left_flag = conditionalColoring(ii-i, jj-i, (ii-i >= 0 && jj-i > 0), top_left_flag);
        }
    }

    /*
    *input: colum index of tile to check, row index of tile to check, boolean check if indices exist, stopping flag
    *Sets the color of tile at [i_index, j_index] depending on whether index_check and flag are true and false respectively 
    *return: boolean that represents whether to stop the path in flag direction or not
    */
    boolean conditionalColoring(int i_index, int j_index, boolean index_check, boolean flag){
        if(index_check && !flag){
            if(chessBoard[i_index][j_index].hasPiece)
                return true;
            else
                chessBoard[i_index][j_index].changeColor(true);
        }
        return flag;
    }


    //Getters
    public GameTile[][] getBoard(){return this.chessBoard;}
    public JLabel getMessage(){return this.message;}
}