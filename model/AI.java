import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class AI {
	
	Color color;
	boolean debug = true;
	
	public AI(Color color) {
		this.color = color;
	}
	
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * This is the opposite method to getXY. It takes some XY and converts it to the indices needed for field.
	 * @param x
	 * @param y
	 * @return the index
	 */
	private int getIndex(int x, int y) {
		int index = -1;
		//the indices in the board:
			//0:1,4 1:2,4 2:3,4 ... 7:8,4
			//8:1,3 ... 15:8,3
			//16:1,2 2,2 3,2 4,2 ... 23:8,2 
			//24:1,1 2,1 3,1 ... 31:8,1
		if(y == 1) {
			index = 24;
		}else if(y == 2) {
			index = 16;
		}else if(y == 3) {
			index = 8;
		}else if(y == 4) {
			index = 0;
		}
		
		return index+(x-1);
	}
	
	/**
	 * The tokens on the field, as in validMoves, are indexed by values 0-31. However, the moves need their x/y coordinates.
	 * This converts some index to some token on the field into X,Y coordinates (i.e.; a move).
	 * @elaborating A field is a string array of all tokens on the field in the order from top-left to bottom-right, reading up-left bottom-down.
	 * @param index Some value from 0-31.
	 * @return the X,Y coordinates known from the index of some token in the field String[].
	 */
	private int[] getXY(int index) {
		//getting x
		int x = index % 8;
		
		//getting y
		int y = -1;
		
		if(index <= 7) {
			y = 4;
		}
		
		if(index > 7) {
			y = 3;
		}
		
		if(index > 15) {
			y = 2;
		}
		
		if(index > 23) {
			y = 1;
		}
		
		//it is x+1 because the board is indexed at 1 and not 0
		return new int[] {x+1, y};
	}
	
	private boolean isOnBoard(int x, int y) {
		if(x<1 || x>8) {
			return false;
		}else if(y<1 || y>4) {
			return false;
		}else return true;
	}
	
	private boolean correctColor(char thisLetter, char otherLetter) {
		Color thisColor = null;
		Color otherColor = null;
		
		if(thisLetter == 'R') {
			thisColor = Color.RED;
		}else if(thisLetter == 'B') {
			thisColor = Color.BLACK;
		}
		
		if(otherLetter == 'R') {
			otherColor = Color.RED;
		}else if(otherLetter == 'B') {
			otherColor = Color.BLACK;
		}
		
		if(thisColor == color) {
			if(otherLetter == 'X') {
				return true;
			}
			if(otherColor == thisColor) {
				return false;
			}else return true;
		}else return false;
	}
	
	private void checkDirection(ArrayList<int[]> moves, ArrayList<int[]> attacks, String[] field, String token, int x, int y, int newX, int newY) {
		if(isOnBoard(newX, newY)) {
			if(field[getIndex(newX, newY)].charAt(2) == 'U' || field[getIndex(newX, newY)].charAt(2) == 'X') {
				if(correctColor(token.charAt(0), field[getIndex(newX, newY)].charAt(0))) {
					if(field[getIndex(newX, newY)].charAt(0) == 'X') {
						moves.add(new int[] {x, y, newX, newY});
						if(debug) System.out.println("[" + x + "," + y + " | " + newX + "," + newY + "]");
					}
				}
				if(correctColor(token.charAt(0), field[getIndex(newX, newY)].charAt(0))) {
					if(field[getIndex(newX, newY)].charAt(1) <= token.charAt(1)) {
						attacks.add(new int[] {x, y, newX, newY});
						if(debug) System.out.println("[" + x + "," + y + " | " + newX + "," + newY + "]");
					}	
				}
			}
		}
	}
	
	private void checkDirectionSoldier(ArrayList<int[]> moves, ArrayList<int[]> attacks, String[] field, String token, int x, int y, int newX, int newY) {
		if(token.charAt(1) == '1') {
			if(isOnBoard(newX, newY)) {
				if(field[getIndex(newX, newY)].charAt(2) == 'U' || field[getIndex(newX, newY)].charAt(2) == 'X') {
					if(correctColor(token.charAt(0), field[getIndex(newX, newY)].charAt(0))) {
						if(field[getIndex(newX, newY)].charAt(0) == 'X') {
							moves.add(new int[] {x, y, newX, newY});
							if(debug) System.out.println("[" + x + "," + y + " | " + newX + "," + newY + "]");
						}
					}
					if(correctColor(token.charAt(0), field[getIndex(newX, newY)].charAt(0))) {
						if(field[getIndex(newX, newY)].charAt(1) == '1' || field[getIndex(newX, newY)].charAt(1) == '7') {
							attacks.add(new int[] {x, y, newX, newY});
							if(debug) System.out.println("[" + x + "," + y + " | " + newX + "," + newY + "]");
						}	
					}
				}
			}
		}
	}
	
	private void checkDirectionGeneral(ArrayList<int[]> moves, ArrayList<int[]> attacks, String[] field, String token, int x, int y, int newX, int newY) {
		if(token.charAt(1) == '7') {
			if(isOnBoard(newX, newY)) {
				if(field[getIndex(newX, newY)].charAt(2) == 'U' || field[getIndex(newX, newY)].charAt(2) == 'X') {
					if(correctColor(token.charAt(0), field[getIndex(newX, newY)].charAt(0))) {
						if(field[getIndex(newX, newY)].charAt(0) == 'X') {
							moves.add(new int[] {x, y, newX, newY});
							if(debug) System.out.println("[" + x + "," + y + " | " + newX + "," + newY + "]");
						}
					}
					if(correctColor(token.charAt(0), field[getIndex(newX, newY)].charAt(0))) {
						if(field[getIndex(newX, newY)].charAt(1) != '1') {
							attacks.add(new int[] {x, y, newX, newY});
							if(debug) System.out.println("[" + x + "," + y + " | " + newX + "," + newY + "]");
						}	
					}
				}
			}
		}
	}
	
	private void checkDirectionCannon(ArrayList<int[]> moves, ArrayList<int[]> attacks, String[] field, String token, int x, int y, int newX, int newY) {
		if(isOnBoard(newX, newY)) {
			if(field[getIndex(newX, newY)].charAt(2) == 'U' || field[getIndex(newX, newY)].charAt(2) == 'X') {
				if(correctColor(token.charAt(0), field[getIndex(newX, newY)].charAt(0))) {
					if(field[getIndex(newX, newY)].charAt(0) == 'X') {
						moves.add(new int[] {x, y, newX, newY});
						if(debug) System.out.println("[" + x + "," + y + " | " + newX + "," + newY + "]");
					}
				}
			}
		}
	}
	
	private void checkCannon(ArrayList<int[]> attacks, String[] field, String token, int startX, int startY, int endX, int endY) {
		if(field[getIndex(endX, endY)].charAt(2) == 'U') {
			if(correctColor(token.charAt(0), field[getIndex(endX, endY)].charAt(0))) {
				int jumped = 0;
				//check Y path
				if(startX==endX && Math.abs(startY-endY)>=2){
					if(startY<endY){
						for(int y=startY+1; y<endY; y++){//Y move down
							if(field[getIndex(startX, y)].charAt(1) != 'X'){
								jumped++;
							}
						}
					}else{
						for(int y=startY-1; y>endY; y--){//Y move up
							if(field[getIndex(startX, y)].charAt(1) != 'X'){
								jumped++;
							}
						}
					}
					
				//check X path
				}else if(startY==endY && Math.abs(startX-endX)>=2){
					
					if(startX<endX){
						for(int x=startX+1; x<endX; x++){//X move right
							if(field[getIndex(x, startY)].charAt(1) != 'X'){
								jumped++;
							}
						}
					}else{
						for(int x=startX-1; x>endX; x--){//X move left
							if(field[getIndex(x, startY)].charAt(1) != 'X'){
								jumped++;
							}
						}
					}
				}
				
				if(jumped==1){
					attacks.add(new int[] {startX, startY, endX, endY});
					if(debug) System.out.println("[" + startX + "," + startY + " | " + endX + "," + endY + "]");
				}
			}
		}
	}
	
	/**
	 * For some given string state, returns a list of all moves possible. A move is an integer array of 4 numbers: {x1, y1, x2, y2}.
	 * @param state = "FIELD . GRAVEYARD"
	 * @return An ArrayList containing moves[][], flips[][], and attacks[][]
	 */
	public ArrayList<int[][]> validMoves(String state){
		ArrayList<int[]> moves = new ArrayList<int[]>();
		ArrayList<int[]> flips = new ArrayList<int[]>();
		ArrayList<int[]> attacks = new ArrayList<int[]>();
		
		//first, split the field/graveyard and then the tokens themselves
		String[] splitState = state.split(" . ");
		String field_raw = splitState[0];
		String[] field = field_raw.split(" ");
		
		//then, add the moves of flipping all tokens
		if(debug) System.out.println("Valid Flips:");
		for(int c=0;c<field.length;c++) {
			String token = field[c];
			if(token.charAt(2) == ('D')) {
				int[] xy = getXY(c);
				flips.add(new int[] {xy[0], xy[1], xy[0], xy[1]});
				if(debug) System.out.println("[" + xy[0] + "," + xy[1] + " | " + xy[0] + "," + xy[1] + "]");
			}
		}
		
		if(debug) System.out.println("Valid Moves:");
		for(int i=0; i<field.length; i++) {
			String token = field[i];
			
			if(token.charAt(2) == ('U')) {
				int[] xy = getXY(i);
				int x = xy[0];
				int y = xy[1];
				
				//Cannon
				if(token.charAt(1) == '2') {
					for(int cannonX=1; cannonX<9; cannonX++) {
						checkCannon(attacks, field, token, x, y, cannonX, y);
					}
					for(int cannonY=1; cannonY<5; cannonY++) {
						checkCannon(attacks, field, token, x, y, x, cannonY);
					}
					
					checkDirectionCannon(moves, attacks, field, token, x, y, x, y+1);
					checkDirectionCannon(moves, attacks, field, token, x, y, x, y-1);
					checkDirectionCannon(moves, attacks, field, token, x, y, x-1, y);
					checkDirectionCannon(moves, attacks, field, token, x, y, x+1, y);
				}
				//General
				else if(token.charAt(1) == '7') {
					checkDirectionGeneral(moves, attacks, field, token, x, y, x, y+1);
					checkDirectionGeneral(moves, attacks, field, token, x, y, x, y-1);
					checkDirectionGeneral(moves, attacks, field, token, x, y, x-1, y);
					checkDirectionGeneral(moves, attacks, field, token, x, y, x+1, y);
				}
				//Soldier
				else if(token.charAt(1) == '1') {
					checkDirectionSoldier(moves, attacks, field, token, x, y, x, y+1);
					checkDirectionSoldier(moves, attacks, field, token, x, y, x, y-1);
					checkDirectionSoldier(moves, attacks, field, token, x, y, x-1, y);
					checkDirectionSoldier(moves, attacks, field, token, x, y, x+1, y);
				}else {
					checkDirection(moves, attacks, field, token, x, y, x, y+1);
					checkDirection(moves, attacks, field, token, x, y, x, y-1);
					checkDirection(moves, attacks, field, token, x, y, x-1, y);
					checkDirection(moves, attacks, field, token, x, y, x+1, y);
				}
			}
		}
		
		//then turn out data into an actual output as expected
		int[][] allMoves = new int[moves.size()][4];
		int[][] allFlips = new int[flips.size()][4];
		int[][] allAttacks = new int[attacks.size()][4];

		int counter = 0;
		for(int[] move : moves) {
			allMoves[counter] = move;
			counter++;
		}
		
		counter = 0;
		for(int[] flip : flips) {
			allFlips[counter] = flip;
			counter++;
		}
		
		counter = 0;
		for(int[] attack : attacks) {
			allAttacks[counter] = attack;
			counter++;
		}
		
		ArrayList<int[][]> output = new ArrayList<int[][]>();
		output.add(allMoves);
		output.add(allFlips);
		output.add(allAttacks);
		return output;
	}
	
	/**
	 * For some given move, and some given state, it makes the move on that state and returns the new state created as a result.
	 * If it cannot make t	he given move it simply returns null.
	 * @param move A move is an integer array of 4 numbers: {x1, y1, x2, y2}.
	 * @param state = "FIELD . GRAVEYARD"
	 * @return null if the move was not valid, the new state created otherwise.
	 */
	public String makeMove(int[] move, String state) {	
		String[] field = state.split(" . ")[0].split(" ");
		String graveyard = state.split(" . ")[1];
		int indexFirst = getIndex(move[0], move[1]);
		char firstColor = field[indexFirst].charAt(0);
		int indexSecond = getIndex(move[2], move[3]);
		char secondColor = field[indexSecond].charAt(0);
		//make a newState
		String newState = "";
		//replace the token at (move[2], move[3]) with the token that was at (move[0], move[1])...
		//...and then replace the token at (move[0], move[1]) with XXX
		field[indexSecond] = field[indexFirst];
		field[indexFirst] = "XXX";
		//set everything up
		for(String token : field) {
			newState += token + " ";
		}
		newState += graveyard;
		//then return the newState onto which that move was made
		return newState;
	}
	
	/**
	 * Returns the score of the board. The higher the score, the better the board is for the player recognized by playerColor.
	 * @param playerColor The color of the player for whom it calculates the score.
	 * @param state The state of the board (including Field & Graveyard).
	 * @return the score of the board for some playerColor. The higher the score, the better the board state is for that player.
	 */
	public Integer calculateScore(Color playerColor, String state) {
		return null;
	}
	
	public void printBoard(String state) {
		String[] splitState = state.split(" . ");
		String field_raw = splitState[0];
		String[] field = field_raw.split(" ");
		
		int i = 0;
		for(int y=4; y>0; y--) {
			for(int x=1; x<9; x++) {
				//System.out.print("[" + x + "," + y + "] ");
				System.out.print(field[i] + " ");
				i++;
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		String state = "B1D R1D B2D R5D R3D R1D R5D R7D R3D B1D B6D B5D R1D B4D R2D B1D B2D B1D B3D R2D R1D R6D B7D R4D B4D B3U B5U R6U XXX B3D XXX R2U . B6U";
		AI ai = new AI(Color.RED);
		ArrayList<int[][]> allOptions = ai.validMoves(state);
		
		for(int[] moveSet) {
			
		}
		
		int[] bestMove = ai.pickBestMove(allOptions, state);
		System.out.println("Best Move:");
		System.out.println("[" + bestMove[0] + "," + bestMove[1] + " | " + bestMove[2] + "," + bestMove[3] + "]");
		ai.printBoard(state);
	}
}