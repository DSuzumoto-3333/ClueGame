package gameEngine;

import java.awt.GridLayout;
import java.awt.Color;

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
	//Create the 2 panels contained.
	JPanel handPanel, seenPanel;
	//Create array lists to hold the text fields we'll be updating.
	ArrayList<JTextField> inHand, inSeen;
	//Declare some "None" text fields to add and remove as game changes.
	JTextField noneHand, noneSeen;
	
	/**
	 * Create a container object that will be capable of displaying all cards the player has, and that the player's seen.
	 * @param labelName - The label of the container. For our purposes, "People", "Rooms" and "Weapons" are the 3 used.
	 */
	public CardPanelContainer(String labelName) {
		//Set basic properties about the container panel.
		this.setLayout(new GridLayout(2,1));
		this.setBorder(new TitledBorder(new EtchedBorder(), labelName));
		
		//Instantiate our ArrayLists
		inHand = new ArrayList<JTextField>();
		inSeen = new ArrayList<JTextField>();
		
		//Create the panel that will display what is in our hand
		handPanel = new JPanel(new GridLayout(0,1));
		//Create the "In Hand:" label
		JLabel handLabel = new JLabel("In Hand:");
		handPanel.add(handLabel);
		//Create a field for "None"
		noneHand = new JTextField();
		noneHand.setText("None");
		handPanel.add(noneHand);
		inHand.add(noneHand);
		
		//Create the panel that will display what has been seen
		seenPanel = new JPanel(new GridLayout(0,1));
		//Create the "Seen:" label
		JLabel seenLabel = new JLabel("Seen:");
		seenPanel.add(seenLabel);
		//Create a field for "None"
		noneSeen = new JTextField();
		noneSeen.setText("None");
		seenPanel.add(noneSeen);
		inSeen.add(noneSeen);
		
		this.add(handPanel);
		this.add(seenPanel);
	}
	
	public void addInHand(String cardName) {
		//Remove the "None" element if present.
		if(inHand.contains(noneHand)) {
			handPanel.remove(noneHand);
			inHand.remove(noneHand);
		}
		
		//Create a new text field for the card.
		JTextField card = new JTextField();
		card.setText(cardName);
		
		//Add to the panel and save in the arrayList.
		handPanel.add(card);
		inHand.add(card);
		
		//Update the panel
		this.revalidate();
		this.repaint();
	}
	
	public void removeFromHand(String cardName) {
		//Search the ArrayList for the element.
		int i = 0;
		//Set a null object to store the card in if found
		JTextField cardToRemove = null;
		for(JTextField card : inHand) {
			if(card.getText().equals(cardName)) {
				cardToRemove = inHand.get(i);
				break;
			}
			i++;
		}
		
		//If the card is found, remove it from the panel and the array list.
		if(!(cardToRemove == null)) {
			handPanel.remove(cardToRemove);
			inHand.remove(cardToRemove);
		}
		
		//If the panel is empty, add the "None" field to it.
		if(inHand.size() == 0) {
			handPanel.add(noneHand);
			inHand.add(noneHand);
		}
		
		//Update the panel
		this.revalidate();
		this.repaint();
	}
	
	public void addInSeen(String cardName, Color color) {
		//Remove the "None" element if present.
		if(inSeen.contains(noneSeen)) {
			seenPanel.remove(noneSeen);
			inSeen.remove(noneSeen);
		}

		//Create a new text field for the card.
		JTextField card = new JTextField();
		card.setText(cardName);
		card.setBackground(color);
		
		//Add to the panel and save in the array list.
		seenPanel.add(card);
		inSeen.add(card);
		
		//Update the panel
		this.revalidate();
		this.repaint();
	}
	
	public void removeFromSeen(String cardName) {
		//Search the ArrayList for the element.
		int i = 0;
		//Set a null object to store the card in if found
		JTextField cardToRemove = null;
		for(JTextField card : inSeen) {
			if(card.getText().equals(cardName)) {
				cardToRemove = inSeen.get(i);
				break;
			}
			i++;
		}

		//If the card is found, remove it from the panel and the array list.
		if(!(cardToRemove == null)) {
			seenPanel.remove(cardToRemove);
			inSeen.remove(cardToRemove);
		}

		//If the panel is empty, add the "None" field to it.
		if(inSeen.size() == 0) {
			seenPanel.add(noneSeen);
			inSeen.add(noneSeen);
		}
		
		//Update the panel
		this.revalidate();
		this.repaint();
	}
	
	public void redrawContainer() {
		
	}
}