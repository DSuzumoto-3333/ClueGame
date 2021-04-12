package gameEngine;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import java.util.ArrayList;

/**
 * A custom JPanel used for containing the GUI elements displaying the cards the player has in hand and what cards they've
 * seen. Allows for GameCardPanel to call instance methods from it's container instances to update game info easily.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 */
public class CardPanelContainer extends JPanel{
	//Create array lists to hold the text fields we'll be updating.
	ArrayList<JTextField> inHand, inSeen;
	//Declare some "None" text fields to add and remove as game changes.
	JTextField noneHand, noneSeen;
	//Declare the "In Hand:" and "Seen:" labels
	JLabel handLabel, seenLabel;
	
	/**
	 * Create a container object that will be capable of displaying all cards the player has, and that the player's seen.
	 * @param labelName - The label of the container. For our purposes, "People", "Rooms" and "Weapons" are the 3 used.
	 */
	public CardPanelContainer(String labelName) {
		//Set basic properties about the container panel.
		this.setLayout(new GridLayout(0,1));
		this.setBorder(new TitledBorder(new EtchedBorder(), labelName));
		
		//Instantiate our ArrayLists
		inHand = new ArrayList<JTextField>();
		inSeen = new ArrayList<JTextField>();

		//Create the "In Hand:" label
		handLabel = new JLabel("In Hand:");
		//Create a field for "None"
		noneHand = new JTextField();
		noneHand.setText("None");
		inHand.add(noneHand);
		
		//Create the "Seen:" label
		seenLabel = new JLabel("Seen:");
		//Create a field for "None"
		noneSeen = new JTextField();
		noneSeen.setText("None");
		inSeen.add(noneSeen);
		
		redrawContainer();
	}
	
	public void addInHand(String cardName) {
		//Remove the "None" element if present.
		if(inHand.contains(noneHand)) {
			inHand.remove(noneHand);
		}
		
		//Create a new text field for the card.
		JTextField card = new JTextField();
		card.setText(cardName);
		
		//Add to the panel and save in the arrayList.
		inHand.add(card);
		
		//Update the panel
		redrawContainer();
	}
	
	public void removeFromHand(String cardName) {
		//Search the ArrayList for the element.
		int i = 0;
		for(JTextField card : inHand) {
			if(card.getText().equals(cardName)) {
				inHand.remove(i);
				break;
			}
			i++;
		}
		
		//If the panel is empty, add the "None" field to it.
		if(inHand.size() == 0) {
			inHand.add(noneHand);
		}
		
		//Update the panel
		redrawContainer();
	}
	
	public void addInSeen(String cardName, Color color) {
		//Remove the "None" element if present.
		if(inSeen.contains(noneSeen)) {
			inSeen.remove(noneSeen);
		}

		//Create a new text field for the card.
		JTextField card = new JTextField();
		card.setText(cardName);
		card.setBackground(color);
		
		//Add to the array list
		inSeen.add(card);
		
		//Update the panel
		redrawContainer();
	}
	
	public void removeFromSeen(String cardName) {
		//Search the ArrayList for the element.
		int i = 0;
		//Set a null object to store the card in if found
		for(JTextField card : inSeen) {
			if(card.getText().equals(cardName)) {
				inSeen.remove(i);
				break;
			}
			i++;
		}

		//If the panel is empty, add the "None" field to it.
		if(inSeen.size() == 0) {
			inSeen.add(noneSeen);
		}
		
		//Update the panel
		redrawContainer();
	}
	
	public void redrawContainer() {
		//Get all components currently on the panel.
		Component[] components = this.getComponents();
		//Clear the panel
		for(int i = 0; i < components.length; i++) {
			this.remove(components[i]);
		}
		
		//Add the "In Hand:" label
		this.add(handLabel);
		//Add all the card text fields in the hand
		for(JTextField card : inHand) {
			this.add(card);
		}
		
		//Add the "Seen:" Label
		this.add(seenLabel);
		//Add all the card text fields seen
		for(JTextField card : inSeen) {
			this.add(card);
		}
		
		//Update the panel
		this.revalidate();
		this.repaint();
	}
}