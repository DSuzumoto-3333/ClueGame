package gameEngine;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * A class that extends the basic DialogPanel object and is used to draw a dialog panel that allows the user to make an accusation and end
 * the game.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 *
 */

public class AccuseDialog extends DialogPanel {
	//The game board instance 
	Board board = Board.getInstance();
	//The necessary JComponents
	private JLabel roomLabel;
	private JComboBox<Card> roomBox;
	
	/**
	 * Default constructor for the AccuseDialog Object, only requires the game's JFrame to be passed in.
	 * @param frame - The ClueGame instance that the game is being drawn on.
	 */
	public AccuseDialog(ClueGame frame) {
		//Generate the DialogPanel items first
		super(frame);
		
		//Get the game's cards and sort out the rooms
		ArrayList<Card> cards = board.getDeck();
		Card[] Rooms = new Card[9];
		int i = 0;
		for(Card card : cards) {
			if(card.getType() == CardType.ROOM) {
				Rooms[i] = card;
				i++;
			}
		}
		
		//Create the label and the combo box for rooms
		roomLabel = new JLabel("Room");
		roomBox = new JComboBox<Card>(Rooms);
		
		//Add the room box listener and submit button listener
		roomBox.addActionListener(new roomListener());
		setSubmitButtonBehavior(new SubmitListener());
		
		//Add the room components first
		add(roomLabel);
		add(roomBox);
		//Then add the parent components
		addBaseComponents();
		
		//Set the room card to default.
		setRoom(((Card) roomBox.getSelectedItem()));
	}
	
/**
 * Listener for the room combo box. Sets the room card field to the selected room.
 */
private class roomListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		setRoom(((Card) roomBox.getSelectedItem()));
	}
}

/**
 * Listener for the submit button. If all of the card fields are set, the dialog panel is made invisible. If not, it will throw an error.
 */
private class SubmitListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		//If any of the card fields are null, prompt the user to finish selecting their accusation.
		if((getRoom() == null) || (getPerson() == null) || (getWeapon() == null)) {
			JOptionPane.showMessageDialog(getFrame(), 
					"Error: Your accusation is not complete.",
					"Complete Accusation",
					JOptionPane.ERROR_MESSAGE);
		}
		//If not, handle the player's accusation.
		else {
			//Make the window invisible
			setVisible(false);
			//Put the selected cards into a set.
			Set<Card> accusation = new HashSet<Card>();
			accusation.add(getRoom());
			accusation.add(getWeapon());
			accusation.add(getPerson());
			//Determine if the accusation is correct.
			boolean gameWon = board.checkAccusation(accusation);
			
			//If the accusation was correct
			if(gameWon) {
				//Notify the player that they've won.
				JOptionPane.showMessageDialog(getFrame(), 
						"Congradulations, you have won!\nGood Job!",
						"Game Won!",
						JOptionPane.INFORMATION_MESSAGE);
				//And exit the game.
				System.exit(0);
			}
			//If it was incorrect
			else {
				//Notify the player that they've lost
				JOptionPane.showMessageDialog(getFrame(), 
						"Your accusation was incorrect.\nBetter luck next time!",
						"Game Lost!",
						JOptionPane.INFORMATION_MESSAGE);
				//And exit the game.
				System.exit(0);
			}
		}
	}
}
}
