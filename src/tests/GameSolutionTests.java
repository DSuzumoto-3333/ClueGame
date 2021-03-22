package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import gameEngine.*;
import junit.framework.Assert;
import java.awt.Color;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;

class GameSolutionTests {


	private static Board board;

	@BeforeEach
	public void initializeBoard() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	/**
	 * Test used to validate the checkAccusation method in the Board class.
	 */
	@Test
	public void testAccusations() {
		//Initialize all necessary card objects
		Card correctWeapon = new Card("Weapon", CardType.WEAPON);
		Card correctPerson = new Card("Person", CardType.PERSON);
		Card correctRoom = new Card("Room", CardType.ROOM);
		Card wrongWeapon = new Card("wrongWeapon", CardType.WEAPON);
		Card wrongPerson = new Card("wrongPerson", CardType.PERSON);
		Card wrongRoom = new Card("wrongRoom", CardType.ROOM);
		//Give the board instance the proper solution
		Set<Card> soln = new HashSet<Card>();
		soln.add(correctWeapon);
		soln.add(correctPerson);
		soln.add(correctRoom);
		board.setSolution(soln);
		
		//Ensure that checking a correct accusation returns true
		assertTrue(board.checkAccusation(soln));
		
		//Modify the accusation to have the incorrect weapon.
		soln.remove(correctWeapon);
		soln.add(wrongWeapon);
		//Ensure that the accusation is false
		assertFalse(board.checkAccusation(soln));
		
		//Modify the accusation to have the incorrect person
		soln.remove(wrongWeapon);
		soln.add(correctWeapon);
		soln.remove(correctPerson);
		soln.add(wrongPerson);
		//Ensure the accusation is still false
		assertFalse(board.checkAccusation(soln));
		
		//Modify the accusation to have the incorrect person
		soln.remove(wrongPerson);
		soln.add(correctPerson);
		soln.remove(correctRoom);
		soln.add(wrongRoom);
		
		//Ensure the accusation is still false
		assertFalse(board.checkAccusation(soln));
	}
	
	/**
	 * Test used to validate the function of the disproveSuggestion() method.
	 */
	@Test
	public void testDisproveSuggestion() {
		//Create all the necessary cards for testing
		Card cPerson = new Card("correctPerson", CardType.PERSON);
		Card cRoom = new Card("correctRoom", CardType.ROOM);
		Card cWeapon = new Card("correctWeapon", CardType.WEAPON);
		Card wPerson = new Card("wrongPerson", CardType.PERSON);
		Card wRoom = new Card("wrongRoom", CardType.ROOM);
		Card wWeapon = new Card("wrongWeapon", CardType.WEAPON);
		//Add the "Correct" cards to the suggestion.
		Set<Card> sug = new HashSet<Card>();
		sug.add(cPerson);
		sug.add(cRoom);
		sug.add(cWeapon);
		
		//Create a test person and populate their hand with the person from the suggestion.
		Player player1 = new HumanPlayer("Player1", Color.blue);
		player1.updateHand(cPerson);
		player1.updateHand(wRoom);
		player1.updateHand(wWeapon);
		//calling disproveSuggestion() should return cPerson
		assertEquals(cPerson, player1.disproveSuggestion(sug));

		//Create a test person and populate their hand with the room from the suggestion.
		Player player2 = new HumanPlayer("Player2", Color.blue);
		player2.updateHand(wPerson);
		player2.updateHand(cRoom);
		player2.updateHand(wWeapon);
		//calling disproveSuggestion() should return cRoom
		assertEquals(cRoom, player2.disproveSuggestion(sug));
		
		//Create a test person and populate their hand with the weapon from the suggestion.
		Player player3 = new HumanPlayer("Player3", Color.blue);
		player3.updateHand(wPerson);
		player3.updateHand(wRoom);
		player3.updateHand(cWeapon);
		//calling disproveSuggestion() should return cWeapon
		assertEquals(cWeapon, player3.disproveSuggestion(sug));
		
		//Create a test person and populate their hand with none of the cards from the suggestion.
		Player player4 = new HumanPlayer("Player4", Color.blue);
		player4.updateHand(wPerson);
		player4.updateHand(wRoom);
		player4.updateHand(wWeapon);
		//calling disproveSuggestion() should return null
		assertEquals(null, player4.disproveSuggestion(sug));
		
		//Create a test person and populate their hand with two of the cards from the suggestion.
		Player player5 = new HumanPlayer("Player4", Color.blue);
		player5.updateHand(cPerson);
		player5.updateHand(wRoom);
		player5.updateHand(cWeapon);
		//calling disproveSuggestion() should return either cPerson or cRoom
		Set<Card> possibleReturns = new HashSet<Card>();
		possibleReturns.add(cPerson);
		possibleReturns.add(cRoom);
		assertTrue(possibleReturns.contains(player5.disproveSuggestion(sug)));
	}
}

