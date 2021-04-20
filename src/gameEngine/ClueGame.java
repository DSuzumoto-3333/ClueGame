package gameEngine;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Main entrypoint for the game, primarily handles drawing the GUI here.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 *
 */
public class ClueGame extends JFrame{
	
	/**
	 * Creates a new type of JFrame object known as ClueGame. This will be frame where all of the game's panels and dialog boxes are drawn.
	 */
	public ClueGame() {
		//Set default window properties like size and exit behaviors.
		setSize(930, 890);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("CSCI 306 Clue Game");
	}
	
	/**
	 * The main entry point for our game. Creates a new ClueGame frame, populates itself with the main 3 components that it requires, mainly the
	 * board, control panel, and card panel, presents the splash screen dialogue, then launches the first turn.
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
		GameCardPanel cardPanel = new GameCardPanel();
		
		//Set the Board's game frame and card panel
		board.setFrame(gameFrame);
		board.setControlPanel(controlPanel);
		board.setCardPanel(cardPanel);
		
		//Add the initial hand cards to the GUI before starting
		//Get the HumanPlayer instance.
		ArrayList<Player> players = board.getPlayers();
		Player humanPlayer = null;
		for(Player player : players) {
			if(player instanceof HumanPlayer) {
				humanPlayer = player;
				break;
			}
		}
		//Get the player's hand
		if(!(humanPlayer == null)) {
			Set<Card> hand = humanPlayer.getHand();
			//Add all the cards to the GUI
			for(Card card : hand) {
				cardPanel.addHandCardGUI(card, humanPlayer.getColor());
			}
		}
		
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
