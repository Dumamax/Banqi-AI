import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class AI {
	
	Color color;
	Game game;

	boolean debug = false;

	
	public AI(Game game, Color color) {
		this.color = color;
		this.game = game;
	}
	
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
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
			//Recurssive Call
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
		return bestMove;
	}
	
	public int[][] negamaxHelper(String state, int depthLeft, Double alpha, Double beta) {
		int score = -1000;
		int[] move = new int[4];
		int[][] output = new int[1][4];
		
		if(game.isOver() || depthLeft == 0) {
			score = calculateScore(color, state);
			output[0][0] = score;
			output[1] = move;
			return output;
		}
		
		int bestScore = -1000;
		int[] bestMove = null;
		int[][] allMoves = compileMoves(validMoves(state));
		
		for(int i=0; i<allMoves.length; i++) {
			String newState = makeMove(allMoves[i], state);
			
			output = negamaxHelper(newState, depthLeft-1, -beta, -alpha);
			score = output[0][0];
			move = output[1];
			if(score == -1000) {
				continue;
			}
			score = -score;
			if(bestScore == -1000 || score > bestScore) {
				bestScore = score;
				bestMove = move;
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
	
	/**
	 * Returns the score of the board. The higher the score, the better the board is for the player recognized by playerColor.
	 * @param playerColor The color of the player for whom it calculates the score.
	 * @param state The state of the board (including Field & Graveyard).
	 * @return the score of the board for some playerColor. The higher the score, the better the board state is for that player.
	 */
	public Integer calculateScore(Color playerColor, String state) {
		char color = 'R';
		if(playerColor == Color.BLACK) {
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
	
	public static void main(String[] args) {
		ArrayList<Integer> blackScores = new ArrayList<Integer>();
		ArrayList<Integer> redScores = new ArrayList<Integer>();
		
		AI ai = new AI(null, Color.RED);
		
		String state = new Game().getBoard().saveBoard();
		ai.printBoard("State:\n" + state);
		
		System.out.println("\n\n");
		
		int moveCounter = 0;
		while(state.split(" . ")[0].contains("R") && state.split(" . ")[0].contains("B")) {
			ArrayList<int[][]> moves = ai.validMoves(state);
			int[][] chosenMoves = moves.get((new Random()).nextInt(moves.size()));
						
			while(chosenMoves.length <= 0) {
				moves.remove(chosenMoves);
				chosenMoves = moves.get((new Random()).nextInt(moves.size()));
			}
			
			int[] chosenMove = chosenMoves[new Random().nextInt(chosenMoves.length)];
			
			System.out.println("Chosen move:\n" + Arrays.toString(chosenMove));
			
			state = ai.makeMove(chosenMove, state);
			
			ai.printBoard("State:\n" + state);
			System.out.println("Value for Red: " + ai.calculateScore(Color.RED, state));
			redScores.add(ai.calculateScore(Color.RED, state));
			System.out.println("Value for Black: " + ai.calculateScore(Color.BLACK, state));
			blackScores.add(ai.calculateScore(Color.BLACK, state));
			
			moveCounter++;
			
			System.out.println("\n\n");
			
			if(ai.getColor() == Color.RED) {
				ai.setColor(Color.BLACK);
			}else {
				ai.setColor(Color.RED);
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
}