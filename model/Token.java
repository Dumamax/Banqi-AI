
public class Token {
	
	private Type type;
	private Ecolor color;
	private Status status;
	private boolean isFaceUp;
	
	public Token(Type _type, Ecolor _color) {
		type = _type;
		color = _color;
		status = Status.ACTIVE;
		isFaceUp = false;
	}
	
	public Type getType() {
		return type;
	}
	
	public Ecolor getColor() {
		return color;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status _status) {
		status = _status;
	}
	
	public boolean isFaceUp() {
		return isFaceUp;
	}
	
	public void flipToken() {
		isFaceUp = !isFaceUp;
	}
	
	public String abbreviate() {
		String temp = "";
		
		if(color == Ecolor.RED) {
			temp += "R";
		}else if(color == Ecolor.BLACK) {
			temp += "B";
		}
		
		switch(type) {
			case GENERAL: temp += "7"; break;
			case ADVISOR: temp += "6"; break;
			case ELEPHANT: temp += "5"; break;
			case CHARIOT: temp += "4"; break;
			case HORSE: temp += "3"; break;
			case CANNON: temp += "2"; break;
			case SOLDIER: temp += "1"; break;
			default: System.out.println("Doesn't have a type");
		}
		
		return temp;
	}
	
	public int value() {
		switch(type) {
		case GENERAL: return 7;
		case ADVISOR: return 6;
		case ELEPHANT: return 5;
		case CHARIOT: return 4;
		case HORSE: return 3;
		case CANNON: return 2;
		case SOLDIER: return 1;
		default: System.out.println("Doesn't have a type"); return -1;
	}
	}
	
	public void printToken() {
		System.out.println("Type: " + type);
		System.out.println("Color: " + color);
		System.out.println();
	}
	
	public void printTokenAdvanced() {
		System.out.println("Type: " + type);
		System.out.println("Color: " + color);
		System.out.println("Status: " + status);
		System.out.println("isFaceUp: " + isFaceUp);
		System.out.println();
	}
}
