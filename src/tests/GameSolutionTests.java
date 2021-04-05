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
import java.util.ArrayList;
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
		Player player1 = new HumanPlayer("Player1", Color.blue, 0, 0);
		player1.updateHand(cPerson);
		player1.updateHand(wRoom);
		player1.updateHand(wWeapon);
		//calling disproveSuggestion() should return cPerson
		assertEquals(cPerson, player1.disproveSuggestion(sug));

		//Create a test person and populate their hand with the room from the suggestion.
		Player player2 = new HumanPlayer("Player2", Color.blue, 0, 0);
		player2.updateHand(wPerson);
		player2.updateHand(cRoom);
		player2.updateHand(wWeapon);
		//calling disproveSuggestion() should return cRoom
		assertEquals(cRoom, player2.disproveSuggestion(sug));
		
		//Create a test person and populate their hand with the weapon from the suggestion.
		Player player3 = new HumanPlayer("Player3", Color.blue, 0, 0);
		player3.updateHand(wPerson);
		player3.updateHand(wRoom);
		player3.updateHand(cWeapon);
		//calling disproveSuggestion() should return cWeapon
		assertEquals(cWeapon, player3.disproveSuggestion(sug));
		
		//Create a test person and populate their hand with none of the cards from the suggestion.
		Player player4 = new HumanPlayer("Player4", Color.blue, 0, 0);
		player4.updateHand(wPerson);
		player4.updateHand(wRoom);
		player4.updateHand(wWeapon);
		//calling disproveSuggestion() should return null
		assertEquals(null, player4.disproveSuggestion(sug));
		
		//Create a test person and populate their hand with two of the cards from the suggestion.
		Player player5 = new HumanPlayer("Player4", Color.blue, 0, 0);
		player5.updateHand(cPerson);
		player5.updateHand(wRoom);
		player5.updateHand(cWeapon);
		//calling disproveSuggestion() should return either cPerson or cWeapon
		Card returned = player5.disproveSuggestion(sug);
		assertTrue((returned.equals(cPerson)) || (returned.equals(cWeapon)));
	}
	
	/**
	 * Test to ensure that suggestions are handled in Board.java properly
	 */
	@Test
	public void testSuggestions() {
		//Get the list of players from the board.
		ArrayList<Player> players = board.getPlayers();
		//Create a "deck" of cards, capable of having solutions and non-solutions (repeat cards are unimportant in this case)
		Card cPerson = new Card("Correct Person", CardType.PERSON);
		Card cRoom = new Card("Correct Room", CardType.ROOM);
		Card cWeapon = new Card("Correct Weapon", CardType.WEAPON);
		Card wPerson = new Card("Wrong Person", CardType.PERSON);
		Card wRoom = new Card("Wrong Room", CardType.ROOM);
		Card wWeapon = new Card("Wrong Weapon", CardType.WEAPON);
		//Set up the suggestion set
		Set<Card> sug = new HashSet<Card>();
		sug.add(cPerson);
		sug.add(cRoom);
		sug.add(cWeapon);
		
		//Make sure no players have cards that would dispute the suggestion
		Set<Card> hand = new HashSet<Card>();
		hand.add(wPerson);
		hand.add(wRoom);
		hand.add(wWeapon);
		for(Player player : players) {
			player.setHand(hand);
		}
		//The handleSuggestion() method should return null (We are using player 6 as the accuser)
		assertEquals(null, board.handleSuggestion(sug, players.get(5)));
		
		//Give the accuser a card that would allow them to disprove their own suggestion.
		hand.remove(wPerson);
		hand.add(cPerson);
		players.get(5).setHand(hand);
		//Ensure that null is still returned, as a player shouldn't be able to debunk their own suggestion.
		assertEquals(null, board.handleSuggestion(sug, players.get(5)));
		
		//Give the first player (HumanPlayer instance) a correct person card
		hand.remove(wPerson);
		hand.add(cPerson);
		players.get(0).setHand(hand);
		//The method should now return cPerson
		assertEquals(cPerson, board.handleSuggestion(sug, players.get(5)));
		
		//Set the first player's hand back to all wrong cards.
		hand.remove(cPerson);
		hand.add(wPerson);
		players.get(0).setHand(hand);
		//Give the 3rd player the correct room card
		hand.remove(wRoom);
		hand.add(cRoom);
		players.get(2).setHand(hand);
		//Give the 5th player the correct weapon card
		hand.remove(cRoom);
		hand.add(wRoom);
		hand.remove(wWeapon);
		hand.add(cWeapon);
		players.get(4).setHand(hand);
		//The 3nd player should beat the 5th to challenging the suggestion, and the method should return cRoom
		assertEquals(cRoom, board.handleSuggestion(sug, players.get(5)));
	}
}

