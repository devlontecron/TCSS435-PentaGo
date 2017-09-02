/**
 * Devin Durham
 */

import java.util.ArrayList;

/**
 * handles the game board and manipulations
 * the board is 4 small arrays inside one big array and can also be converted to on big array
 * @author Devin
 *
 */
public class GameBoard {

	public static ArrayList<String> Q1 = new ArrayList<String>();
	public static ArrayList<String> Q2 = new ArrayList<String>();
	public static ArrayList<String> Q3 = new ArrayList<String>();
	public static ArrayList<String> Q4 = new ArrayList<String>();
	public static ArrayList<ArrayList<String>> Q = new ArrayList<ArrayList<String>>();
	static String[][] master = new String[6][6];
	int remaining = 0;

	/**
	 * creates the initial game board 
	 */
	public GameBoard() {
		for (int i = 0; i < 9; i++) {
			Q1.add("-");
			Q2.add("-");
			Q3.add("-");
			Q4.add("-");

		}
		Q.add(Q1);
		Q.add(Q2);
		Q.add(Q3);
		Q.add(Q4);
		
		toMaster();

	}
	
	/**
	 * places the given color token
	 * @param token color
	 * @param sub-board
	 * @param possition
	 */
	public void placeToken(String WorB, int board, int possition) {
		String response;
		remaining++;
		switch (board) {
		case 1:
			Q1.set(possition - 1, WorB);
			Q.set(0, Q1);

			break;
		case 2:
			Q2.set(possition - 1, WorB);
			Q.set(1, Q2);
			break;
		case 3:
			Q3.set(possition - 1, WorB);
			Q.set(2, Q3);
			break;
		case 4:
			Q4.set(possition - 1, WorB);
			Q.set(3, Q4);
			break;
		}
	}
	
	
/**
 * rotates a sub board in a given direction
 * @param board
 * @param direction
 */
	public void rotateBoard(int board, String direction) {
		switch (board) {
		case 1:
			if (direction.equalsIgnoreCase("L")) {
				Q1 = rotateLeft(Q1);
			} else {
				Q1 = rotateRight(Q1);
			}
			Q.set(0, Q1);
			break;
		case 2:
			if (direction.equalsIgnoreCase("L")) {
				Q2 = rotateLeft(Q2);
			} else {
				Q2 = rotateRight(Q2);
			}
			Q.set(1, Q2);
			break;
		case 3:
			if (direction.equalsIgnoreCase("L")) {
				Q3 = rotateLeft(Q3);
			} else {
				Q3 = rotateRight(Q3);
			}
			Q.set(2, Q3);
			break;
		case 4:
			if (direction.equalsIgnoreCase("L")) {
				Q4 = rotateLeft(Q4);
			} else {
				Q4 = rotateRight(Q4);
			}
			Q.set(3, Q4);
			break;
		}
		toMaster();

	}

	/**
	 * helper method to rotate to the left
	 * @param board
	 * @return new game board
	 */
	private ArrayList<String> rotateLeft(ArrayList<String> arr) {
		String holder;

		holder = arr.get(6);
		arr.set(6, arr.get(0));
		arr.set(0, arr.get(2));
		arr.set(2, arr.get(8));
		arr.set(8, holder);

		holder = arr.get(1);
		arr.set(1, arr.get(5));
		arr.set(5, arr.get(7));
		arr.set(7, arr.get(3));
		arr.set(3, holder);

		return arr;
	}

	/**
	 * helper method to rotate to the right
	 * @param board
	 * @return new game board
	 */
	private ArrayList<String> rotateRight(ArrayList<String> arr) {
		String holder;

		holder = arr.get(0);
		arr.set(0, arr.get(6));
		arr.set(6, arr.get(8));
		arr.set(8, arr.get(2));
		arr.set(2, holder);

		holder = arr.get(1);
		arr.set(1, arr.get(3));
		arr.set(3, arr.get(7));
		arr.set(7, arr.get(5));
		arr.set(5, holder);

		return arr;
	}
	
	/**
	 * updates the gameboard and updates the sub arrays with a given large 2d 6x6 array
	 * @param 6x6 2d String Array
	 */
	public void setGameBoardFromArray(String[][] arr){
		ArrayList<String> G1 = new ArrayList<String>();
		ArrayList<String> G2 = new ArrayList<String>();
		ArrayList<String> G3 = new ArrayList<String>();
		ArrayList<String> G4 = new ArrayList<String>();
		ArrayList<ArrayList<String>> G = new ArrayList<ArrayList<String>>();
		
		for(int i = 0; i<3; i++){
			for(int j = 0; j<3; j++){
				G1.add(arr[i][j]);
			}
		}
		
		for(int i = 0; i<3; i++){
			for(int j = 3; j<6; j++){
				G2.add(arr[i][j]);
			}
		}
		
		for(int i = 3; i<6; i++){
			for(int j = 0; j<3; j++){
				G3.add(arr[i][j]);
			}
		}
		
		for(int i = 3; i<6; i++){
			for(int j = 3; j<6; j++){
				G4.add(arr[i][j]);
			}
		}
		
		Q1 = G1;
		Q2 = G2;
		Q3 = G3;
		Q4 = G4;
		
		Q.set(0,Q1);
		Q.set(1,Q2);
		Q.set(2,Q3);
		Q.set(3,Q4);
		toMaster();
		
	}

	/**
	 * converts the sub board to a large 2d 6x6 string array
	 * @return string array
	 */
	public static String[][] toMaster() {

		ArrayList<String> currB = null;
		int row = 0;
		for (int q = 0; q < 4; q++) {
			currB = Q.get(q);
			if (q == 2 || q == 3) {
				row = 3;
			} else {
				row = 0;
			}
			for (int i = 0; i < 9; i++) {
				if (q == 0 || q == 1) {

					if (i % 3 == 0 && i != 0) {
						row++;
					}
					if (q == 0) {
						master[row][(i % 3)] = currB.get(i);
					} else {
						master[row][(i % 3) + 3] = currB.get(i);
					}
				} else {

					if (i % 3 == 0 && i != 0) {
						row++;
					}
					if (q == 2) {
						master[row][(i % 3)] = currB.get(i);
					} else {
						master[row][(i % 3) + 3] = currB.get(i);
					}
				}

			}
		}

		return master;
	}
	
/**
 * checks if the goal state has been reached
 * looks at all possible winning locations and checks for 5 tokens in a row
 * @return
 */
	public String checkGoal() {
		StringBuilder sbb = new StringBuilder();

		toMaster();

		String winner = checkNegDiagonal();
		if (remaining != 36) {

			if (!winner.equals("-")) {
				sbb.append(winner + " has five in a row!\n");
			}

			winner = checkPosDiagonal();
			if (!winner.equals("-")) {
				sbb.append(winner + " has five in a row!\n");
			}
			winner = checkVert();
			if (!winner.equals("-")) {
				sbb.append(winner + " has five in a row!\n");
			}

			winner = checkHor();
			if (!winner.equals("-")) {
				sbb.append(winner + " has five in a row!\n");
			}

		} else {
			sbb.append("No Remiaing spots.\nGame Over");
		}

		return sbb.toString();

	}

	/**
	 * helper method for the checkGoal() method to look at winning locations for negative diagonals
	 * @return string of token who won
	 */
	private static String checkNegDiagonal() {
		String winners = "-";
		int i = 0;
				
		for (int row = 0; row < 2; row++) {
			for (int col = 0; col < 2; col++) {
				if (!master[row][col].equals("-")) {
					int count = 1;
					String currTok = master[row][col];
					boolean streeking = true;
					int loopRow = row + 1;
					int loopCol = col + 1;
					while (count < 5 && streeking) {
						if (currTok.equals(master[loopRow][loopCol])) {
							loopRow++;
							loopCol++;
							count++;
						} else {
							count = 0;
							streeking = false;
						}
					}
					if (streeking == true && i==0) {
						winners = currTok;
						i++;
					}else if(streeking){
						winners = currTok + " " + winners;
					}

				}

			}
		}
		return winners;
	}

	/**
	 * helper method for the checkGoal() method to look at winning locations for positive diagonals
	 * @return string of token who won
	 */
	private static String checkPosDiagonal() {
		int i = 0;
		String winners = "-";
		for (int row = 4; row < 6; row++) {
			for (int col = 0; col < 2; col++) {
				if (!master[row][col].equals("-")) {
					int count = 1;
					String currTok = master[row][col];
					boolean streeking = true;
					int loopRow = row - 1;
					int loopCol = col + 1;
					while (count < 5 && streeking) {
						if (currTok.equals(master[loopRow][loopCol])) {
							loopRow--;
							loopCol++;
							count++;
						} else {
							count = 0;
							streeking = false;
						}
					}
					if (streeking == true && i==0) {
						winners = currTok;
						i++;
					}else if(streeking){
						winners = currTok + " " + winners;
					}

				}

			}
		}
		return winners;
	}

	/**
	 * helper method for the checkGoal() method to look at winning locations for horizontals
	 * @return string of token who won
	 */
	private static String checkHor() {
		int i = 0;
		String winners = "-";
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 2; col++) {
				if (!master[row][col].equals("-")) {
					int count = 1;
					String currTok = master[row][col];
					boolean streeking = true;
					int loopCol = col + 1;
					while (count < 5 && streeking) {
						if (currTok.equals(master[row][loopCol])) {
							loopCol++;
							count++;
						} else {
							count = 0;
							streeking = false;
						}
					}

					if (streeking == true && i==0) {
						winners = currTok;
						i++;
					}else if(streeking){
						winners = currTok + " " + winners;
					}
				}
			}
		}
		return winners;
	}

	/**
	 * helper method for the checkGoal() method to look at winning locations for verticals
	 * @return string of token who won
	 */
	private static String checkVert() {
		int i = 0;
		String winners = "-";
		for (int col = 0; col < 6; col++) {
			for (int row = 0; row < 2; row++) {
				if (!master[row][col].equals("-")) {
					int count = 1;
					String currTok = master[row][col];
					boolean streeking = true;
					int loopRow = row + 1;
					while (count < 5 && streeking) {
						if (currTok.equals(master[loopRow][col])) {
							loopRow++;
							count++;
						} else {
							count = 0;
							streeking = false;
						}
					}

					if (streeking == true && i==0) {
						winners = currTok;
						i++;
					}else if(streeking){
						winners = currTok + " " + winners;
					}
				}
			}
		}
		return winners;
	}

	/**
	 * a graphical representation of the board in its current state
	 * @return String
	 */
	public String displayBoard() {
		StringBuilder sb = new StringBuilder();
		sb.append("+---+---+\n");
		for (int p = 0; p < 6; p++) {
			if (p % 3 == 0 && p != 0) {
				sb.append("+---+---+\n");
			}
			sb.append("|");
			for (int r = 0; r < 6; r++) {
				if (r % 3 == 0 && r != 0) {
					sb.append("|");
					sb.append(master[p][r]);
				} else {
					sb.append(master[p][r]);
				}
			}
			sb.append("|\n");
		}
		sb.append("+---+---+\n");

		return sb.toString();

	}
	
	public String[][] getMaster(){
		String[][] RMaster = new String[6][6];
		
		for(int i = 0; i<master.length; i++){
			for(int j = 0; j<master.length; j++){
				RMaster[i][j] = master[i][j];
			}
		}
		return RMaster;
	}

}
