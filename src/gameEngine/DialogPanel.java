package gameEngine;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * A class to hold all the basic methods and components needed for a suggestion/accusaction dialog box.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 */

public abstract class DialogPanel extends JDialog {
	//The instance of the game board.
	private Board board = Board.getInstance();
	//The necessary JComponents
	private JLabel personLabel, weaponLabel;
	private JComboBox<Card> personBox, weaponBox;
	private JButton submitButton, cancelButton;
	//The cards to return as a suggestion/accusation
	private Card roomCard, personCard, weaponCard;
	
	/**
	 * Creates a basic dialogue panel with everything except for the room label and box/text field. Inheriting classes will
	 * establish those first.
	 */
	public DialogPanel() {
		super();
		//Set the basic window properties
		setTitle("Make an Accusation");
		setSize(300, 200);
		setLayout(new GridLayout(0, 2));
		
		//Get the deck and sort the cards by type.
		ArrayList<Card> cards = board.getDeck();
		Card[] People = new Card[6];
		Card[] Weapons = new Card[6];
		int i = 0, j = 0;
		for(Card card : cards) {
			switch(card.getType()) {
			case PERSON:
				People[i] = card;
				i++;
				break;
			case WEAPON:
				Weapons[j] = card;
				j++;
				break;
			}
		}
		
		//Create the labels, combo boxes, and buttons.
		personLabel = new JLabel("Person");
		personBox = new JComboBox<Card>(People);
		weaponLabel = new JLabel("Weapon");
		weaponBox = new JComboBox<Card>(Weapons);
		submitButton = new JButton("Submit");
		cancelButton = new JButton("Cancel");

		//Add action listeners
		cancelButton.addActionListener(new CancelListener());
		submitButton.addActionListener(new SubmitListener());
		personBox.addActionListener(new personListener());
		weaponBox.addActionListener(new weaponListener());
	}
	
	/**Method used to add the existing elements of a dialog panel to the panel itself, to be called after inheriting class adds the room
	 * label and corresponding component.
	 */
	public void addBaseComponents() {
		//Add all the missing components.
		add(personLabel);
		add(personBox);
		add(weaponLabel);
		add(weaponBox);
		add(submitButton);
		add(cancelButton);
	}

/**
 * Listener for the cancel button. When the cancel button is pressed, the Card fields of the dialog panel are set to null and the dialog 
 * panel is made invisible.
 */
private class CancelListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
			    
	}
}

/**
 * Listener for the submit button. If all of the card fields are set, the dialog panel is made invisible. If not, it will throw an error.
 */
private class SubmitListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		
	}
}

/**
 * Listener for the person combo box. Sets the Person card field to the selected person.
 */
private class personListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		System.out.println(personBox.getSelectedItem());
	}
}

/**
 * Listener for the weapon combo box. Sets the weapon card field to the selected weapon.
 */
private class weaponListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		
	}
}
}
