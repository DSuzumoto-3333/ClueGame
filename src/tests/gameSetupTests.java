package tests;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gameEngine.*;
/**
 * Test class to ensure that non-board related game items (mainly Cards and Players) are initialized from the setup file correctly. This class should not only ensure that the cards and
 * player objects exist, but that they are also filled out with proper names, colors, enumerations, etc.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 */

class gameSetupTests {
	private static Board board;
	
	/**
	 * Initialize the game board with the proper files for testing before any tests are conducted.
	 */
	@BeforeAll
	public static void init() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	/**
	 * This test should be used to validate that all 21 game cards are generated, and have the correct data.
	 */
	@Test
	void testCards() {
		//Get the set of players and the solution set from the board.
		Set<Player> players = board.getPlayers();
		Set<Card> cards = board.getSolution();
		//Ensure the sets are not empty.
		assertFalse(players == null);
		assertFalse(cards == null);
		//Add each player's hand to the cards set.
		for(Player player : players) {
			cards.addAll(player.getHand());
		}
		
		//Ensure there are exactly 21 cards.
		assertEquals(cards.size(), 21);
		
		//Ensure there are 9 room cards, 6 weapon cards, and 6 player cards.
		//Initialize counters.
		int roomCount = 0;
		int weapCount = 0;
		int plrCount = 0;
		//Check every card.
		for(Card card : cards) {
			switch(card.getType()){
			case PERSON:
				plrCount += 1;
				break;
			case ROOM:
				roomCount += 1;
				break;
			case WEAPON:
				weapCount += 1;
				break;
			}
		}
		//Ensure there are the proper amount of each type.
		assertEquals(roomCount, 9);
		assertEquals(plrCount, 6);
		assertEquals(weapCount, 6);
		
		//Ensure the cards are filled in as expected.
		//Create 21 cards that should be identical to the cards in the set.
		//Set an array containing all the names of the cards specified in the config file.
		String[] names = {	"Garden", "Kitchen", "Parlor", "Pantry", "Pool", "Ballroom", "Study", 
							"Closet", "Lounge", "Knife", "Sword", "Axe", "Pistol", "Hunting Rifle", 
							"Shurikens", "Ramona Rodriguez", "Leland Blake", "Irene Wright", 
							"Blake Greene", "Rosalie Vaughn", "Fernando Elliot" };
		//Create a set called correctCards of the expected deck.
		Set<Card> correctCards = new HashSet<Card>();
		//Populate correctCards
		int i = 0;
		for(String name : names) {
			if(i < 9) {
				correctCards.add(new Card(name, CardType.ROOM));
			}else if (i > 8 && i < 15){
				correctCards.add(new Card(name, CardType.WEAPON));
			}else {
				correctCards.add(new Card(name, CardType.PERSON));
			}
		}
		
		Set<Card> foundInBoth = new HashSet<Card>();
		for(Card card : cards) {
			for(Card card2 : correctCards) {
				if(card.equals(card2)) {
					foundInBoth.add(card);
					System.out.println(card.toString());
				}
			}
		}
		
		assertEquals(21, foundInBoth.size());
		
	}
	
	/**
	 * Test to ensure that all player objects are loaded, are of the appropriate type, and are fully and correctly filled out.
	 */
	
	/**
	 * Test to ensure that the player's hand updates and behaves as expected.
	 */
	
	/**
	 * Test to ensure that the solution to the game is always valid, and that cards are not both in the solution and the hand of a player.
	 */
}
