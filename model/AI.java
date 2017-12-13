import java.util.Random;

import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AI {
	

	Ecolor color;
	Game game;
	
	String mainQfile = "qMap1game.map";

	boolean debug = false;

	
	public AI(Game game, Ecolor color) {
		this.color = color;
		this.game = game;
	}
	
	public AI(){
		
	}
	
	
	public void setColor(Ecolor color) {
		this.color = color;
	}
	
	public Ecolor getColor() {
		return color;
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
		Ecolor thisColor = null;
		Ecolor otherColor = null;
		
		if(thisLetter == 'R') {
			thisColor = Ecolor.RED;
		}else if(thisLetter == 'B') {
			thisColor = Ecolor.BLACK;
		}
		
		if(otherLetter == 'R') {
			otherColor = Ecolor.RED;
		}else if(otherLetter == 'B') {
			otherColor = Ecolor.BLACK;
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
		String[] stateSplit = state.split(" . ");
		String[] field = null;
		String graveyard = null;

		if(stateSplit.length == 2) {
			field = stateSplit[0].split(" ");
			graveyard = stateSplit[1];
		}else {
			field = stateSplit[0].split(" ");
			graveyard = "";
		}
		int indexFirst = getIndex(move[0], move[1]);
		int indexSecond = getIndex(move[2], move[3]);
		
		//make a newState
		String newState = "";
		
		if(move[0] == move[2] && move[1] == move[3]) {
			//we are flipping, not moving
			field[indexFirst] = field[indexFirst].substring(0, 2) + "U";
		}else {
			//replace the token at (move[2], move[3]) with the token that was at (move[0], move[1])...
			//...and then replace the token at (move[0], move[1]) with XXX
			field[indexSecond] = field[indexFirst];
			field[indexFirst] = "XXX";
		}
		
		//set everything up
		for(String token : field) {
			newState += token + " ";
		}
		newState += " . " + graveyard;
		//then return the newState onto which that move was made
		return newState;
	}

	
	public int[] negamax(String state, int depthLimit){
		Double alpha = Double.NEGATIVE_INFINITY;
		Double beta = Double.POSITIVE_INFINITY;
		int bestScore = -1000;
		int[] bestMove = new int[]{-1, -1, -1, -1};
		
		for (int depth=0; depth<depthLimit; depth++) {
			//Recursive Call
			int[][] output = negamaxHelper(state, depth, alpha, beta);
			int score = output[0][0];
			int[] move = output[1];
			
			if(bestScore == -1000 || score > bestScore) {
				bestScore = score;
				bestMove = move;
				if(game.isOver()) {
					return bestMove;
				}
			}
		}
		
		//looks at the value of each state made by using each of the flips, choosing the flip that leads to the highest scoring state
		int[][] flips = validMoves(state).get(1);
		if(flips.length > 0 && (bestMove == null || (bestMove[0] == 0 && bestMove[1] == 0 && bestMove[2] == 0 && bestMove[3] == 0))) {
			bestMove = flips[0];
			bestScore = 0;
			for(int[] flip : flips) {
				int tempScore = calculateScore(getColor(), makeMove(flip, state));
				if(tempScore > bestScore) {
					bestScore = tempScore;
					bestMove = flip;
				}
			}
		}
		
		if(bestMove[0] == 0 && bestMove[1] == 0 && bestMove[2] == 0 && bestMove[3] == 0) {
			ArrayList<int[][]> allValidMoves = validMoves(state);
			bestMove = allValidMoves.get(0)[new Random().nextInt(allValidMoves.get(0).length)];
		}
		
		return bestMove;
	}
	
	public int[][] negamaxHelper(String state, int depthLeft, Double alpha, Double beta) {
		int score = -1000;
		int[] move = new int[4];
		int[][] output = new int[2][4];
		
		if(game.isOver() || depthLeft == 0) {
			score = calculateScore(color, state);
			output[0][0] = score;
			output[1] = move;
			return output;
		}
		
		int bestScore = -1000;
		int[] bestMove = null;
		ArrayList<int[][]> allValidMoves = validMoves(state);
		int[][] flips = allValidMoves.get(1);
		int[][] allMoves = compileMoves(allValidMoves);
		
		for(int[] thisMove : allMoves) {
			String newState = makeMove(thisMove, state);
			
			output = negamaxHelper(newState, depthLeft-1, -beta, -alpha);
			score = output[0][0];
			//move = output[1];
			if(score == -1000) {
				continue;
			}
			score = -score;
			if(bestScore == -1000 || score > bestScore) {
				bestScore = score;
				bestMove = thisMove;
			}
			if(bestScore > alpha) {
				alpha = (double) bestScore;
			}
			if(bestScore >= beta) {
				break;
			}
		}
		output[0][0] = bestScore;
		output[1] = bestMove;
		
		return output;
	}
	
	private int[][] compileMoves(ArrayList<int[][]> validMoves){
		int length = 0;
		length += validMoves.get(0).length;
		//length += validMoves.get(1).length;
		length += validMoves.get(2).length;
		
		int[][] allMoves = new int[length][4];
		int x = 0;
		for(int i=0; i<validMoves.get(0).length; i++) {
			allMoves[x] = validMoves.get(0)[i];
			x++;
		}
//		for(int i=0; i<validMoves.get(1).length; i++) {
//			allMoves[x] = validMoves.get(1)[i];
//			x++;
//		}
		for(int i=0; i<validMoves.get(2).length; i++) {
			allMoves[x] = validMoves.get(2)[i];
			x++;
		}
		
		return allMoves;
	}
	
	/**
	 * Returns the score of the board. The higher the score, the better the board is for the player recognized by playerColor.
	 * @param playerColor The color of the player for whom it calculates the score.
	 * @param state The state of the board (including Field & Graveyard).
	 * @return the score of the board for some playerColor. The higher the score, the better the board state is for that player.
	 */
	public Integer calculateScore(Ecolor playerColor, String state) {
		char color = 'R';
		if(playerColor == Ecolor.BLACK) {
			color = 'B';
		}
		
		//the score is the addition of all of the power levels of my tokens and the subtraction of all of the power levels of his tokens
		String field = state.split(" . ")[0];
		
		Integer score = 0;
		
		int mySoldierCounter = 0;
		int hisSoldierCounter = 0;
		int myGeneralCounter = 0;
		int hisGeneralCounter = 0;
		
		for(String token : field.split(" ")) {
			//add to score the power level of my tokens
			
			if(token.equals("XXX")) {
				continue;
			}
			
			if(token.charAt(0) == color) {
				score += Character.getNumericValue(token.charAt(1));
			}
			//subtract from score the power level of his tokens
			if(token.charAt(0) != color) {
				score -= Character.getNumericValue(token.charAt(1));
			}
			
			//for each cannon of mine, increase my score and for each cannon of my opponent, decrease my score
			if(token.charAt(1) == '2' && token.charAt(0) == color) {
				score += 6;
			}else if(token.charAt(1) == '2' && token.charAt(0) != color) {
				score -= 6;
			}
			
			if(token.charAt(1) == '1' && token.charAt(0) == color) {
				mySoldierCounter++;
			}else if(token.charAt(1) == '1' && token.charAt(0) != color) {
				hisSoldierCounter++;
			}
			
			if(token.charAt(1) == '7' && token.charAt(0) == color) {
				myGeneralCounter++;
			}else if(token.charAt(1) == '7' && token.charAt(0) != color) {
				hisGeneralCounter++;
			}
			
			//face up tokens of mine are more valuable than face down tokens of mine
			if(token.charAt(2) == 'U' && token.charAt(0) == color) {
				score++;
			}
			
			//face up tokens of my opponents is a bad thing
			if(token.charAt(2) == 'U' && token.charAt(0) != color) {
				score--;
			}
		}

		//if I  have as many or more soldiers than he does generals, increase my score
		if(mySoldierCounter >= hisGeneralCounter) {
			score += 15;
		}
		//if he has as many or more soldiers than i have generals, decrease my score
		if(hisSoldierCounter >= myGeneralCounter) {
			score -= 15;
		}
		
		return score;
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
	
	/**
	
	public static void main(String[] args) {

		ArrayList<Integer> blackScores = new ArrayList<Integer>();
		ArrayList<Integer> redScores = new ArrayList<Integer>();
			
		Game game = new Game();
		
		AI ai = new AI(game, Ecolor.RED);
		
		String state = game.getBoard().saveBoard();
		ai.printBoard("State:\n" + state);

		System.out.println("\n\n");
		
		int moveCounter = 0;
		while(state.split(" . ")[0].contains("R") && state.split(" . ")[0].contains("B")) {
//			ArrayList<int[][]> moves = ai.validMoves(state);
//			int[][] chosenMoves = moves.get((new Random()).nextInt(moves.size()));
//						
//			while(chosenMoves.length <= 0) {
//				moves.remove(chosenMoves);
//				chosenMoves = moves.get((new Random()).nextInt(moves.size()));
//			}
//			
//			int[] chosenMove = chosenMoves[new Random().nextInt(chosenMoves.length)];
			
			int[] chosenMove = ai.negamax(state, 4);
			
			int depthLimit = 5;
			while(chosenMove[0] == 0 && chosenMove[1] == 0 && chosenMove[2] == 0 && chosenMove[3] == 0) {
				System.out.println("Now trying with a depth limit of " + depthLimit + " for player " + ai.getColor());
				chosenMove = ai.negamax(state, depthLimit);
				depthLimit++;
			}
			
			System.out.println("Chosen move:\n" + Arrays.toString(chosenMove));
			
			state = ai.makeMove(chosenMove, state);
			
			ai.printBoard("State:\n" + state);
			System.out.println("Value for Red: " + ai.calculateScore(Ecolor.RED, state));
			redScores.add(ai.calculateScore(Ecolor.RED, state));
			System.out.println("Value for Black: " + ai.calculateScore(Ecolor.BLACK, state));
			blackScores.add(ai.calculateScore(Ecolor.BLACK, state));
			
			moveCounter++;
			
			System.out.println("\n\n");
			
			if(ai.getColor() == Ecolor.RED) {
				ai.setColor(Ecolor.BLACK);
			}else {
				ai.setColor(Ecolor.RED);
			}
		}
		
		String victor = "black";
		if(state.split(" . ")[0].contains("R")) {
			victor = "red";
		}
		
		System.out.println("With " + moveCounter + " moves, " + victor + " is the victor!");
		
		ArrayList<Integer> newRedScores = new ArrayList<Integer>(redScores);
		Integer lastScore = null;
		for(Integer score : redScores) {
			if(lastScore == null) {
				lastScore = score;
				continue;
			}else {
				if(lastScore == score) {
					newRedScores.remove(lastScore);
				}
				lastScore = score;
			}
		}
		
		ArrayList<Integer> newBlackScores = new ArrayList<Integer>(blackScores);
		lastScore = null;
		for(Integer score : blackScores) {
			if(lastScore == null) {
				lastScore = score;
				continue;
			}else {
				if(lastScore == score) {
					newBlackScores.remove(lastScore);
				}
				lastScore = score;
			}
		}
		
		System.out.println("Red Scores:   " + newRedScores.toString());
		System.out.println("Black Scores: " + newBlackScores.toString());
		
	}
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	*/
	
	
	//begin Qtable
	
	//test main comment out if not needed
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		Game game = new Game();
		AI ai = new AI(game, Ecolor.RED);
		
		ai.Qtrials();
	}
	
	
	/**
	 * 
	 * save Qtable
	 * @throws IOException 
	 * 
	 * citation: https://stackoverflow.com/questions/3347504/how-to-read-and-write-a-hashmap-to-a-file
	 * 
	 */
	private void saveQ(Map<Pair<String, String>, Double> qMap, String fileName) throws IOException{
		File file = new File(fileName);
		FileOutputStream f = new FileOutputStream(file,false);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(qMap);
		s.flush();
	}
	
	/**
	 * load Qtable
	 * 
	 * citation: https://stackoverflow.com/questions/3347504/how-to-read-and-write-a-hashmap-to-a-file
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	public Map<Pair<String, String>, Double> loadQ(String fileName) throws IOException, ClassNotFoundException{
		
		File file = new File(fileName);
		FileInputStream f = new FileInputStream(file);
		ObjectInputStream s = new ObjectInputStream(f);
		@SuppressWarnings("unchecked")
		Map<Pair<String, String>, Double> qMap = (Map<Pair<String, String>, Double>)s.readObject();
		s.close();
		return qMap;
	}
	
	/**
	 * train
	 */
	private void trainQ(int numGames, double learningRate, double epsilonDecayFactor, Game game){
		int redW=0;
		int blackW=0;
		String data="For "+numGames+"\n";
		boolean space=true;
		double rho = learningRate;
		String mapFileName = mainQfile;
		Map<Pair<String,String>, Double> Q = new HashMap<Pair<String,String>, Double>();
		try {
			Q=loadQ(mapFileName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("class Not found!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("File: "+mapFileName+" not found.");
			e.printStackTrace();
		}
		double epsilon = epsilonDecayFactor;
		boolean done=false;
		
		data+="starting map size of: "+Q.size()+"\n";
		data+="#format redWins:blackWins:winLossRatio:steps for every 100 games.\n";
		
		//play many games
		for(int i=0;i<numGames;i++){
			char Qcolor='R';
			char nmColor='B';
			epsilon *= epsilonDecayFactor;
			//Game mainGame = new Game();
			String state = game.getBoard().saveBoard();
			int step=0;
			
			//play a game
			while(!gameOver(state)){
				step++;
				String stateNew= new String(state);
				
				ArrayList<int[][]> validMovesList = validMovesColor(state,Qcolor);
				int[][] allMoves = compileAllMoves(validMovesList);
				//int[][] allMoves = qValidMoves(state,Qcolor);
				
				if(allMoves.length==0){
					break;
				}
				
				int[] move = epsilonGreedy(epsilon, Q, state, Qcolor, allMoves);
				String stateOld=state;
				int[] moveOld=move;
				stateNew=makeMove(move,stateNew);
				if(!Q.containsKey(makeStateMovePair(state,move))){
					Q.put(makeStateMovePair(state,move), 0.0);
				}
				Q.put(makeStateMovePair(state,move), Q.get(makeStateMovePair(state,move))+(double) score('R',stateNew));
				
				int[] nmMove=negamax(state,4);
				stateNew = makeMove(nmMove,stateNew);
				Q.put(makeStateMovePair(state,move), rho * (Q.get(makeStateMovePair(state,move))+(double)score('R',stateNew)) );
				if(step>1){
					Q.put(makeStateMovePair(stateOld,moveOld), Q.get(makeStateMovePair(stateOld,moveOld))+(rho * (Q.get(makeStateMovePair(state,move))-Q.get(makeStateMovePair(stateOld,moveOld))) ));
				}
				
				state=stateNew;
				
				
			}
			//printBoard(state);
			if(space){
				//printBoard(state);
				System.out.println(" "+winner);
				space=false;
			}else{
				//printBoard(state);
				System.out.println(winner);
				space=true;
			}
			if(winner.equals("red")){
				redW++;
			}else{
				blackW++;
			}
			if(i%100==0&&i!=0){
				data+=redW+":"+blackW+":"+redW/(redW+blackW)+":"+step+"\n";
				redW=0;
				blackW=0;
				System.out.println(numGames-i+" games remaining.");
			}
			
		}
		
		try {
			saveQ(Q,mapFileName);
			saveData(data,numGames+"GameData.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not make file: "+mapFileName);
			e.printStackTrace();
		}
		
		
	}
	
	private Pair<String, String> makeStateMovePair(String state, int[] move){
		String sMove="";
		for(int i=0;i<move.length;i++){
			sMove+=move[i];
		}
		
		
		return new Pair<String, String>(state,sMove);
	}
	
	private String winner;
	
	private boolean gameOver(String state){
		int red=0;
		int black=0;
		String field = state.split(" . ")[0];
		for(String token : field.split(" ")) {
			if(token.equals("XXX")) {
				continue;
			}
			if(token.charAt(0) == 'R') {
				red++;
			}
			if(token.charAt(0) == 'B') {
				black++;
			}
		}
		
		if(red==0){
			winner="black";
			return true;
		}else if(black==0){
			winner="red";
			return true;
		}else{
			return false;
		}
		
		
	}
	
	//begin epsilonGreedy
	//returns one move
	private int[] epsilonGreedy(double epsilon, Map<Pair<String, String>, Double> Q, String state, char color,int[][] allMoves){
		Random random = new Random();
		
		//ArrayList<int[][]> validMovesList = validMovesColor(state,color);
		//int[][] allMoves = compileAllMoves(validMovesList);
		
		//int[][] allMoves = qValidMoves(state, color);
		
		
		
		
		
		//System.out.println(allMoves.length);
		if(allMoves.length==0){
			printBoard(state);
		}
		
		
		
		if(Math.random()<epsilon){
			//int randomNumber = random.nextInt(max + 1 - min) + min;
			int randomMove = random.nextInt(allMoves.length);
			return allMoves[randomMove];
			
		}else{
			int length = allMoves.length;
			double[] Qscore= new double[length];
			
			for(int i=0;i<length;i++){
				if(Q.containsKey(makeStateMovePair(state,allMoves[i]))){
					Qscore[i]=Q.get(makeStateMovePair(state,allMoves[i]));
				}else{
					Qscore[i]=0;
				}
			}
			
			//find max index
			double maxVal = Qscore[0];
			int maxIndex = 0;
			for(int i=0;i<length;i++){
				if(Qscore[i]>maxVal){
					maxVal=Qscore[i];
					maxIndex=i;
				}
			}
			return allMoves[maxIndex];	
		}
	}
	//end epsilon greedy
	
	private int score(char color, String state){
		int red=52;
		int black=52;
		String field = state.split(" . ")[0];
		for(String token : field.split(" ")) {
			if(token.equals("XXX")) {
				continue;
			}
			if(token.charAt(0) == 'R') {
				red-=(int)token.charAt(1);
			}
			if(token.charAt(0) == 'B') {
				black-=(int)token.charAt(1);
			}
		}
		if(color=='R'){
			return black-red;
		}else if(color=='B'){
			return red-black;
		}else{
			return 0;
		}
	}
	
	public int[] Qmove(String state, char color, Map<Pair<String,String>, Double> Q){
		
		
		Random random = new Random();
		ArrayList<int[][]> validMovesList = validMovesColor(state,color);
		int[][] allMoves = compileAllMoves(validMovesList);
		//int[][] allMoves = qValidMoves(state,color);
		int length = allMoves.length;
		if(length==0){
			System.out.println("allMoves=0!");
		}
		double[] Qscore= new double[length];
		
		for(int i=0;i<length;i++){
			if(Q.containsKey(makeStateMovePair(state,allMoves[i]))){
				Qscore[i]=Q.get(makeStateMovePair(state,allMoves[i]));
			}else{
				Qscore[i]=0;
			}
		}
		
		//find max index
		double maxVal = Qscore[0];
		int maxIndex = 0;
		for(int i=0;i<length;i++){
			if(Qscore[i]>maxVal){
				maxVal=Qscore[i];
				maxIndex=i;
			}
		}
		if(maxVal==0){
			System.out.println("Qmove makeing random move.");
			int randomMove = random.nextInt(allMoves.length);
			return allMoves[randomMove];
		}else{
			return allMoves[maxIndex];
		}
		
		
		
		
	}
	
	private void startMapFile(){

		Map<Pair<String,String>, Double> Q = new HashMap<Pair<String,String>, Double>();
		try {
			saveQ(Q,mainQfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not make map file.");
			e.printStackTrace();
		}
	}
	
	private void saveData(String data, String fileName) throws IOException{
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			out.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    out.close();
	}
	
	private void testQ(){
		
	}
	
	
	public void Qtrials() throws IOException, ClassNotFoundException{
		//startMapFile();
		
		String masterState = "R4D R2D R2D B1D R5D B1D B6D R1D B4D R7D R3D B6D B4D B3D R4D B1D B2D R6D B1D B3D B2D R1D B5D B1D R1D R3D B7D B5D R1D R1D R6D R5D . ";
		String mapFileName = "qMap1game.map";
		
		Game game = new Game(masterState);
		
		//System.out.println(game.getBoard().saveBoard());
		
		
		trainQ(5000,0.2,.99999999999,game);
		Map<Pair<String,String>, Double> Q = new HashMap<Pair<String,String>, Double>();
		
		Q=loadQ("qMap1game2.map");
		
		int[] move = {1,1,1,1};
		
		//Q.put(makeStateMovePair(masterState,move), 0.0);
		
		//saveQ(Q,mapFileName);
		//System.out.println(Q);
		System.out.println(Q.toString());
		
		
		
		
		
	}
	
	//end Qtable 
	
	
	private int[][] moveSpace(int[] tokenPos){
		int x = tokenPos[0];
		int y = tokenPos[1];
		
		ArrayList<int[]> moves = new ArrayList<int[]>();
		if(x>0){
			int[] move ={x-1,y};
			moves.add(move);
		}
		if(x<7){
			int[] move ={x+1,y};
			moves.add(move);
		}
		if(y>0){
			int[] move = {x,y-1};
			moves.add(move);
		}
		if(y<3){
			int[] move = {x,y+1};
			moves.add(move);
		}
		
		//Integer[] arr = new Integer[al.size()];
        //arr = al.toArray(arr);
		
		int[][] movesArr = new int[moves.size()][2];
		movesArr = moves.toArray(movesArr);
		
		return movesArr;
		
	}
	
	private String[][] stateToGrid(String state){
		String[][] gridState = new String[4][8];
		String field = state.split(" . ")[0];
		int index=0;
		for(String token : field.split(" ")) {
			gridState[index/8][index%8]=token;
			index++;
		}
		
		return gridState;
	}
	
	//qValidMovesStart-------------------------------------------------------------------------------------------------------------------
	private int[][] qValidMoves(String state, char color){
		char oColor = 'B';
		if(color==('B')){
			oColor='R';
		}
		String[][] gridState=stateToGrid(state);
		ArrayList<int[]> moves = new ArrayList<int[]>();
		ArrayList<int[]> myTokens = new ArrayList<int[]>();
		for(int y=0;y<4;y++){
			for(int x=0;x<8;x++){
				if(gridState[y][x].charAt(2)==('D')){
					int[] move = {x+1,y+1,x+1,y+1};
					moves.add(move);
				}else if(gridState[y][x].charAt(2)==('U')&&gridState[y][x].charAt(0)==(color)){
					int[] tokenPos ={x,y};
					myTokens.add(tokenPos);
				}
			}
		}
		//go through each token and add possible valid moves
		for(int[] tokenPos : myTokens){
			String token = gridState[tokenPos[1]][tokenPos[0]];
			char power = token.charAt(1);
			int[][] moveSpace = moveSpace(tokenPos);
			if(power==('1')){
				//soldier
				for(int[] pMove : moveSpace){
					if(gridState[pMove[1]][pMove[0]].charAt(1)==('X')||(gridState[pMove[1]][pMove[0]].charAt(2)==('U')&&gridState[pMove[1]][pMove[0]].charAt(0)==oColor&&(int)gridState[pMove[1]][pMove[0]].charAt(1)==7)){
						int[] move = {tokenPos[0]+1,tokenPos[1]+1,pMove[0]+1,pMove[1]+1};
						moves.add(move);
					}
				}
			}else if(power==('2')){
				//cannon
				//simple move
				for(int[] pMove : moveSpace){
					if(gridState[pMove[1]][pMove[0]].charAt(1)==('X')){
						int[] move = {tokenPos[0]+1,tokenPos[1]+1,pMove[0]+1,pMove[1]+1};
						moves.add(move);
					}
				}
				//vertical attack down
				if(tokenPos[1]<2){
					int jumped =0;
					int y=tokenPos[1];
					y++;
					while(y<=3){
						if(gridState[y][tokenPos[0]].charAt(2)!=('X')){
							if(jumped==1&&gridState[y][tokenPos[0]].charAt(2)==('U')&&gridState[y][tokenPos[0]].charAt(0)==oColor){
								int[] move = {tokenPos[0]+1,tokenPos[1]+1,tokenPos[0]+1,y+1};
								moves.add(move);
							}
							jumped++;
						}
						y++;
					}	
				}
				//vertical attack up
				if(tokenPos[1]>1){
					int jumped =0;
					int y=tokenPos[1];
					y--;
					while(y>=0){
						if(gridState[y][tokenPos[0]].charAt(2)!=('X')){
							if(jumped==1&&gridState[y][tokenPos[0]].charAt(2)==('U')&&gridState[y][tokenPos[0]].charAt(0)==oColor){
								int[] move = {tokenPos[0]+1,tokenPos[1]+1,tokenPos[0]+1,y+1};
								moves.add(move);
							}
							jumped++;
						}
						y--;
					}
				}
				//horizontal attack right
				if(tokenPos[0]<6){
					int jumped=0;
					int x=tokenPos[0];
					x++;
					while(x<=7){
						if(gridState[tokenPos[1]][x].charAt(2)==('X')){
							if(jumped==1&&gridState[tokenPos[1]][x].charAt(2)==('U')&&gridState[tokenPos[1]][x].charAt(0)==oColor){
								int[] move = {tokenPos[0]+1,tokenPos[1]+1,x+1,tokenPos[1]+1};
								moves.add(move);
							}
							jumped++;
						}
						x++;
					}
				}
				
				//horizontal attack left
				if(tokenPos[0]>1){
					int jumped=0;
					int x=tokenPos[0];
					x--;
					while(x<=0){
						if(gridState[tokenPos[1]][x].charAt(2)==('X')){
							if(jumped==1&&gridState[tokenPos[1]][x].charAt(2)==('U')&&gridState[tokenPos[1]][x].charAt(0)==oColor){
								int[] move = {tokenPos[0]+1,tokenPos[1]+1,x+1,tokenPos[1]+1};
								moves.add(move);
							}
							jumped++;
						}
						x--;
					}
				}
				
			}else if(power==('7')){
				//general
				for(int[] pMove : moveSpace){
					if(gridState[pMove[1]][pMove[0]].charAt(1)==('X')||(gridState[pMove[1]][pMove[0]].charAt(2)==('U')&&gridState[pMove[1]][pMove[0]].charAt(0)==oColor&&(int)gridState[pMove[1]][pMove[0]].charAt(1)>1)){
						int[] move = {tokenPos[0]+1,tokenPos[1]+1,pMove[0]+1,pMove[1]+1};
						moves.add(move);
					}
				}
			}else{
				//all others
				for(int[] pMove : moveSpace){
					if(gridState[pMove[1]][pMove[0]].charAt(1)==('X')||(gridState[pMove[1]][pMove[0]].charAt(2)==('U')&&gridState[pMove[1]][pMove[0]].charAt(0)==oColor&&(int)gridState[pMove[1]][pMove[0]].charAt(1)<=(int)power)){
						int[] move = {tokenPos[0]+1,tokenPos[1]+1,pMove[0]+1,pMove[1]+1};
						moves.add(move);
					}
				}
				
			}
		}
		
		
		int[][] movesArr = new int[moves.size()][4];
		movesArr = moves.toArray(movesArr);
		
		return movesArr;
	}
	
	
	//qValidMovesEnd
	
	//new valid moves
	/**
	 * For some given string state, returns a list of all moves possible. A move is an integer array of 4 numbers: {x1, y1, x2, y2}.
	 * @param state = "FIELD . GRAVEYARD"
	 * @return An ArrayList containing moves[][], flips[][], and attacks[][]
	 */
	public ArrayList<int[][]> validMovesColor(String state,char color){
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
			
			if(token.charAt(2) == ('U')&&token.charAt(0) ==color) {
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
	
	//compile all moves
	private int[][] compileAllMoves(ArrayList<int[][]> validMoves){
		int length = 0;
		length += validMoves.get(0).length;
		length += validMoves.get(1).length;
		length += validMoves.get(2).length;
		
		int[][] allMoves = new int[length][4];
		int x = 0;
		for(int i=0; i<validMoves.get(0).length; i++) {
			allMoves[x] = validMoves.get(0)[i];
			x++;
		}
		for(int i=0; i<validMoves.get(1).length; i++) {
			allMoves[x] = validMoves.get(1)[i];
			x++;
		}
		for(int i=0; i<validMoves.get(2).length; i++) {
			allMoves[x] = validMoves.get(2)[i];
			x++;
		}
		
		return allMoves;
	}
	
	
}