package gameEngine;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;

public class AccuseDialog extends DialogPanel {
	//The game board instance 
	Board board = Board.getInstance();
	//The necessary JComponents
	private JLabel roomLabel;
	private JComboBox<Card> roomBox;
	
	public AccuseDialog() {
		//Generate the DialogPanel items first
		super();
		
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
		
		//Add the room box listener
		roomBox.addActionListener(new roomListener());
		
		//Add the room components first
		add(roomLabel);
		add(roomBox);
		//Then add the parent components
		addBaseComponents();
	}
	
/**
 * Listener for the room combo box. Sets the room card field to the selected room.
 */
private class roomListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
			
	}
}
}
