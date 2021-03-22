package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import gameEngine.*;
import junit.framework.Assert;

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
		Card A1 = new Card("Weapon", CardType.WEAPON);
		Card A2 = new Card("Person", CardType.PERSON);
		Card A3 = new Card("Room", CardType.ROOM);
		Card W1 = new Card("wrongWeapon", CardType.WEAPON);
		Card W2 = new Card("wrongPerson", CardType.PERSON);
		Card W3 = new Card("wrongRoom", CardType.ROOM);
		//Give the board instance the proper solution
		Set<Card> soln = new HashSet<Card>();
		soln.add(A1);
		soln.add(A2);
		soln.add(A3);
		board.setSolution(soln);
		//Ensure that checking a correct accusation returns true
		assertTrue(board.checkAccusation(soln));
		//Modify the accusation to have the incorrect weapon.
		soln.remove(A1);
		soln.add(W1);
		//Ensure that the accusation is false
		assertFalse(board.checkAccusation(soln));
		//Modify the accusation to have the incorrect person
		soln.remove(W1);
		soln.add(A1);
		soln.remove(A2);
		soln.add(W2);
		//Ensure the accusation is still false
		assertFalse(board.checkAccusation(soln));
		//Modify the accusation to have the incorrect person
		soln.remove(W2);
		soln.add(A2);
		soln.remove(A3);
		soln.add(W3);
		//Ensure the accusation is still false
		assertFalse(board.checkAccusation(soln));
	}
}

