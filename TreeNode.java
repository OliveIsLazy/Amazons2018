import java.util.ArrayList;



public class TreeNode {
	
	private int[][] board ;
	private String player ;
	private TreeNode parent ;
	private ArrayList<TreeNode> children ;
	private int score ;
	private Position origin ;
	private Position dest ;
	private Position aDest ;
	public Position[] move ;
	
	public TreeNode(int[][] b, String pl, TreeNode pa){
		children = new ArrayList<TreeNode>() ;
		board = b ;
		parent = pa;
		player = pl ;
		score = 0 ;
		move = new Position[3] ;
	}
	
	public Position getOrigin() {
		return origin;
	}

	public void setOrigin(Position origin) {
		this.origin = origin;
	}

	public Position getDest() {
		return dest;
	}

	public void setDest(Position dest) {
		this.dest = dest;
	}

	public Position getaDest() {
		return aDest;
	}

	public void setaDest(Position aDest) {
		this.aDest = aDest;
	}

	

	public void addChild(TreeNode c){
		children.add(c) ;
	}
	
	public ArrayList<TreeNode> getChildren(){
		return children ;
	}
	
	public String getPlayer(){
		return player ;
	}
	
	public TreeNode getParent(){
		return parent ;
	}
	
	public boolean hasChildren(){
		if(children.isEmpty()){
			return false ;
		}
		else{
			return true ;
		}
	}
	
	public int[][] getBoard(){
		return board ;
	}
	
	public boolean hasParent(){
		if(getParent() == null){
			return false;
		}
		else{
			return true ;
		}
	}
	
	public boolean isRoot(){
		if(!hasParent()){
			return true ;
		}
		else{
			return false ;
		}
	}
	
	public void setScore(int s){
		score = s ;
	}
	
	public int getScore(){
		return score ;
	}


}
