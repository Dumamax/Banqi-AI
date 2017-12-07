//Code in this class is paraphrased from the following online swing example of checkers
//<https://www.javaworld.com/article/3014190/learn-java/checkers-anyone.html> 
//author-Jeff Friesen DEC 13, 2015 12:48 PM PT

//This code renders the board, all tokens and facilitates drag-and-drop game play



//import model.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

//import edu.colostate.cs.cs414.banqi.controller.Controller;
//import edu.colostate.cs.cs414.banqi.model.Board;
//import edu.colostate.cs.cs414.banqi.model.Game;
//import edu.colostate.cs.cs414.banqi.model.Status;
//import edu.colostate.cs.cs414.banqi.model.Tile;
//import edu.colostate.cs.cs414.banqi.model.Token;
//import edu.colostate.cs.cs414.banqi.model.Type;

public class BoardComponent extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int squareSize=(int)(80*1.25);;
	
	private int boardWidth = 8 * squareSize;
	
	private int boardHeight = 4 * squareSize;
	

    int boardStartY1 = 2*this.squareSize+10;
    
    int boardStartY2 = 6*this.squareSize+20;
	
	private boolean dragging = false;
	
	Game game;
	String user;
	GameBoard parentGameBoard;
	
	

	//Game game = new Game();
	
	Token dragToken;
	
	int mouseX,mouseY,mouseX2,mouseY2,mouseDx,mouseDy;
	
	int tileX,tileY,tileX2,tileY2;
	
	public BoardComponent(Game aGame, String aUser, GameBoard prntGB){
		
		this.user=aUser;
		this.game=aGame;
		this.parentGameBoard=prntGB;
		
		//Listens for mouse button events
		addMouseListener(new MouseAdapter()
				{
					
					//Collects coordinates of button press
					@Override
					public void mousePressed(MouseEvent me){
						mouseX = me.getX();
                        mouseY = me.getY();
                        
                        
                        //get tile coordinates.
                        tileX=mouseX/squareSize;
                        tileY=(mouseY-boardStartY1)/squareSize;
                        //System.out.println(tileX+" "+tileY);
                        //game.getBoard().getTile(tileX+1, tileY+1).getToken().flipToken();
                        dragToken = game.getBoard().getTile(tileX+1, tileY+1).getToken();
                        dragging = true;
                        
                        //repaint();
					}
					
					//Collects coordinates of mouse release and calls for a game move from
					//mouse press to mouse release then calls repaint to update the board.
					@Override
					public void mouseReleased(MouseEvent me){
						dragging=false;
						dragToken=null;
						mouseX2=me.getX();
						mouseY2=me.getY();
                        
                        //get tile coordinates.
                        tileX2=mouseX2/squareSize;
                        tileY2=(mouseY2-boardStartY1)/squareSize;
                        
                        if(game.getBoard().getToken(tileX+1, tileY+1)!=null){
                        
                        	if(game.moveToken(user, tileX+1, tileY+1, tileX2+1, tileY2+1)) {
                        		if(game.getGameID().equals("AI")){
                        			try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
                        			parentGameBoard.refresh();
                        		}else{
                        			//Controller.updateGame(game);
                        		}
                        	}
                        
                        }
                        
                        //System.out.println(tileX2+" "+tileY2);
                        if(game.isOver()) {
							System.out.println("Winning Player: " + game.getWinningPlayer());
							System.out.println("Winning Color: " + game.getWinningColor());
						}
                        
                        repaint();
						
					}
				}
				);
		
		
		//For collecting mouse drag coordinates
		addMouseMotionListener(new MouseMotionAdapter(){
			
			@Override
			public void mouseDragged(MouseEvent me){
				if(dragging){
					mouseDx=me.getX();
					mouseDy=me.getY();
					repaint();
				}
			}
			
			
				
		});
		
		
		
		
	}
		//Paint entire board, called by "repaint()"
		@Override
	    protected void paintComponent(Graphics g){
		
	      paintBanqiBoard(g);
	      
	      
	      
	      Board board = game.getBoard();
	      
	      //Place active tokens on the board
	      for(int y=0;y<4;y++){
	      	for(int x=0;x<8;x++){
	      		//Tile tile = board.getTile(x+1, y+1);
	      		Token token = board.getToken(x+1, y+1);
	      		if(token!=dragToken){
		      		if(token!=null){
			      		TokenComponent gToken = new TokenComponent(javaColor(token.getColor()),stringType(token.getType()),token.isFaceUp(),boolStatus(token.getStatus()));
			      		gToken.draw(g, x*squareSize+squareSize/2, y*squareSize+squareSize/2+boardStartY1);
		      		}
	      		}
	      	}
	      }
	      
	      
	      //Place captured tokens in their respective grave yards.
	      ArrayList<Token> graveyard = board.getGraveyard();
	      Iterator<Token> graveyardIterator = graveyard.iterator();
	      int redX,redY,blackX,blackY;
	      redX=redY=blackX=blackY=0;
	      while(graveyardIterator.hasNext()){
	    	  Token token = graveyardIterator.next();
	    	  TokenComponent gToken = new TokenComponent(javaColor(token.getColor()),stringType(token.getType()),token.isFaceUp(),true);
	    	  //Top grave yard
	    	  if(token.getColor()==Ecolor.RED){
	    		  if(redX/8==1){
	    			  redX=0;
	    			  redY=1;
	    		  }
	    		  gToken.draw(g, redX*squareSize+squareSize/2, redY*squareSize+squareSize/2);
	    		  redX++;
	    	  }
	    	  //Bottom grave yard
	    	  if(token.getColor()==Ecolor.BLACK){
	    		  if(blackX/8==1){
	    			  blackX=0;
	    			  blackY=1;
	    		  }
	    		  gToken.draw(g, blackX*squareSize+squareSize/2, blackY*squareSize+squareSize/2+boardStartY2);
	    		  blackX++;
	    	  }
	    	  
	      }
	      
	      
	      
	      // Draw dragged token last so that it appears over any underlying 
	  // token.
	
	      if (dragging&&dragToken!=null){
	    	  TokenComponent dToken = new TokenComponent(javaColor(dragToken.getColor()),stringType(dragToken.getType()),dragToken.isFaceUp(),boolStatus(dragToken.getStatus()));
	    	  dToken.draw(g, mouseDx, mouseDy);
	      }
	      
	   }
	
	
		//Paint the background of the game board and grave yards
	    private void paintBanqiBoard(Graphics g){
	      ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                                        RenderingHints.VALUE_ANTIALIAS_ON);
	      
	      //Trace top graveYard
	      
	      for (int row = 0; row < 2; row++){
	         g.setColor(Color.BLACK);
	         for (int col = 0; col < 8; col++){
	        	// System.out.println(squareSize);
	            g.drawRect(col * this.squareSize, row * this.squareSize, this.squareSize, this.squareSize);
	         }
	      }

	      // Paint Banqi board.

	      for (int row = 0; row < 4; row++){
	         g.setColor(((row & 1) != 0) ? Color.BLACK : Color.WHITE);
	         for (int col = 0; col < 8; col++){
	        	// System.out.println(squareSize);
	            g.fillRect(col * this.squareSize, row * this.squareSize+ boardStartY1, this.squareSize, this.squareSize);
	            g.setColor((g.getColor() == Color.BLACK) ? Color.WHITE : Color.BLACK);
	         }
	      }
	      
	      //Trace bottom graveYard
	      
	      for (int row = 0; row < 2; row++){
	         g.setColor(Color.BLACK);
	         for (int col = 0; col < 8; col++){
	        	// System.out.println(squareSize);
	            g.drawRect(col * this.squareSize, row * this.squareSize+boardStartY2, this.squareSize, this.squareSize);
	         }
	      }
	      
	   }
	   
	   public int getBoardWidth(){
		   return boardWidth;
	   }
	   
	   
	   public int getBoardHeight(){
		   return boardHeight;
	   }
	   
	   //Change the model.Type enum to a string
	   private String stringType(Type type){
				switch(type) {
				case GENERAL: return "GENERAL";
				case ADVISOR: return "ADVISOR";
				case ELEPHANT: return "ELEPHANT";
				case CHARIOT: return "CHARIOT";
				case HORSE: return "HORSE";
				case CANNON: return "CANNON";
				case SOLDIER: return "SOLDIER";
				default: System.out.println("Doesn't have a type"); return "Doesn't have a type";
			}
	   }
	   
	   //Change the model.Status enum to a boolean
	   private boolean boolStatus(Status status){
		   switch(status){
		   case INACTIVE: return false;
		   case ACTIVE: return true;
		   default: System.out.println("Doesn't have status"); return false;
		   }
	   }
	   
	   
	   //Change the model.Color enum to a java.awt.Color color
	   private Color javaColor(Ecolor color){
		   if(color==Ecolor.RED){
			   return java.awt.Color.RED;
		   }else{
			   return java.awt.Color.BLACK;
		   }
	   }
	
	
}
