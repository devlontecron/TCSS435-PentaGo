/**
 * Devin Durham
 * TCSS 435 
 * PA2 
 * 6/9/17
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;


/**
 * Main class that handles the tree making, heuristics, turn playing, and board manipulation
 * @author Devin
 *
 */
public class main {
	static StringBuilder sbb = new StringBuilder();
	static GameBoard myGB = new GameBoard();
	static Scanner scan = new Scanner(System.in);
	static String humanColor;
	static String compColor;
	static boolean isFirstDepth = true;
	static nodes rootNode;
	static int progress = 0;

	/**
	 * welcomes player and asks what color token they choose to be
	 * randomly selects which player will go first
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.print(myGB.displayBoard());
		sbb.append(myGB.displayBoard());

		System.out.println("What color world the human like to be?[White or Black]?");
		sbb.append("What color world the human like to be?[White or Black]?\n");
		String color = scan.nextLine();
		sbb.append(color + "\n");
		if (color.equalsIgnoreCase("white")) {
			humanColor = "W";
			compColor = "B";
		} else {
			humanColor = "B";
			compColor = "W";
		}

		/**
		 * chooses which player will go first
		 */
		Random rando = new Random();
		if ((rando.nextInt(2) == 1)) {
			System.out.println(
					"Human goes first. (\"BoardQuadrant[1-4]/Position[1-9] BoardQuadreant[1-4]LeftOrRightRotate[L,R]\")");
			sbb.append(
					"Human goes first. (\"BoardQuadrant[1-4]/Position[1-9] BoardQuadreant[1-4]LeftOrRightRotate[L,R]\")\n");
			userMove();

		} else {
			System.out.println("Computer goes first.");
			sbb.append("Computer goes first.\n");
			AIMove();
		}
	}

	/**
	 * how the computer plays and makes it move
	 */
	public static void AIMove() {
		System.out.println("Computers turn:");
		sbb.append("Computers turn:\n");

		/**
		 * creates a general root from the current gameboard to be used in the trees
		 */
		rootNode = new nodes(myGB.getMaster(), 0, 0, 0, 1, "L");

		// makes and calculates leafs cost
		makeTreeComp(rootNode, true);
		myGB.setGameBoardFromArray(rootNode.getBoard());
		System.out.println(myGB.displayBoard());
		
		/**
		 * goes through the tree to identify best possible node to play
		 */
		nodes bestMove = minMaxTraversePruning(rootNode);
		// nodes bestMove = minMaxTraverse(rootNode);

		/**
		 * parses out the chosen node to make the move
		 */
		if (bestMove.getRow() > 2) {
			if (bestMove.getCol() > 2) {
				myGB.placeToken(compColor, 4, (((bestMove.getRow() * 3) - 9) + (bestMove.getCol() - 2)));
			} else {
				myGB.placeToken(compColor, 3, (((bestMove.getRow() * 3) - 9) + bestMove.getCol() + 1));
			}
		} else {
			if (bestMove.getCol() > 2) {
				myGB.placeToken(compColor, 2, ((bestMove.getRow() * 3) + (bestMove.getCol() - 2)));
			} else {
				myGB.placeToken(compColor, 1, ((bestMove.getRow() * 3) + bestMove.getCol() + 1));
			}
		}
		 /**
		  * prints out the computers selection and checks for goal state
		  */
		System.out.println("Row: "+bestMove.getRow() + "/" + "Col:" + bestMove.getCol()+ " " + (bestMove.getBoardRotate())+ "" + bestMove.getRotateDirection());
		sbb.append(bestMove.getRow() +"/"+ bestMove.getRow() +" "+ bestMove.getBoardRotate() + bestMove.getRotateDirection()+ "\n");
		myGB.toMaster();
		sbb.append(myGB.displayBoard());
		String winner = myGB.checkGoal();
		if(!winner.isEmpty()){
			System.out.println(winner);
			sbb.append(winner + "\n");
			printToFile();
		}
		System.out.println(myGB.displayBoard());
		myGB.rotateBoard(bestMove.getBoardRotate(), bestMove.getRotateDirection());
		System.out.println(myGB.displayBoard());
		sbb.append(myGB.displayBoard());
		String winnerR = myGB.checkGoal();
		if(!winnerR.isEmpty()){
			System.out.println(winnerR);
			sbb.append(winnerR + "\n");
			printToFile();
		}

		userMove();
	}

	/**
	 * method that traverse the tree (provided by the root)
	 * @param Root
	 * @return nodes optimal play
	 */
	public static nodes minMaxTraverse(nodes Root) {
		int exp = 0;
		boolean rootIsSet = false;
		Root.setCost(-100);
		ArrayList<nodes> D1 = Root.getPointer();

		/**
		 * gets the node from the root
		 */
		for (int i = 0; i < D1.size(); i++) {
			nodes D1node = D1.get(i);
			exp++;
			ArrayList<nodes> D2 = D1node.getPointer();
			
			/**
			 * expands into the 2nd depth node
			 */
			for (int j = 0; j < D2.size(); j++) {
				nodes D2node = D2.get(j);
				exp++;
				int cost = D2node.getCost();
				int beta = Math.min(cost, D1node.getCost());
				if (beta == cost) {
					D1node = D2node;
					}
				}
			
			/**
			 * updates the root node cost
			 */
			int rootCost = Math.max(D1node.getCost(), Root.getCost());
			if (rootCost == D1node.getCost()) {
				Root = D1node;
				rootIsSet = true;
			}
			
		}
		
		/**
		 * sets the node to the best possible node from 1st depth
		 */
		nodes bestRoot = D1.get(0);
		if (!rootIsSet) {
			for (int i = 1; i < D1.size(); i++) {
				nodes D1node = D1.get(i);
				if (D1node.getCost() > bestRoot.getCost()) {
					bestRoot = D1node;
				}
				Root = bestRoot;
			}
		}
		System.out.println(exp);
		return Root;
	}
	
	/**
	 * min max traversal but implements pruning
	 * @param Root
	 * @return nodes optimal play
	 */
	public static nodes minMaxTraversePruning(nodes Root) {
		int exp = 0;
		boolean rootIsSet = false;
		Root.setCost(-100);
		ArrayList<nodes> D1 = Root.getPointer();

		for (int i = 0; i < D1.size(); i++) {
			nodes D1node = D1.get(i);
			exp++;
			ArrayList<nodes> D2 = D1node.getPointer();
			for (int j = 0; j < D2.size(); j++) {
				nodes D2node = D2.get(j);
				exp++;
				int cost = D2node.getCost();
				int beta = Math.min(cost, D1node.getCost());
				if (beta == cost) {
					D1node = D2node;
					
					if(D1node.getCost()<Root.getCost() && rootIsSet){
						j = D2.size();
					}
					}
				}
			
			int rootCost = Math.max(D1node.getCost(), Root.getCost());
			if (rootCost == D1node.getCost()) {
				Root = D1node;
				rootIsSet = true;
			}
		}
		nodes bestRoot = D1.get(0);
		if (!rootIsSet) {
			for (int i = 1; i < D1.size(); i++) {
				nodes D1node = D1.get(i);
				if (D1node.getCost() > bestRoot.getCost()) {
					bestRoot = D1node;
				}
				Root = bestRoot;
			}
		}
		System.out.println(exp);
		return Root;
	}
	
	
/**
 * calculates the cost of each gameboard using the heuristics below
 * @param leaf
 * @return cost
 */
	public static int leafCost(String[][] leaf) {
		String[][] currLeaf = leaf;

		int compRemainingWins = CalcNegDiagonal(currLeaf, compColor) + CalcPosDiagonal(currLeaf, compColor)
				+ CalcVert(currLeaf, compColor) + CalcHorz(currLeaf, compColor);
		int UserRemainingWins = CalcNegDiagonal(currLeaf, humanColor) + CalcPosDiagonal(currLeaf, humanColor)
				+ CalcVert(currLeaf, humanColor) + CalcHorz(currLeaf, humanColor);

		int cost = compRemainingWins - UserRemainingWins;

		return cost;

	}

	/**
	 * how the user's turn is played
	 */
	public static void userMove() {
		System.out.println("Humans turn:");
		sbb.append("Humans turn:\n");
		/**
		 * looks for input from user
		 */
		String input = scan.nextLine();
		sbb.append(input + "\n");
		/**
		 * parse input to make moves on gameboard
		 */
		char[] charIn = input.toCharArray();
		int board = Character.getNumericValue(charIn[0]);
		int pos = Character.getNumericValue(charIn[2]);
		int boardRotate = Character.getNumericValue(charIn[4]);
		String LorR = Character.toString(charIn[5]);

		/**
		 * places the token, 
		 * updates board
		 * checks goal
		 */
		myGB.placeToken(humanColor, board, pos);
		myGB.toMaster();
		System.out.print(myGB.displayBoard());
		sbb.append(myGB.displayBoard());
		String winner = myGB.checkGoal();
		if(!winner.isEmpty()){
			System.out.println(winner);
			sbb.append(winner + "\n");
			printToFile();
		}
		
		/**
		 * rotates board
		 * checks goal
		 */
		myGB.rotateBoard(boardRotate, LorR);
		System.out.print(myGB.displayBoard());
		sbb.append(myGB.displayBoard());
		String winnerR = myGB.checkGoal();
		if(!winnerR.isEmpty()){
			System.out.println(winnerR);
			sbb.append(winnerR + "\n");
			printToFile();
		}

		AIMove();
	}

	/**
	 * method to make the tree starting with the computer making all possible moves
	 * @param root node
	 * @param isRoot
	 */
	public static void makeTreeComp(nodes arr, boolean isRoot) {

		nodes copyNode = arr.copyNode(arr);

		/**
		 * expeands into all possible plays given the root gameboard
		 */
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 6; c++) {
				String[][] root = copyNode.getBoard();
				if (root[r][c].equals("-")) {
					String[][] newBoard = root.clone();
					newBoard[r][c] = compColor;

					GameBoard currGB = new GameBoard();
					currGB.setGameBoardFromArray(newBoard);
					nodes Child;
					/**
					 * adds nodes to parents nodes list
					 */
					if (!isRoot) {
						Child = new nodes(newBoard, leafCost(newBoard), r, c, 4, "R");
					} else {
						Child = new nodes(newBoard, 0, r, c, 4, "R");
					}
					arr.addNode(Child);

					/**
					 * after placing token, all possible board rotations are also calculated and added
					 */
					for (int t = 1; t < 5; t++) {
						currGB.rotateBoard(t, "L");
						String[][] Lrot = currGB.getMaster();
						nodes leftChild;
						if (!isRoot) {
							leftChild = new nodes(Lrot, leafCost(Lrot), r, c, t, "L");
						} else {
							leftChild = new nodes(Lrot, 0, r, c, t, "L");
						}
						arr.addNode(leftChild);

						currGB.rotateBoard(t, "R");
						currGB.rotateBoard(t, "R");
						String[][] Rrot = currGB.getMaster();
						nodes rightChild;
						if (!isRoot) {
							rightChild = new nodes(Rrot, leafCost(Rrot), r, c, t, "R");
						} else {
							rightChild = new nodes(Rrot, 0, r, c, t, "R");
						}
						arr.addNode(rightChild);

						currGB.rotateBoard(t, "L");

					}
				}
			}
		}

		/**
		 * sends nodes over to helper mothd to create the next depth
		 */
		if (isRoot) {
			// expand into new depth
			ArrayList<nodes> arrPointer = copyNode.getPointer();
			for (int e = 0; e < arrPointer.size(); e++) {
				makeTreeHuman(arrPointer.get(e));
			}
		}

	}

	/**
	 * similer to makeTreeComputer, this method places all possible moves the human may make
	 * @param node
	 */
	public static void makeTreeHuman(nodes arr) {
		nodes copyNode = arr.copyNode(arr);

		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 6; c++) {
				String[][] root = copyNode.getBoard();
				if (root[r][c].equals("-")) {
					String[][] newBoard = root.clone();
					newBoard[r][c] = humanColor;
					/**
					 * checks for all possible token placements
					 */
					GameBoard currGB = new GameBoard();
					currGB.setGameBoardFromArray(newBoard);
					nodes Child = new nodes(newBoard, leafCost(newBoard), r, c, 4, "R");
					arr.addNode(Child);
					/**
					 * checks for all possible board rotations
					 */
					for (int t = 1; t < 5; t++) {
						currGB.rotateBoard(t, "L");
						String[][] Lrot = currGB.getMaster();
						nodes leftChild = new nodes(Lrot, leafCost(Lrot), r, c, t, "L");
						/**
						 * add node to parent
						 */
						arr.addNode(leftChild);

						currGB.rotateBoard(t, "R");
						currGB.rotateBoard(t, "R");
						String[][] Rrot = currGB.getMaster();
						nodes rightChild = new nodes(Rrot, leafCost(Rrot), r, c, t, "R");
						arr.addNode(rightChild);

						currGB.rotateBoard(t, "L");
					}
				}
			}
		}
		/**
		 // expand into new 3rd depth 
		 ArrayList<nodes> arrPointer = copyNode.getPointer(); 
		 for (int e = 0; e < arrPointer.size(); e++) {
		 makeTreeComp(arrPointer.get(e), false);
		 
		 }
		**/
	}

	/**
	 * creates and writes to file Output.txt from String buffer to record console
	 * (im aware there is a better way to do this. i didnt have time to figure it out
	 */
	public static void printToFile() {

		File newFile = new File("Output.txt");
		try {
			newFile.createNewFile();
			FileWriter fw = new FileWriter(newFile);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sbb.toString());
			bw.flush();
			bw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

		System.exit(0);

	}

	/**
	 * heuristic calculation for possible winning cases of the negative diagonal
	 * if user token is seen ++
	 * if able to win in this row ++
	 * if not able to win in this row --
	 * 
	 * @param gameboard
	 * @param Token color
	 * @return cost
	 */
	private static int CalcNegDiagonal(String[][] arr, String Token) {
		int count = 0;
		int inARow = 0;
		for (int row = 0; row < 2; row++) {
			for (int col = 0; col < 2; col++) {
				if (arr[row][col].equals("-") || arr[row][col].equals(Token)) {
					boolean streeking = true;
					int loopRow = row + 1;
					int loopCol = col + 1;
					int index = 1;
					while (index < 5 && streeking) {
						if (arr[loopRow][loopCol].equals("-")) {
							loopRow++;
							loopCol++;
							index++;
						} else if (arr[loopRow][loopCol].equals(Token)) {
							loopRow++;
							loopCol++;
							index++;
							inARow++;
						} else {
							streeking = false;
							inARow = 0;
							count--;
							

						}
					}
					if (streeking == true) {
						count = count + inARow + 1;
					}
				}
			}
		}
		return count;
	}

	/**
	 * heuristic calculation for possible winning cases of the positive diagonal
	 * if user token is seen ++
	 * if able to win in this row ++
	 * if not able to win in this row --
	 * 
	 * @param gameboard
	 * @param Token color
	 * @return cost
	 */
	private static int CalcPosDiagonal(String[][] arr, String Token) {
		int count = 0;
		int inARow = 0;
		for (int row = 4; row < 6; row++) {
			for (int col = 0; col < 2; col++) {
				if (arr[row][col].equals("-") || arr[row][col].equals(Token)) {
					boolean streeking = true;
					int loopRow = row - 1;
					int loopCol = col + 1;
					int index = 1;
					while (index < 5 && streeking) {
						if (arr[loopRow][loopCol].equals("-")) {
							loopRow--;
							loopCol++;
							index++;
						} else if (arr[row][col].equals(Token)) {
							loopRow--;
							loopCol++;
							index++;
							inARow++;
						} else {
							streeking = false;
							inARow = 0;
							count--;
						}
					}
					if (streeking == true) {
						count = count + inARow + 1;
					}

				}

			}
		}
		return count;
	}

	/**
	 * heuristic calculation for possible winning cases of vertical wins
	 * if user token is seen ++
	 * if able to win in this row ++
	 * if not able to win in this row --
	 * 
	 * @param gameboard
	 * @param Token color
	 * @return cost
	 */
	private static int CalcVert(String[][] arr, String Token) {
		int count = 0;
		int inARow = 0;
		for (int col = 0; col < 6; col++) {
			for (int row = 0; row < 2; row++) {
				if (arr[row][col].equals("-") || arr[row][col].equals(Token)) {
					boolean streeking = true;
					int loopRow = row + 1;
					int index = 1;
					while (index < 5 && streeking) {
						if (arr[loopRow][col].equals("-")) {
							loopRow++;
							index++;
						} else if (arr[loopRow][col].equals(Token)) {
							loopRow++;
							index++;
							inARow++;
						} else {
							streeking = false;
							inARow = 0;
							count--;
						}
					}
					if (streeking == true) {
						count = count + inARow + 1;
					}

				}

			}
		}
		return count;
	}

	/**
	 * heuristic calculation for possible winning cases of the horizontal wins
	 * if user token is seen ++
	 * if able to win in this row ++
	 * if not able to win in this row --
	 * 
	 * @param gameboard
	 * @param Token color
	 * @return cost
	 */
	private static int CalcHorz(String[][] arr, String Token) {
		int count = 0;
		int inARow = 0;
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 2; col++) {
				if (arr[row][col].equals("-") || arr[row][col].equals(Token)) {
					boolean streeking = true;
					int loopCol = col + 1;
					int index = 1;
					while (index < 5 && streeking) {
						if (arr[row][loopCol].equals("-")) {
							loopCol++;
							index++;
						} else if (arr[row][loopCol].equals(Token)) {
							loopCol++;
							index++;
							inARow++;
						} else {
							streeking = false;
							count--;
							inARow = 0;
						}
					}
					if (streeking == true) {
						count = count + inARow + 1;
					}

				}

			}
		}
		return count;
	}

}
