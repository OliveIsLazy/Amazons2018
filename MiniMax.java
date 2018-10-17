import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class MiniMax {
	
    //private SearchTree tree;
    private GameTile[][] board;
    private String currentP;
    private TreeNode root ;
    private boolean whiteWon = false;
    private boolean blackWon = false;
    int[][] boardCopy;
    int depthLimit;
    int evaluationType = 1 ;
    int[][] bestMove;
    
    
    public MiniMax(GameTile[][] b, String p, int dL) {
    	this.board = b;
    	this.currentP = p;
    	boardCopy = new int[board.length][board[0].length];
    	bestMove = new int[board.length][board[0].length];
    	depthLimit = dL;
    	constructTree();
    	evaluateTree(root, currentP);
    	selectBestMove(root);
    }
	
	private void constructTree() {
        //tree = new SearchTree(null);
    	boardCopy = copyBoard(board);
        root = new TreeNode(boardCopy, currentP, null);
        //tree.setRoot(root);
        expandTree(root, depthLimit);       
    }
    
    private void evaluateTree(TreeNode root, String ogP){
    	int invoker;
    	if(ogP.equals("White")){
    		invoker = 1;
    	}
    	else{
    		invoker = 2 ;
    	}
    	traverseTree(root, invoker) ;
    	
    	//traverse the tree (post order traversal ??) and evaluate nodes and remember path in order to return best root child
    	
    }

    private void selectBestMove(TreeNode root){
    	int bestScore = 0;
    	for(int i = 0; i < root.getChildren().size(); i++){
    		if(bestScore < root.getChildren().get(i).getScore()){
    			bestScore = root.getChildren().get(i).getScore() ;
    			for(int y = 0; y < bestMove.length; y++){
    				for(int x = 0; x < bestMove[0].length; x++){
    					bestMove[y][x] = root.getChildren().get(i).getBoard()[y][x] ;
    				}
    			}
    		}
    	}
    }
    
    private void traverseTree(TreeNode node, int ogP){
    	while(node.hasChildren()){
    		for(int i = 0; i < node.getChildren().size(); i++){
        		traverseTree(node.getChildren().get(i), ogP);
        	}
    	}
    	if(!node.hasChildren()){
    		evaluateNode(node, ogP) ;
    	}
    	if(!node.isRoot()){
    		if(node.getParent().getScore() < node.getScore()){
        		node.getParent().setScore(node.getScore());
        	}
    	}
    	
    }
    
    private void evaluateNode(TreeNode n, int ogP){
    	int score ;
    	Evaluator eval = new Evaluator(evaluationType) ;
    	score = eval.evaluate(n, ogP) ;
    	n.setScore(score);
    }
    
    
    private void expandTree(TreeNode node, int limit){
    	
    	if(limit == 0){
    		return ;
    	}
    	isFinished(node.getBoard(), node) ;
    	if (blackWon || whiteWon) {
    		return;
    	}
    	
    	if(node.getPlayer().equals("White")){
    		for(int i =0; i < boardCopy.length; i++){
    			for(int j= 0; j < boardCopy[0].length; j++){
    				if(boardCopy[i][j] == 1){
    					ArrayList<Point> possibleM = checkForLegalMoves(i,j, boardCopy);
    					for(int p = 0; p < possibleM.size(); p++) {
    						Point dest = possibleM.get(p);
    						int[][] movePerformed = new int[boardCopy.length][boardCopy[0].length];
    						for(int y = 0; y < boardCopy.length; y++) {
    							for (int x = 0; x < boardCopy[0].length; x++) {
    								movePerformed[y][x] = boardCopy[y][x];
    								
    			    			}
    			    		}
    						movePerformed[i][j] = 0;
							movePerformed[dest.y][dest.x] = 1;
							ArrayList<Point> possibleS = checkForLegalMoves(i,j, movePerformed);
							for(int s = 0; s < possibleS.size(); s++){
								Point shot = possibleS.get(s);
								int[][] finished = new int[movePerformed.length][movePerformed[0].length];
								finished[shot.y][shot.x] = 3 ;
								TreeNode newNode = new TreeNode(finished, "Black", node);
								node.addChild(newNode);
							}
    					}
    				}
    			}
    		}
    	}
    	else{
    		for(int i =0; i < boardCopy.length; i++){
    			for(int j= 0; j < boardCopy[0].length; j++){
    				if(boardCopy[i][j] == 2){
    					ArrayList<Point> possibleM = checkForLegalMoves(i,j, boardCopy);
    					for(int p = 0; p < possibleM.size(); p++) {
    						Point dest = possibleM.get(p);
    						int[][] movePerformed = new int[boardCopy.length][boardCopy[0].length];
    						for(int y = 0; y < boardCopy.length; y++) {
    							for (int x = 0; x < boardCopy[0].length; x++) {
    								movePerformed[y][x] = boardCopy[y][x];
    								
    			    			}
    			    		}
    						movePerformed[i][j] = 0;
							movePerformed[dest.y][dest.x] = 2;
							ArrayList<Point> possibleS = checkForLegalMoves(i,j, movePerformed);
							for(int s = 0; s < possibleS.size(); s++){
								Point shot = possibleS.get(s);
								int[][] finished = new int[movePerformed.length][movePerformed[0].length];
								finished[shot.y][shot.x] = 3 ;
								TreeNode newNode = new TreeNode(finished, "White", node);
								node.addChild(newNode);
							}
    					}
    				}
    			}
    		}
    	}
    	limit--;
    	for(int i = 0; i < node.getChildren().size(); i++){
    		expandTree(node.getChildren().get(i), limit) ;
    	}
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
    
    //Player1 is 1, Player2 is 2, Arrow is 3, empty spots are 0
    private int[][] copyBoard(GameTile[][] b) {
    	int board[][] = new int[b.length][b[0].length];
    	for(int x = 0; x < b.length; x++) {
    		for (int y = 0; y < b[0].length; y++) {
    			if (b[y][x].hasPiece) {
    				if(b[y][x].piece.color == Color.WHITE){
    					board[y][x] = 1;
    				}
    				else if (b[y][x].piece.color == Color.BLACK) {
    					board[y][x] = 2;
    				}
    				else if (b[y][x].shot) {
    				board[y][x] = 3;
    				}
    				else { 
    					board[y][x] = 0;
    				}
    			}
    		}
    	}
    	
    	return board;
    	
    }
    
    //Check for moves possible moves for given player, if there are any, game may continue, 
    //if not, method isFinished is true
    private void isFinished(int[][] b, TreeNode node) {
    	if(node.getPlayer().equals("White")){
    		for(int i =0; i < b.length; i++){
    			for(int j= 0; j < b[0].length; j++){
    				if(b[i][j] == 1){
    					ArrayList<Point> possibleM = checkForLegalMoves(i,j, b);
    					if (possibleM.size() == 0){
    						blackWon = true ;
    					}
    				}
    			}
    		}
    	}
    	else{
    		for(int i =0; i < b.length; i++){
    			for(int j= 0; j < b[0].length; j++){
    				if(b[i][j] == 2){
    					ArrayList<Point> possibleM = checkForLegalMoves(i,j, b);
    					if (possibleM.size() == 0){
    						whiteWon = true ;
    					}
    				}
    			}
    		}
    	}
    	
    }
    
}