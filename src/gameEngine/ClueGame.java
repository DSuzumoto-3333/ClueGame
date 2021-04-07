package gameEngine;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Main entrypoint for the game, primarily handles drawing the GUI here.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 *
 */
public class ClueGame extends JFrame{
	
	/**
	 * Creates a new type of JFrame object known as ClueGame
	 */
	public ClueGame() {
		//Set default window properties like size and exit behaviors.
		setSize(930, 890);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("CSCI 306 Clue Game");
	}
	
	/**
	 * The main entry point for our game.
	 * @param args
	 */
	public static void main (String[] args) {
		//Get the game board and initialize it both as the game's engine and as the panel in the GUI
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.setSize(780, 690);
		
		//Create a new Frame
		ClueGame gameFrame = new ClueGame();
		
		//Create a new Control Panel
		GameControlPanel controlPanel = new GameControlPanel(gameFrame);
		
		//Create a new Card Panel
		GameCardPanel cardPanel = new GameCardPanel(gameFrame);
		
		//Set the Board's game frame
		board.setFrame(gameFrame);
		
		//Add everything to the Frame
		gameFrame.add(board, BorderLayout.CENTER);
		gameFrame.add(cardPanel, BorderLayout.EAST);
		gameFrame.add(controlPanel, BorderLayout.SOUTH);
		
		//Show the splash screen
		JOptionPane.showMessageDialog(gameFrame, 
				"Welcome to Clue!\nYou are Ramona Rodriguez. \nCan you solve the mystery before the bots?",
				"Welcome to clue",
				JOptionPane.INFORMATION_MESSAGE);
		
		//Let the frame be visible.
		gameFrame.setVisible(true);
		
		//Start the game by launching the first turn.
		board.handleTurn();
		controlPanel.displayRoll(board.getCurrentRoll());
		controlPanel.displayTurn(board.getCurrentPlayer());
	}
}
