import java.awt.Point;
import java.util.ArrayList;

public class Evaluator {
	
	private int type ;
		
	public Evaluator(int t){
		type = t ;
	}
	
	
	public int evaluate(TreeNode t, int ogP){
		if(type == 1){
			return ownMobilityEvaluate(t, ogP) ;
		}
		return 0 ;
	}
	
	public int ownMobilityEvaluate(TreeNode node, int ogP){
		//score is just equal to the number of possible moves for the invoking player at any given boardstate
		int score = 0 ;
		for(int i =0; i < node.getBoard().length; i++){
			for(int j = 0 ; j < node.getBoard()[0].length; j++){
				if(node.getBoard()[i][j] == ogP){
					ArrayList<Point> cntr = new ArrayList() ;
					cntr = checkForLegalMoves(i, j, node.getBoard()) ;
					score = score + cntr.size() ;
				}
			}
		}
		return score ;
	}
	
	private ArrayList<Point> checkForLegalMoves(int i, int j, int[][] board){
    	ArrayList<Point> moves = new ArrayList<>();
    	//horizontal moves
    	for(int xRight = j; xRight < board[0].length; xRight++){
    		if(board[i][xRight] == 1 || board[i][xRight] == 2 || board[i][xRight] == 3){
    			break ;
    		}
    		moves.add(new Point(xRight, i));
    	}
    	for(int xLeft = j; xLeft >= 0; xLeft--){
    		if(board[i][xLeft] == 1 || board[i][xLeft] == 2 || board[i][xLeft] == 3){
    			break ;
    		}
    		moves.add(new Point(xLeft, i));
    	}
    	//vertical moves
    	for(int yDown = i; yDown < board.length; yDown++){
    		if(board[yDown][j] == 1 || board[yDown][j] == 2 || board[yDown][j] == 3){
    			break ;
    		}
    		moves.add(new Point(j, yDown));
    	}
    	for(int yUp = i; yUp >= 0; yUp--){
    		if(board[yUp][j] == 1 || board[yUp][j] == 2 || board[yUp][j] == 3){
    			break ;
    		}
    		moves.add(new Point(j, yUp)) ;
    	}
    	//diagonal moves
    	//top right
    	for(int yUp = i; yUp >= 0; yUp--){
    		for(int xRight = j; xRight < board[0].length; xRight++){
    			if(board[yUp][xRight] == 1 || board[yUp][xRight] == 2 || board[yUp][xRight] == 3){
        			break ;
        		}
    			moves.add(new Point(xRight, yUp)) ;
    		}
    	}
    	//bottom right
    	for(int yDown = i; yDown <  board.length; yDown++){
    		for(int xRight = j; xRight < board[0].length; xRight++){
    			if(board[yDown][xRight] == 1 || board[yDown][xRight] == 2 || board[yDown][xRight] == 3){
        			break ;
        		}
    			moves.add(new Point(yDown, xRight));
    		}
    	}
    	//bottom left
    	for(int yDown = i; yDown < board.length; yDown++){
    		for(int xLeft = j; xLeft >= 0; xLeft--){
    			if(board[yDown][xLeft] == 1 || board[yDown][xLeft] == 2 || board[yDown][xLeft] == 3){
    				break;
    			}
    			moves.add(new Point(yDown, xLeft)) ;
    		}
    	}
    	//top left
    	for(int yUp = i; yUp >= 0; yUp--){
    		for(int xLeft = j; xLeft >= 0; xLeft--){
    			if(board[yUp][xLeft] == 1 || board[yUp][xLeft] == 2 || board[yUp][xLeft] == 3){
    				break  ;
    			}
    			moves.add(new Point(yUp, xLeft)) ;
    		}
    	}
    	return moves;
    }

	
}
