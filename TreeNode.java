import java.util.ArrayList;

public class TreeNode {
	
	private int[][] board ;
	private String player ;
	private TreeNode parent ;
	private ArrayList<TreeNode> children ;
	private int score ;
	
	public TreeNode(int[][] b, String pl, TreeNode pa){
		children = new ArrayList<TreeNode>() ;
		board = b ;
		parent = pa;
		player = pl ;
		score = 0 ;
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
