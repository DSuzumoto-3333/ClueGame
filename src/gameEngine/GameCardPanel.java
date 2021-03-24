package gameEngine;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import gameEngine.GameControlPanel;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;

public class GameCardPanel extends JPanel {
	CardPanelContainer peoplePanel, roomsPanel, weaponsPanel;
	public GameCardPanel() {
		peoplePanel = new CardPanelContainer("People");
		roomsPanel = new CardPanelContainer("Rooms");
		weaponsPanel = new CardPanelContainer("Weapons");
		
		add(peoplePanel);
		add(roomsPanel);
		add(weaponsPanel);
	}
	
	public void addHandCardGUI(Card card) {
		//Determine the card's type
		switch(card.getType()) {
		case PERSON:
			//Add a person to peoplePanel's hand
			peoplePanel.addInHand(card.getName());
			break;
		case ROOM:
			//Add a room to roomPanel's hand
			roomsPanel.addInHand(card.getName());
			break;
		case WEAPON:
			//Add a weapon to weaponPanel's hand
			weaponsPanel.addInHand(card.getName());
			break;
		}
	}
	
	public void removeHandCardGUI(Card card) {
		//Determine the card's type
		switch(card.getType()) {
		case PERSON:
			//Add a person to peoplePanel's hand
			peoplePanel.removeFromHand(card.getName());
			break;
		case ROOM:
			//Add a room to roomPanel's hand
			roomsPanel.removeFromHand(card.getName());
			break;
		case WEAPON:
			//Add a weapon to weaponPanel's hand
			weaponsPanel.removeFromHand(card.getName());
			break;
		}
	}
	
	public void addSeenCardGUI(Card card, Color playerColor) {
		//Determine the card's type
		switch(card.getType()) {
		case PERSON:
			//Add a person to peoplePanel's hand
			peoplePanel.addInSeen(card.getName(), playerColor);
			break;
		case ROOM:
			//Add a room to roomPanel's hand
			roomsPanel.addInSeen(card.getName(), playerColor);
			break;
		case WEAPON:
			//Add a weapon to weaponPanel's hand
			weaponsPanel.addInSeen(card.getName(), playerColor);
			break;
		}
	}
	
	public void removeSeenCardGUI(Card card) {
		//Determine the card's type
		switch(card.getType()) {
		case PERSON:
			//Add a person to peoplePanel's hand
			peoplePanel.removeFromSeen(card.getName());
			break;
		case ROOM:
			//Add a room to roomPanel's hand
			roomsPanel.removeFromSeen(card.getName());
			break;
		case WEAPON:
			//Add a weapon to weaponPanel's hand
			weaponsPanel.removeFromSeen(card.getName());
			break;
		}
	}
	
	
	public static void main(String[] args) {
		GameCardPanel panel = new GameCardPanel();  // create the panel
		panel.setLayout(new GridLayout(3,1));
		panel.setBorder(new TitledBorder(new EtchedBorder(), "Known Cards"));
		JFrame frame = new JFrame();  // create the frame 
		frame.setContentPane(panel); // put the panel in the frame
		frame.setSize(250, 900);  // size the frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
		frame.setVisible(true); // make it visible
		
		//Test GUI
		//Create a bunch of cards.
		Card person1 = new Card("Person 1", CardType.PERSON);
		Card person2 = new Card("Person 2", CardType.PERSON);
		Card person3 = new Card("Person 3", CardType.PERSON);
		Card person4 = new Card("Person 4", CardType.PERSON);
		Card person5 = new Card("Person 5", CardType.PERSON);
		Card person6 = new Card("Person 6", CardType.PERSON);
		Card room1 = new Card("Room 1", CardType.ROOM);
		Card room2 = new Card("Room 2", CardType.ROOM);
		Card room3 = new Card("Room 3", CardType.ROOM);
		Card room4 = new Card("Room 4", CardType.ROOM);
		Card room5 = new Card("Room 5", CardType.ROOM);
		Card room6 = new Card("Room 6", CardType.ROOM);
		Card weap1 = new Card("Weapon 1", CardType.WEAPON);
		Card weap2 = new Card("Weapon 2", CardType.WEAPON);
		Card weap3 = new Card("Weapon 3", CardType.WEAPON);
		Card weap4 = new Card("Weapon 4", CardType.WEAPON);
		Card weap5 = new Card("Weapon 5", CardType.WEAPON);
		Card weap6 = new Card("Weapon 6", CardType.WEAPON);
		
		//Test adders
		panel.addHandCardGUI(person1);
		panel.addHandCardGUI(person2);
		panel.addHandCardGUI(room1);
		panel.addHandCardGUI(room2);
		panel.addHandCardGUI(room3);
		panel.addHandCardGUI(weap1);
		
		panel.addSeenCardGUI(person3, Color.red);
		panel.addSeenCardGUI(person4, Color.orange);
		panel.addSeenCardGUI(person5, Color.yellow);
		panel.addSeenCardGUI(person6, Color.green);
		panel.addSeenCardGUI(room4, Color.red);
		panel.addSeenCardGUI(room5, Color.orange);
		panel.addSeenCardGUI(room6, Color.yellow);
		panel.addSeenCardGUI(weap2, Color.red);
		panel.addSeenCardGUI(weap3, Color.orange);
		panel.addSeenCardGUI(weap4, Color.yellow);
		panel.addSeenCardGUI(weap5, Color.green);
		panel.addSeenCardGUI(weap6, Color.blue);
		
		//Test removers
		
		panel.removeHandCardGUI(person2);
		panel.removeHandCardGUI(room2);
		panel.removeHandCardGUI(room3);
		panel.removeHandCardGUI(weap1);
		
		panel.removeSeenCardGUI(person4);
		panel.removeSeenCardGUI(person6);
		panel.removeSeenCardGUI(room4);
		panel.removeSeenCardGUI(room5);
		panel.removeSeenCardGUI(room6);
		panel.removeSeenCardGUI(weap3);
		panel.removeSeenCardGUI(weap5);
	}
}

