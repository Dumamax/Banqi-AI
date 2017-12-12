//package edu.colostate.cs.cs414.banqi.userInterface;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.util.Pair;

//import edu.colostate.cs.cs414.banqi.controller.Controller;
//import edu.colostate.cs.cs414.banqi.model.AI;
//import edu.colostate.cs.cs414.banqi.model.Color;
//import edu.colostate.cs.cs414.banqi.model.Game;

//import 

import javax.swing.JButton;

public class GameBoard {

	private JFrame frame;
	private Game game;
	private String user;
	private AI aiPlayer;
	
	private static String P1;
	private static String P2;
	
	private int posX;
	private int posY;
	private static boolean testing= false;
	private static Map<Pair<String,String>, Double> Q = new HashMap<Pair<String,String>, Double>();
	
	
	//testing AI main
	public static void main(String[] args) {
		Game aiGame= new Game();
		String tUser = "Tester";
		aiGame.setPlayers(tUser, "AI");
		aiGame.setCreatorColor(Ecolor.RED);
		aiGame.setCurrentColor(Ecolor.RED);
		aiGame.setCurrentPlayer(tUser);
		aiGame.setGameID("AI");
		
		testing = true;
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameBoard window = new GameBoard(aiGame, tUser);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void main(String p1, String p2) {
		P1=p1;
		P2=p2;
		
		Game aiGame;
		
		AI ai= new AI();
		
		try {
			if(p2.equals("Q-Table")){
				Q=ai.loadQ("qMap1Game2.map");
			}
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(P2.equals("Q-Table")){
			aiGame = new Game("R4D R2D R2D B1D R5D B1D B6D R1D B4D R7D R3D B6D B4D B3D R4D B1D B2D R6D B1D B3D B2D R1D B5D B1D R1D R3D B7D B5D R1D R1D R6D R5D . ");
			aiGame.setPlayers(p1, p2);
			aiGame.setCreatorColor(Ecolor.BLACK);
			aiGame.setCurrentColor(Ecolor.RED);

			aiGame.setCurrentPlayer(p2);
		}else{
			aiGame= new Game();
			aiGame.setPlayers(p1, p2);
			aiGame.setCreatorColor(Ecolor.RED);
			aiGame.setCurrentColor(Ecolor.RED);

			aiGame.setCurrentPlayer(p1);
		}
		
		aiGame.setGameID("AI");
		
		testing = true;
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameBoard window = new GameBoard(aiGame, p1);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	/**
	 * Launch the application.
	 */
	public static void main(Game mGame,String mUser) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameBoard window = new GameBoard(mGame, mUser);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void main(Game mGame, String mUser, int x, int y) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameBoard window = new GameBoard(mGame, mUser, x, y);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	
	/**
	 * @wbp.parser.constructor
	 */
	public GameBoard(Game aGame, String aUser) {
		this.user=aUser;
		this.game=aGame;
		posX = 100;
		posY = 100;
		initialize();
	}
	
	public GameBoard(Game aGame, String aUser, int x, int y) {
		this.user=aUser;
		this.game=aGame;
		this.posX=x;
		this.posY=y;
		initialize();
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		if(game.getGameID().equals("AI")&&game.getPlayers().get(1).equals("Negamax")){
			aiPlayer = new AI(game,Ecolor.BLACK);
		}else{
			aiPlayer = new AI(game,Ecolor.RED);
		}
		frame = new JFrame(game.toString()+"---You are "+game.getPlayerColor(user)+isMyTurn());
		frame.setBounds(posX, posY, 850, 939);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//code from: https://stackoverflow.com/questions/17815033/how-to-change-java-icon-in-a-jframe
		//frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
		
		BoardComponent board = new BoardComponent(game, user, this);
		board.setBounds(0, 0, board.getBoardWidth()+2, board.getBoardHeight()*2+22);
		frame.getContentPane().add(board);
		
		JButton btnCancel = new JButton("Close");
		btnCancel.setToolTipText("Close window without making a move.");
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				frame.dispose();
				
				
			}
		});
		btnCancel.setBounds(10, 835, 97, 25);
		frame.getContentPane().add(btnCancel);
		
		
		//Quit
		JButton btnQuit = new JButton("Quit");
		btnQuit.setToolTipText("In Banqi it is common to forfeit if you can see no way of winning.");
		btnQuit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				
				if(game.getGameID().equals("AI")){
					frame.dispose();
				}else{
					if(0==JOptionPane.showConfirmDialog(null, "Are you sure you want to forfeit? This will count as a loss.", "Deregister Confirm.", JOptionPane.YES_NO_OPTION)){
						//Controller.forfeitGame(user, game.getGameID());
						frame.dispose();
					}
					
				}
			}
		});
		btnQuit.setBounds(705, 835, 97, 25);
		frame.getContentPane().add(btnQuit);
		
		
	}
	
	private String isMyTurn(){
		if(user.equals(game.getCurrentPlayer())){
			return " It is your turn.";
		}else{
			return " It is not your turn.";
		}
	}
	
	public void refresh(){
		if(game.getGameID().equals("AI")){
			//AI makes move here
			if(game.isOver()){
				JOptionPane.showMessageDialog(null, "The game is Over!\nThe winner is "+game.getWinningPlayer());
				frame.dispose();
			}
			if(game.getCurrentPlayer().equals(P2)){
				//System.out.println(game.getWinningPlayer());
				if(game.isOver()){
					JOptionPane.showMessageDialog(null, "The game is Over!\nThe winner is "+game.getWinningPlayer());
					frame.dispose();
				}else{
				
					String gameState = game.getBoard().saveBoard();//This is where it gets an active game state, this will throw errors when using the AI test board
					//aiPlayer.printBoard(gameState);
					int[] bestMove;
					if(P2.equals("Negamax")){
						bestMove = aiPlayer.negamax(gameState, 10);
					}else if(P2.equals("Q-Table")){
						bestMove = aiPlayer.Qmove(gameState, 'R', Q);
					}else{
						bestMove = new int[]{1,1,1,1};
					}
					game.moveToken(P2, bestMove[0], bestMove[1], bestMove[2], bestMove[3]);
					
					//board gets refreshed
					GameBoard freshGameBoard = new GameBoard(game, user, (int) frame.getLocation().getX(), (int) frame.getLocation().getY());
					freshGameBoard.main(game, user,  (int) frame.getLocation().getX(), (int) frame.getLocation().getY());
					frame.dispose();
				}
			}
		}
	}
	
	

}
