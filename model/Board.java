package edu.colostate.cs.cs414.banqi.model;
import java.util.ArrayList;
import java.util.Collections;

public class Board {

	private ArrayList<Token> tokens;
	private ArrayList<Tile> tiles;
	private ArrayList<Token> graveyard;
	
	public Board() {
		tokens = new ArrayList<Token>();
		tiles = new ArrayList<Tile>();
		graveyard = new ArrayList<Token>();
		setBoard();
	}
  
  public Board(String string) {
		tokens = new ArrayList<Token>();
		tiles = new ArrayList<Tile>();
		graveyard = new ArrayList<Token>();
		loadBoard(string);

	}
	

	public ArrayList<Token> getGraveyard(){
		return graveyard;
  }

	
	
	public Token getToken(int x, int y) {
		for(int i=0; i<tiles.size(); i++) {
			if(tiles.get(i).getPosition()[0] == x) {
				if(tiles.get(i).getPosition()[1] == y) {
					return tiles.get(i).getToken();
				}
			}
		}
		System.out.println("Token not on board");
		return null;
	}
	
	public Tile getTile(int x, int y) {
		for(int i=0; i<tiles.size(); i++) {
			if(tiles.get(i).getPosition()[0] == x) {
				if(tiles.get(i).getPosition()[1] == y) {
					return tiles.get(i);
				}
			}
		}
		System.out.println("Error finding tile");
		return null;
	}
	
	public void createToken(Type type, Color color, int numOfTokens) {
		for(int i=0; i<numOfTokens; i++) {
			tokens.add(new Token(type, color));
		}
	}
	
	public void createTokenSet(Color _color) {
		Color color = _color;
		Type type = Type.GENERAL;
		createToken(type, color, 1);
		type = Type.ADVISOR;
		createToken(type, color, 2);
		type = Type.ELEPHANT;
		createToken(type, color, 2);
		type = Type.CHARIOT;
		createToken(type, color, 2);
		type = Type.HORSE;
		createToken(type, color, 2);
		type = Type.CANNON;
		createToken(type, color, 2);
		type = Type.SOLDIER;
		createToken(type, color, 5);
	}
	
	public void createAllTokens() {
		createTokenSet(Color.RED);
		createTokenSet(Color.BLACK);
	}
	
	public void createTiles() {

		for(int y=4; y>0; y--) {
			for(int x=1; x<9; x++) {
				tiles.add(new Tile(x, y));
			}
		}
	}
	
	public void setBoard() {
		createAllTokens();
		createTiles();
		Collections.shuffle(tokens);
		for(int i=0; i<tokens.size(); i++) {
			tiles.get(i).setToken(tokens.get(i));
		}
	}
	
	public void resetBoard() {
		tokens.clear();
		tiles.clear();
		createAllTokens();
		createTiles();
		Collections.shuffle(tokens);
		for(int i=0; i<tokens.size(); i++) {
			tiles.get(i).setToken(tokens.get(i));
		}
	}
	

	public void presetBoard() {
		for(int i=0; i<tokens.size(); i++) {
			tiles.get(i).setToken(tokens.get(i));
		}
	}
	
	public void moveToGraveyard(Token token) {
		graveyard.add(token);
		tokens.remove(token);
		for(int i=0; i<tiles.size(); i++) {
			if(tiles.get(i).getToken() == token) {
				tiles.get(i).setToken(null);
			}
		}
	}
	
	public String saveBoard() {
		String output = "";
		Token token;
		for(int i=0; i<tiles.size(); i++) {
			if(tiles.get(i).getToken() == null) {
				output += "XXX ";
			}else {
				token = tiles.get(i).getToken();
				output += token.abbreviate();
				if(token.isFaceUp()) {
					output += "U ";
				}else output += "D ";
			}
		}
		
		output += ". ";
		for(int i=0; i<graveyard.size(); i++) {
			token = graveyard.get(i);
			output += token.abbreviate();
			if(token.isFaceUp()) {
				output += "U ";
			}else output += "D ";
		}
		
		return output;
	}
	
	public void loadBoard(String string) {
		String[] graveyardSplit = string.split("\\.");
		String[] graveyardString = graveyardSplit[1].split(" ");
		String[] split = graveyardSplit[0].split(" ");
		Color color;
		Type type;
		boolean isFaceUp;
		Token token;
		
		tokens.clear();
		tiles.clear();
		createTiles();
		
		for(int i=0; i<split.length; i++) {
			if(split[i].equals("XXX")) {
				continue;
			}
			
			if(split[i].charAt(0) == 'B') {
				color = Color.BLACK;
			}else color = Color.RED;
			
			int temp = split[i].charAt(1);
			switch(temp) {
				case '7': type = Type.GENERAL; break;
				case '6': type = Type.ADVISOR; break;
				case '5': type = Type.ELEPHANT; break;
				case '4': type = Type.CHARIOT; break;
				case '3': type = Type.HORSE; break;
				case '2': type = Type.CANNON; break;
				case '1': type = Type.SOLDIER; break;
				default: type = null;
			}
			
			if(split[i].charAt(2) == 'U') {
				isFaceUp = true;
			}else isFaceUp = false;
			
			token = new Token(type, color);
			if(isFaceUp) {
				token.flipToken();
			}
			
			tokens.add(token);
			tiles.get(i).setToken(token);
		}
		
//		for(int j=0; j<graveyardString.length; j++) {
//			if(graveyardString[j].charAt(0) == 'B') {
//				color = Color.BLACK;
//			}else color = Color.RED;
//			
//			int temp = graveyardString[j].charAt(1);
//			switch(temp) {
//				case '7': type = Type.GENERAL; break;
//				case '6': type = Type.ADVISOR; break;
//				case '5': type = Type.ELEPHANT; break;
//				case '4': type = Type.CHARIOT; break;
//				case '3': type = Type.HORSE; break;
//				case '2': type = Type.CANNON; break;
//				case '1': type = Type.SOLDIER; break;
//				default: type = null;
//			}
//			
//			if(graveyardString[j].charAt(2) == 'U') {
//				isFaceUp = true;
//			}else isFaceUp = false;
//			
//			token = new Token(type, color);
//			TODO THIS MAY BE WHERE THE ERROR HAPPENS!
//			if(isFaceUp) {
//				token.flipToken();
//			}
//			
//			graveyard.add(token);
//		}
	}

	public void printBoard() {
		int i = 0;

		for(int y=4; y>0; y--) {
			for(int x=1; x<9; x++) {
				if(tiles.get(i).getToken() == null) {
					System.out.print("-- ");
				}else if(!tiles.get(i).getToken().isFaceUp()){
					System.out.print("[] ");
				}else {
					System.out.print(tiles.get(i).getToken().abbreviate() + " ");
				}
				i++;
			}
			System.out.println();
		}
		System.out.println();
	}

	public void printGraveyard() {
		System.out.print("Graveyard: ");
		for(int i=0; i<graveyard.size(); i++) {
			System.out.print(graveyard.get(i).abbreviate() + " ");
		}
		System.out.println();
	}
}
