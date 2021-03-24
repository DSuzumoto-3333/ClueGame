package gameEngine;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 * GUI Component that displays important information about the game, including current turn, roll, guess, and guess result.
 * Also holds interaction buttons for accusing and moving to next turn.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 *
 */
public class GameControlPanel extends JPanel {
	private JTextField guessField, guessResultField, turnField, rollField;

	public GameControlPanel()  {
		//Get the top and bottom panels
		JPanel top = createTop();
		JPanel bottom = createBottom();
		//Add them to the outermost panel.
		JPanel wrapper = new JPanel(new GridLayout(2, 1));
		add(top);
		add(bottom);
	}
	
	public JPanel createBottom() {
		//Create the guess panel
		JPanel guessPanel = new JPanel(new BorderLayout());
		JLabel guessLabel = new JLabel("Guess");
		guessPanel.add(guessLabel, BorderLayout.NORTH);
		guessField = new JTextField();
		guessPanel.add(guessField, BorderLayout.CENTER);
		
		//Create the guessResult panel
		JPanel guessResultPanel = new JPanel(new BorderLayout());
		JLabel guessResultLabel = new JLabel("Guess Result");
		guessResultPanel.add(guessResultLabel, BorderLayout.NORTH);
		guessResultField = new JTextField();
		guessResultPanel.add(guessResultField, BorderLayout.CENTER);
		
		//Create the bottom panel
		JPanel bottom = new JPanel(new GridLayout(1,2));
		bottom.add(guessPanel);
		bottom.add(guessResultPanel);
		return bottom;
	}
	
	public JPanel createTop() {
		//Create the turn panel
		JPanel turnPanel = new JPanel(new GridLayout(2,1));
		JLabel turnLabel = new JLabel("Who's Turn?");
		turnPanel.add(turnLabel);
		turnField = new JTextField();
		turnPanel.add(turnField);
		

		//Create the roll panel
		JPanel rollPanel = new JPanel();
		JLabel rollLabel = new JLabel("Roll:");
		rollPanel.add(rollLabel);
		rollField = new JTextField(5);
		rollPanel.add(rollField);

		JPanel dataPanel = new JPanel(new GridLayout(1,2));
		dataPanel.add(turnPanel);
		dataPanel.add(rollPanel);
		
		//Create the accuse button
		JButton accuseButton = new JButton("Acuse!");

		//Create the next button
		JButton nextButton = new JButton("Next!");
		
		JPanel buttonPanel = new JPanel(new GridLayout(1,2));
		buttonPanel.add(accuseButton);
		buttonPanel.add(nextButton);
		
		//Create the top panel
		JPanel top = new JPanel(new GridLayout(1,2));
		top.add(dataPanel);
		top.add(buttonPanel);
		return top;
	}
	/**
	 * Method to display the name of the player provided in the "Who's Turn?" field of the Control Panel.
	 * @param player - The player to display.
	 */
	public void displayTurn(Player player) {
		turnField.setText(player.getName());
		turnField.setBackground(player.getColor());
	}
	
	/**
	 * Method to display the number rolled in the "Roll: " field of the control panel.
	 * @param roll - Int representing the value of the roll to display.
	 */
	public void displayRoll(int roll) {
		rollField.setText(String.valueOf(roll));
	}
	
	/**
	 * Method to display the current guess, in string form, in the "Guess" field of the control panel.
	 * @param guess - A string representing the guess to display.
	 */
	public void displayGuess(String guess) {
		guessField.setText(guess);
	}
	
	/**
	 * Method to display the current guess result, in string form, in the "Guess Result" field of the control panel.
	 * @param result - A string representing the result of the last guess to be displayed.
	 */
	public void displayGuessResult(String result) {
		guessResultField.setText(result);
	}
	
	public static void main(String[] args) {
		GameControlPanel panel = new GameControlPanel();  // create the panel
		panel.setLayout(new GridLayout(2,0));
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(840, 200);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		//Test setters.
		panel.displayTurn(new ComputerPlayer("TestPlayer", Color.magenta));
		panel.displayRoll(6);
		panel.displayGuess("I do not have a guess");
		panel.displayGuessResult("I do not have a result");
	}
}
