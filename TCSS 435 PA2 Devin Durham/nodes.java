/**
 * Devin Durham
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Node class to handle storing gameboard, parents, children, cost and much more about each game state
 * @author Devin
 *
 */
public class nodes {
	private ArrayList<nodes> pointer;
	private int cost;
	private String[][] board;
	private int row;
	private int col;
	private int boardRotate;
	private String rotateDirection;
	
	/**
	 * constructor
	 * @param boardArray
	 * @param winningCost
	 * @param ArrayRow
	 * @param ArrayCol
	 * @param boardRotated
	 * @param directionOfRotation
	 */
	public nodes(String[][] boardArray, int winningCost, int ArrayRow, int ArrayCol, int boardRotated, String directionOfRotation){
		this.board = boardArray;
		pointer = new ArrayList();
		this.cost = winningCost;
		this.boardRotate = boardRotated;
		this.rotateDirection = directionOfRotation;
		this.row=ArrayRow;
		this.col=ArrayCol;
		
	}
	
	public nodes(){
		
	}
	
	

	public int getBoardRotate() {
		return boardRotate;
	}


	public void setBoardRotate(int boardRotate) {
		this.boardRotate = boardRotate;
	}


	public String getRotateDirection() {
		return rotateDirection;
	}


	public void setRotateDirection(String rotateDirection) {
		this.rotateDirection = rotateDirection;
	}
	
	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public int getCol() {
		return col;
	}


	public void setCol(int col) {
		this.col = col;
	}

	public ArrayList<nodes> getPointer() {
		return pointer;
	}


	public void setPointer(ArrayList pointer) {
		this.pointer = pointer;
	}


	public int getCost() {
		return cost;
	}


	public void setCost(int cost) {
		this.cost = cost;
	}


	public String[][] getBoard() {
		String[][] copy = new String [6][6]; 
		for(int i = 0; i<6; i++){
			for(int j = 0; j<6; j++){
				copy[i][j] = board[i][j];
			}
		}
			
		return copy;
	}


	public void setBoard(String[][] board) {
		this.board = board;
	}
	
	public void addNode(nodes node){
		pointer.add(node);
	}
	
	/**
	 * deep copy of node
	 * @param arr
	 * @return
	 */
	public nodes copyNode(nodes arr){
		nodes newNode = new nodes();
		newNode.setBoard(arr.getBoard());
		newNode.setBoardRotate(arr.getBoardRotate());
		newNode.setCol(arr.getCol());
		newNode.setCost(arr.getCost());
		newNode.setPointer(arr.getPointer());
		newNode.setRotateDirection(arr.getRotateDirection());
		newNode.setRow(arr.getRow());
		
		return newNode;
		
	}

}
