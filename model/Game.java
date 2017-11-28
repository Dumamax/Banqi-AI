package edu.colostate.cs.cs414.banqi.model;
import java.util.ArrayList;
import java.util.Arrays;

public class Game {

	private Status status;
	private Color winningColor;
	private String winningPlayer;
	private String losingPlayer;
	private String currentPlayer;
	private Color currentColor;
	private Color creatorColor;
	private ArrayList<String> players;
	private Board board;
    private String gameID;
    private boolean isFirstMove;
	
	public Game() {
		status = Status.ACTIVE;
		board = new Board();
		players = new ArrayList<String>();
		isFirstMove = true;
	}
	
	public Game(String _board) {
		status = Status.ACTIVE;
		board = new Board(_board);
		players = new ArrayList<String>();
	}
	
	public String getCreatorColorAsString() {
		if(getCreatorColor() == Color.RED) {
			return "R";
		}else {
			return "B";
		}
	}
	
	public Color getCreatorColor() {
		return creatorColor;
	}
	
	public void setCreatorColor(Color color) {
		creatorColor = color;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status _status) {
		status = _status;
	}
	
	public String getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setCurrentPlayer(String player) {
		currentPlayer = player;
	}
	
	public Color getCurrentColor() {
		return currentColor;
	}
	
	public void setCurrentColor(Color color) {
		currentColor = color;
	}
	
	public ArrayList<String> getPlayers(){
		return players;
	}
	
	public void setPlayers(String player1, String player2) {
		players.clear();
		players.add(player1);
		players.add(player2);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public void setBoard() {
		board.resetBoard();
	}
	
	public String getWinningPlayer() {
		return winningPlayer;
	}

    public String getLosingPlayer() {
        return losingPlayer;
    }    

	public Color getWinningColor() {
		return winningColor;
	}
    
    public String getGameID() {
        return gameID;
    }    
    
    public void setGameID(String gameID) {
    	this.gameID = gameID;
    }
    
    
	//Game actions and logic starts here
	public void switchPlayer() {
		if(currentPlayer == players.get(0)) {
			currentPlayer = players.get(1);
		}else if(currentPlayer == players.get(1)) {
			currentPlayer = players.get(0);
		}else {
			System.out.println("currentPlayer is not one of this game's players");
		}
		
		switchColor();
	}
	
	public void switchColor() {
		if(currentColor == Color.RED) {
			currentColor = Color.BLACK;
		}else if(currentColor == Color.BLACK) {
			currentColor = Color.RED;
		}
	}
	
	public void flipToken(int x, int y) {
		if(!board.getToken(x,y).isFaceUp()) {
			board.getToken(x,y).flipToken();
		}else {
			System.out.println("Token is already face-up!");
		}
	}
	

	public boolean moveToken(String username, int startX, int startY, int endX, int endY) {
		System.out.println("currentPlayer=<"+currentPlayer+">  username=<"+username+">");
		if(!username.equals(currentPlayer)) {
			return false;
		}
		
		Token token = board.getToken(startX, startY);
		Token token2 = board.getToken(endX, endY);
		if(token.isFaceUp()&&(token2==null||token2.isFaceUp())) {
			if(token.getColor() != currentColor) {
				return false;
			}
			
			if(isValidMove(startX, startY, endX, endY)) {
				if(token2!=null) board.moveToGraveyard(token2);
				board.getTile(endX, endY).setToken(token);
				board.getTile(startX, startY).setToken(null);
				switchPlayer();
				return true;
			}else return false;
		}else if(!token.isFaceUp()){
//			if(isFirstMove) {
//				isFirstMove = false;
//				creatorColor = board.getToken(startX, startY).getColor();
//				currentColor = creatorColor;
//			}
			flipToken(startX, startY);
			switchPlayer();
			return true;
		}else{
			return false;
		}
	}
	
	
	
	public boolean isValidMove(int startX, int startY, int endX, int endY) {
		Token token = board.getToken(startX, startY);
		if(board.getToken(endX, endY) == null) {
			
			if((Math.abs(endX-startX) == 1 && Math.abs(endY-startY) == 0) || 
			(Math.abs(endX-startX) == 0 && Math.abs(endY-startY) == 1)) {
				return true;
			}else{
				return false;
			}
			
		}else{
			Token token2 = board.getToken(endX, endY);
			if(isValidAttack(token, token2)) {
				if(token.getType()==Type.CANNON){
					return cannonAttack(startX,startY,endX,endY);
				}else if((Math.abs(endX-startX) == 1 && Math.abs(endY-startY) == 0) || 
						(Math.abs(endX-startX) == 0 && Math.abs(endY-startY) == 1)) {
					return true;
				} else return false;
			}else return false;
		}
	}
	
	
	
	private boolean cannonAttack(int startX, int startY, int endX, int endY){

		int jumped = 0;
		//check Y path
		if(startX==endX && Math.abs(startY-endY)>=2){
			if(startY<endY){
				for(int y=startY+1; y<endY; y++){//Y move down
					if(board.getToken(startX, y)!=null){
						jumped++;
					}
				}
			}else{
				for(int y=startY-1; y>endY; y--){//Y move up
					if(board.getToken(startX, y)!=null){
						jumped++;
					}
				}
			}
			
		//check X path
		}else if(startY==endY && Math.abs(startX-endX)>=2){
			
			if(startX<endX){
				for(int x=startX+1; x<endX; x++){//X move right
					if(board.getToken(x, startY)!=null){
						jumped++;
					}
				}
			}else{
				for(int x=startX-1; x>endX; x--){//X move left
					if(board.getToken(x, startY)!=null){
						jumped++;
					}
				}
			}
			
		}else return false;
		
		//System.out.println(jumped);
		if(jumped==1){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isValidAttack(Token token, Token token2) {
		if(token.getColor().equals(token2.getColor())) {
			return false;
		}
		if(token.getType() == Type.GENERAL && token2.getType() == Type.SOLDIER){
			return false;
		}
		if(token.getType() == Type.SOLDIER && token2.getType() == Type.GENERAL) {
				return true;
		}
		if(token.getType() == Type.CANNON) {
			return true;
		}
		if(token.value() >= token2.value()) {
				return true;
		}else{
			return false;
		}
	}

	/**
	 * Returns True if the game is won, making sure to set winningPlayer, losingPlayer, & winningColor all appropriately.
	 * @return
	 */
	public boolean isOver() {
		String state = board.saveBoard();
		boolean output = false;
		Color winningColor = null;
		
		String[] split = state.split("\\.");
		
		System.out.println("split:" + Arrays.toString(split));
		
		String board = split[0];
		String graveyard = split[1];
		
		if(!board.contains("R")) {
			//no red pieces, black wins
			winningColor = Color.RED;
			output = true;
		}else if(!board.contains("B")) {
			//no black pieces, red wins
			winningColor = Color.BLACK;
			output = true;
		}else {
			//there are both black and red pieces, game is not over
			return false;
		}
		
		String otherPlayer = "";
		
		for(String player : players) {
			if(!currentPlayer.equals(player)) {
				otherPlayer = player;
			}
		}
		
		Color colorOfWinningMove = Color.BLACK;
		if(currentColor == Color.BLACK) {
			colorOfWinningMove = Color.RED;
		}
		
		//currentPlayer is winner if currentColor = winningColor
		if(colorOfWinningMove == winningColor) {
			this.winningPlayer = currentPlayer;
			this.losingPlayer = otherPlayer;
		}else {
			this.winningPlayer = otherPlayer;
			this.losingPlayer = currentPlayer;
		}
		
		return output;
	}
	
//	public boolean isOver() {
//		String state = board.saveBoard();
//		String[] graveyardSplit = state.split("\\.");
//		if(!graveyardSplit[0].contains("R")) {
//			if(currentColor == Color.BLACK) {
//				winningColor = Color.BLACK;
//				winningPlayer = currentPlayer;
//				switchPlayer();
//				losingPlayer = currentPlayer;
//			}else{
//				losingPlayer = currentPlayer;
//				switchPlayer();
//				winningColor = Color.BLACK;
//				winningPlayer = currentPlayer;
//			}
//			return true;
//		}
//		else if(!graveyardSplit[0].contains("B")){
//			if(currentColor == Color.RED) {
//				winningColor = Color.RED;
//				winningPlayer = currentPlayer;
//				switchPlayer();
//				losingPlayer = currentPlayer;
//			}else{
//				losingPlayer = currentPlayer;
//				switchPlayer();
//				winningColor = Color.RED;
//				winningPlayer = currentPlayer;
//			}
//			return true;
//		}
//		else return false;
//	}
	
	public String getBoardWithColor() {
		String output = board.saveBoard();
		if(currentColor == Color.RED) {
			output += ". R";
		}
		else if(currentColor == Color.BLACK) {
			output += ". B";
		}else System.err.println("Problem getting currentColor!");
		
		return output;
	}
	
	public void setBoardWithColor(String string) {
		System.out.println(string);
		System.out.println(string.substring(0, string.length()-3));
		if(string.charAt(string.length()-1) == 'R') {
			System.out.println("SETTING CURRENT PLAYER AS RED");
			currentColor = Color.RED;
			board.loadBoard(string.substring(0, string.length()-3));
		}else if(string.charAt(string.length()-1) == 'B') {
			System.out.println("SETTING CURRENT PLAYER AS BLACK");
			currentColor = Color.BLACK;
			board.loadBoard(string.substring(0, string.length()-3));
		}else {
			System.out.println("SHOULDN'T EVER BE HERE");
		}
	}
	
	public String toString(){
		return players.get(0)+" vs. "+players.get(1);
	}
}
