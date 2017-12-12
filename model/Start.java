import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Start {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Start window = new Start();
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
	public Start() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Banqi AI");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		
		String[] box1 = {"Me"};
		String[] box2 = {"Negamax","Q-Table"};
		
		JComboBox Player1 = new JComboBox(box1);
		Player1.setBounds(25, 100, 150, 22);
		frame.getContentPane().add(Player1);
		
		JComboBox Player2 = new JComboBox(box2);
		Player2.setBounds(240, 100, 150, 22);
		frame.getContentPane().add(Player2);
		
		JButton btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Start game
				int leftBox = Player1.getSelectedIndex();
				int rightBox = Player2.getSelectedIndex();
				
				String leftName = Player1.getSelectedItem().toString();
				String rightName = Player2.getSelectedItem().toString();
				
				//System.out.println(leftName+" vs. "+rightName);
				//System.out.println(leftBox+" vs. "+rightBox);
				GameBoard.main(Player1.getSelectedItem().toString(), Player2.getSelectedItem().toString());
				
				
			}
		});
		btnStartGame.setBounds(155, 166, 120, 25);
		frame.getContentPane().add(btnStartGame);
		
		JLabel lblVs = new JLabel("VS");
		lblVs.setHorizontalAlignment(SwingConstants.CENTER);
		lblVs.setBounds(175, 103, 65, 16);
		frame.getContentPane().add(lblVs);
		
		JLabel lblHeader = new JLabel("Banqi with AI");
		lblHeader.setForeground(java.awt.Color.BLUE);
		lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeader.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblHeader.setBounds(25, 33, 365, 40);
		frame.getContentPane().add(lblHeader);
	}
}
