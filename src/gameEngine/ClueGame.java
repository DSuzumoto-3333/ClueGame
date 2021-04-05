package gameEngine;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ClueGame extends JFrame{
	public ClueGame() {
		setSize(930, 890);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("CSCI 306 Clue Game");
	}
	
	public static void main (String[] args) {
		Board board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
		board.setSize(780, 690);
		
		ClueGame gameFrame = new ClueGame();
		
		GameControlPanel controlPanel = new GameControlPanel();
		
		GameCardPanel cardPanel = new GameCardPanel();
		
		gameFrame.add(board, BorderLayout.CENTER);
		gameFrame.add(cardPanel, BorderLayout.EAST);
		gameFrame.add(controlPanel, BorderLayout.SOUTH);
		
		gameFrame.setVisible(true);
	}
}
