package gameEngine;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * A class that extends the basic DialogPanel object and is used to draw a dialog panel that allows the user to make a suggestion, to be 
 * drawn when the player clicks a room tile to move to.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 *
 */

public class SuggestDialog extends DialogPanel {
	//The game board instance 
	Board board = Board.getInstance();
	//The necessary JComponents
	private JLabel roomLabel;
	private JTextField roomField;
	
	
	/**
	 * The constructor for the SuggestDialog object. Requires the game's Jframe and the current room's card to be passed in as arguments.
	 * @param frame - The ClueGame instance that the game is being drawn on.
	 * @param roomCard - The card representing the room that the player has just moved to.
	 */
	public SuggestDialog(ClueGame frame, Card roomCard) {
		//Generate the DialogPanel items first
		super(frame);
		
		
		//Create the label and the combo box for rooms
		roomLabel = new JLabel("Room");
		roomField = new JTextField(roomCard.getName());
		roomField.setEditable(false);
		
		//Add the submit button listener
		setSubmitButtonBehavior(new SubmitListener());
		
		//Add the room components first
		add(roomLabel);
		add(roomField);
		//Then add the parent components
		addBaseComponents();
		
		//Set the room card to default.
		setRoom(roomCard);
	}

/**
 * Listener for the submit button. If all of the card fields are set, the dialog panel is made invisible. If not, it will throw an error.
 */
private class SubmitListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		//If any of the card fields are null, prompt the user to finish selecting their suggestion.
		if((getRoom() == null) || (getPerson() == null) || (getWeapon() == null)) {
			JOptionPane.showMessageDialog(getFrame(), 
					"Error: Your suggestion is not complete.",
					"Complete Suggestion",
					JOptionPane.ERROR_MESSAGE);
		}
		//If not, handle the player's suggestion.
		else {
			//Make the window invisible
			setVisible(false);
			
			//Put the selected cards into a set.
			Set<Card> suggestion = new HashSet<Card>();
			suggestion.add(getRoom());
			suggestion.add(getWeapon());
			suggestion.add(getPerson());
			
			//Handle the suggestion
			board.handleSuggestion(suggestion, board.getHumanPlayer());
		}
	}
}
}
