package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import gameEngine.*;
import junit.framework.Assert;

import java.util.Random;
import java.util.Set;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;

class ComputerAITests {


	private static Board board;

	@BeforeEach
	public void initializeBoard() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	/**
	 * Test to determine if the createSuggestion() method in ComputerPlayer.java creates valid suggestions with cards 
	 * that have not been seen before and with all the necessary types.
	 */
	@Test
	public void testCreateSuggestion() {
		//Create a test ComputerPlayer and put him in the center of the pool room.
		ComputerPlayer suggester = new ComputerPlayer("Test NPC", Color.black);
		suggester.setPosition(board.getRoom('O').getCenterCell());
		//Set up a bunch of cards that we don't want to see in suggestions.
		Set<Card> seen = new HashSet<Card>();
		seen.add(new Card("Knife", CardType.WEAPON));
		seen.add(new Card("Sword", CardType.WEAPON));
		seen.add(new Card("Axe", CardType.WEAPON));
		seen.add(new Card("Pistol", CardType.WEAPON));
		seen.add(new Card("Ramona Rodriguez", CardType.PERSON));
		seen.add(new Card("Leland Blake", CardType.PERSON));
		/* 
		 * Add each of these cards to the seen list. Seen is static because all players have seen the same cards, 
		 * aside from what's in their hand, so we can just add it through a single one of our players.
		 */
		for(Card card : seen) {
			suggester.addSeen(card);
		}
		
		/* 
		 * We should now be able to get 2 suggestions in total before we run out of unseen weapon cards. Get the 
		 * first suggestion
		 */
		Set<Card> sug = suggester.createSuggestion();
		//Ensure that exactly 1 of each type of card is returned.
		assertEquals(3, sug.size());
		boolean hasRoom = false, hasWeapon = false, hasPerson = false;
		for(Card card : sug) {
			switch(card.getType()) {
			case ROOM:
				hasRoom = true;
				break;
			case WEAPON:
				hasWeapon = true;
				break;
			case PERSON:
				hasPerson = true;
				break;
			}
		}
		assertTrue(hasRoom);
		assertTrue(hasWeapon);
		assertTrue(hasPerson);
		//Ensure that none of the cards returned were seen earlier
		for(Card card : sug) {
			assertFalse(seen.contains(card));
		}
		
		//Assume the weapon card has been seen now.
		for(Card card : sug) {
			if(card.getType().equals(CardType.WEAPON)) {
				suggester.addSeen(card);
				seen.add(card);
			}
		}
		//Get a new suggestion from the suggester
		sug = suggester.createSuggestion();
		//Ensure that exactly 1 of each type of card is returned.
		assertEquals(3, sug.size());
		hasRoom = false;
		hasWeapon = false;
		hasPerson = false;
		for(Card card : sug) {
			switch(card.getType()) {
			case ROOM:
				hasRoom = true;
				break;
			case WEAPON:
				hasWeapon = true;
				break;
			case PERSON:
				hasPerson = true;
				break;
			}
		}
		assertTrue(hasRoom);
		assertTrue(hasWeapon);
		assertTrue(hasPerson);
		//Ensure that none of the cards returned were seen earlier
		for(Card card : sug) {
			assertFalse(seen.contains(card));
		}
		
		//Assume the final weapon card has been seen now.
		for(Card card : sug) {
			if(card.getType().equals(CardType.WEAPON)) {
				suggester.addSeen(card);
				seen.add(card);
			}
		}
		//Get a new suggestion from the suggester
		sug = suggester.createSuggestion();
		//Ensure that an empty set is returned.
		assertEquals(0, sug.size());
	}
	
}

