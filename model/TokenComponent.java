 //Code in this class is paraphrased from the following online swing example of checkers
//<https://www.javaworld.com/article/3014190/learn-java/checkers-anyone.html> 
//author-Jeff Friesen DEC 13, 2015 12:48 PM PT

//This code renders individual tokens.



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

//import model.Type;

public final class TokenComponent {
	
	private final int diameter = 80;
	
	public boolean faceUp;
	
	public boolean active;
	
	private String name;
	
	private Color color;
	
	public TokenComponent(Color color, String name, boolean faceUp, boolean active){
		this.color = color;
		this.name = name;
		this.faceUp = faceUp;
		this.active = active;
	}
	
	public void draw(Graphics g, int cX, int cY){
		
		int X = cX - diameter / 2;
	    int Y = cY - diameter / 2;
	    Font nameFont = new Font(g.getFont().getFontName(), Font.BOLD,12);
	    Font powerFont = new Font(g.getFont().getFontName(), Font.BOLD, 20);
	    
		if(active){
		    if(faceUp){
				g.setColor(this.color);
				g.fillOval(X, Y, diameter, diameter);
				g.setColor(Color.white);
				g.drawOval(X, Y, diameter, diameter);
				g.setFont(nameFont);
				g.drawString(this.name, cX-(int)(8*name.length()/2), cY);
				g.setFont(powerFont);
				g.drawString(power(name), cX-8, cY+22);
		    }else{
		    	g.setColor(Color.white);
		    	g.fillOval(X, Y, diameter, diameter);
		    	g.setColor(Color.black);
		    	g.drawOval(X, Y, diameter, diameter);
		    }
		}
		
	}
	
	public int getDiameter(){
		return this.diameter;
	}
	
	private String power(String type){
		
		switch(type) {
		case "GENERAL": return "7";
		case "ADVISOR": return "6";
		case "ELEPHANT": return "5";
		case "CHARIOT": return "4";
		case "HORSE": return "3";
		case "CANNON": return "2";
		case "SOLDIER": return "1";
		default: System.out.println("Doesn't have a power."); return "-1";
	}
}
}
