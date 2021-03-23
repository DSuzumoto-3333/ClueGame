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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
		ArrayList<Card> deck = board.getDeck();
		seen.add(new Card("Knife", CardType.WEAPON));
		seen.add(new Card("Sword", CardType.WEAPON));
		seen.add(new Card("Axe", CardType.WEAPON));
		seen.add(new Card("Pistol", CardType.WEAPON));
		seen.add(new Card("Ramona Rodriguez", CardType.PERSON));
		seen.add(new Card("Leland Blake", CardType.PERSON));
		//Add each of these cards to the seen list for the player, adding some to the hand as well
		int i = 0;
		for(Card card : seen) {
			//Add the first 3 cards to the seen set
			if(i < 3) {
				suggester.addSeen(card);
			}
			//Add the last 3 to the player's hand, which should update the seen set
			else {
				suggester.updateHand(card);
			}
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
	
	/**
	 * Test to verify that the computer player moves to new locations on the board correctly, prioritizing entering new 
	 * rooms that have yet to be seen, otherwise randomly selecting a target from the list.
	 */
	@Test
	public void testSelectTarget() {
		//Create a new test player
		ComputerPlayer player = new ComputerPlayer("Test Player", Color.black);
		//We will always calculate a roll-length of 2 starting from (11,16) on the board.
		board.calcTargets(board.getCell(11, 16), 2);
		/*
		 * This should give us 5 targets, the center of the pool room and 4 walkway tiles. Since our player does not have
		 * a seen card for the pool room, he must always go to the pool room.
		 */
		//Test an arbitrarily sized sample group.
		Room room = board.getRoom('O');
		BoardCell roomCell = room.getCenterCell();
		for(int i = 0; i < 200; i++) {
			//The only cell returned must be the center of the pool room.
			assertEquals(roomCell, player.selectTarget());
		}
		
		//Now, add the room's card to the seen list.
		player.addSeen(new Card(room.getName(), CardType.ROOM));
		//Now, any cell returned by board.getTargets() is a valid location to go to.
		Set<BoardCell> targets = board.getTargets();
		//Create a map to hold counters for how often each target is encountered.
		Map<BoardCell, Integer> counters = new HashMap<BoardCell, Integer>();
		for(BoardCell target : targets) {
			counters.put(target, 0);
		}
		//Test an arbitrarily sized sample group
		for(int i = 0; i < 200; i++) {
			//Get the cell returned by the computer player
			BoardCell returnedCell = player.selectTarget();
			//All returned cells must be valid elements from targets
			assertTrue(targets.contains(returnedCell));
			//Increment the respective counters
			counters.put(returnedCell, counters.get(returnedCell) + 1);
		}
		//Ensure that all counter entries > 0.
		for(BoardCell target : targets) {
			assertTrue(counters.get(target) > 0);
		}
		
		//Now, calculate a roll of length 4 from (9, 21)
		board.calcTargets(board.getCell(9, 21), 4);
		/*
		 * The targets list should include 2 rooms, neither of which have been seen. They must be randomly chosen 
		 * between as the only valid returns for selectTarget().
		 */
		BoardCell study = board.getRoom('S').getCenterCell();
		BoardCell closet = board.getRoom('C').getCenterCell();
		//Add the cells to a set for easy testing.
		Set<BoardCell> rooms = new HashSet<BoardCell>();
		rooms.add(study);
		rooms.add(closet);
		//Test and arbitrarily sized sample group
		int studyCount = 0, closetCount = 0;
		for(int i = 0; i < 200; i++) {
			BoardCell returnedCell = player.selectTarget();
			//The returned cell must be either of the two rooms.
			assertTrue(rooms.contains(returnedCell));
			//Increment the counters properly.
			if(returnedCell.equals(study)) {
				studyCount += 1;
			}else if (returnedCell.equals(closet)) {
				closetCount += 1;
			}
		}
		//Both counts must be greater than 0
		assertTrue(studyCount > 0);
		assertTrue(closetCount > 0);
	}
}

