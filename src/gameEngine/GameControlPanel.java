package gameEngine;

import javax.swing.*;

public class GameControlPanel extends JPanel {

	public GameControlPanel()  {
		//Create a wrapper panel
		JPanel wrapper = new JPanel();
		
		//Create the top panel
		JPanel top = new JPanel();
		//Create the turn panel
		JPanel turnPanel = new JPanel();
		JLabel turnLabel = new JLabel("Who's Turn?");
		JTextField turnField = new JTextField();
		//Create the roll panel
		JPanel rollPanel = new JPanel();
		JLabel rollLabel = new JLabel("Roll:");
		JTextField rollField = new JTextField();
		//Create the accuse button
		JButton accuseButton = new JButton("Acuse!");
		//Create the next button
		JButton nextButton = new JButton("Next!");
		
		//Create the bottom panel
		JPanel bottom = new JPanel();
		JLabel guessLabel = new JLabel("Guess");
		JLabel guessResultLabel = new JLabel("Guess Result");
	}
	
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  // create the panel
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(750, 180);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		// test filling in the data
		//panel.setTurn(new ComputerPlayer( "Col. Mustard", 0, 0, "orange"), 5);
		//panel.setGuess( "I have no guess!");
		//panel.setGuessResult( "So you have nothing?");
	}
}
