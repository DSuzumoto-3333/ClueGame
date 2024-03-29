package tests;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.awt.Color;

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

class GameSetupTests {
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
		ArrayList<Player> players = board.getPlayers();
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
			i++;
		}
		
		//Check each card in cards against each card in correctCards
		Set<Card> foundInBoth = new HashSet<Card>();
		for(Card card : cards) {
			for(Card card2 : correctCards) {
				if(card.equals(card2)) {
					foundInBoth.add(card);
				}
			}
		}
		
		//Each card in correctCards should've found a match in cards.
		assertEquals(21, foundInBoth.size());
		
	}
	
	/**
	 * Test to ensure that all player objects are loaded, are of the appropriate type, and are fully and correctly filled out.
	 */
	@Test
	public void testPlayers() {
		//Get the set of players from the board.
		ArrayList<Player> players = board.getPlayers();
		
		//The set should have 6 objects,
		assertEquals(6, players.size());
		//Count the instances of both types of player objects
		int npcCount = 0;
		int plrCount = 0;
		for(Player player : players) {
			if(player instanceof ComputerPlayer) {
				npcCount ++;
			}else if(player instanceof HumanPlayer) {
				plrCount ++;
			}
		}
		//There should be 5 ComputerPlayers and 1 HumanPlayer
		assertEquals(5,npcCount);
		assertEquals(1,plrCount);
		
		//Ensure the players have the proper data
		//Create a hard-coded list of correct player objects.
		Set<Player> correctPlayers = new HashSet<Player>();
		//Create all the necessary colors
		Color color1 = new Color(136, 92, 194);
		Color color2 = new Color(247, 39, 34);
		Color color3 = new Color(60, 141, 47);
		Color color4 = new Color(255, 192, 203);
		Color color5 = new Color(37, 78, 165);
		Color color6 = new Color(255, 116, 0);
		//Create correct player objects with the proper names and colors.
		correctPlayers.add(new HumanPlayer("Ramona Rodriguez", color1, 0, 0));
		correctPlayers.add(new ComputerPlayer("Leland Blake", color2, 0, 0));
		correctPlayers.add(new ComputerPlayer("Irene Wright", color3, 0, 0));
		correctPlayers.add(new ComputerPlayer("Blake Greene", color4, 0, 0));
		correctPlayers.add(new ComputerPlayer("Rosalie Vaughn", color5, 0, 0));
		correctPlayers.add(new ComputerPlayer("Fernando Elliot", color6, 0, 0));
		
		//Check each player in players against each player in correctPlayers. If they are equal, add to foundInBoth.
		Set<Player> foundInBoth = new HashSet<Player>();
		//Ensure that every player is properly loaded in.
		int i = 0;
		for(Player player : players) {
			for(Player player2 : correctPlayers) {
				if(player.equals(player2)) {
					foundInBoth.add(player);
				}
			}
		}
		//All 6 players should appear here.
		for(Player player : foundInBoth) {
			System.out.println(player.getName());
		}
		assertEquals(6, foundInBoth.size());	
	}
	/**
	 * Test to ensure that the player's hand updates and behaves as expected.
	 */
	@Test
	public void testPlayerHand() {
		//Create a dummy human player.
		Player testPlayer = new HumanPlayer("Test", Color.BLACK, 0, 0);
		//Get it's hand.
		Set<Card> testHand = testPlayer.getHand();
		//It should be empty at the start.
		assertEquals(new HashSet<Card>(), testHand);
		
		//Define a small deck of cards to work with.
		Card[] cards = new Card[4];
		cards[0] = new Card("Card0", CardType.ROOM);
		cards[1] = new Card("Card1", CardType.ROOM);
		cards[2] = new Card("Card2", CardType.ROOM);
		cards[3] = new Card("Card3", CardType.ROOM);
		
		//Give the dummy the first card, and get it's hand again.
		testPlayer.updateHand(cards[0]);
		testHand = testPlayer.getHand();
		//It should be of length one and contain cards[0]
		assertEquals(1, testHand.size());
		assertTrue(testHand.contains(cards[0]));
		
		//Give the dummy the next card, and get it's hand again.
		testPlayer.updateHand(cards[1]);
		testHand = testPlayer.getHand();
		//It should be of length two and contain cards[0] and cards[1]
		assertEquals(2, testHand.size());
		assertTrue(testHand.contains(cards[0]));
		assertTrue(testHand.contains(cards[1]));
		
		//Give the dummy the next card, and get it's hand again.
		testPlayer.updateHand(cards[2]);
		testHand = testPlayer.getHand();
		//It should be of length three and contain cards[0]-cards[2]
		assertEquals(3, testHand.size());
		assertTrue(testHand.contains(cards[0]));
		assertTrue(testHand.contains(cards[1]));
		assertTrue(testHand.contains(cards[2]));
		
		//Try to give the dummy the next card, and get it's hand again.
		testPlayer.updateHand(cards[3]);
		testHand = testPlayer.getHand();
		//It should be of length three and contain cards[0]-cards[2]. It should not hold the 4th card.
		assertEquals(3, testHand.size());
		assertTrue(testHand.contains(cards[0]));
		assertTrue(testHand.contains(cards[1]));
		assertTrue(testHand.contains(cards[2]));
	}

	/**
	 * Test to ensure that the solution to the game is always valid, and that cards are not both in the solution and the hand of a player.
	 */
	@Test
	public void testSolution() {
		// Make sure that the cards aren't in both the player's deck and the solution
		Set<Card> solution = board.getSolution();
		ArrayList<Player> players = board.getPlayers();
		
		// Verify the solution is the correct length
		assertEquals(3,solution.size());
		
		// ensures that each card in each players hand does not match with any card found in the solution
		for (Player player: players) {
			for (Card playerCard: player.getHand()) {
				for (Card solutionCard: solution) {
					assertFalse(playerCard.equals(solutionCard));
				}
			}
		}
		
		//Make sure the solution has one card of each type
		//Declare counters
		int roomCount = 0;
		int playerCount = 0;
		int weaponCount = 0;
		//Check every card
		for(Card card : solution) {
			if(card.getType() == CardType.PERSON) {
				playerCount++;
			} else if(card.getType() == CardType.ROOM) {
				roomCount++;
			} else if(card.getType() == CardType.WEAPON) {
				weaponCount++;
			}
		}
		//Ensure there is exactly one of each type
		assertEquals(1,roomCount);
		assertEquals(1,playerCount);
		assertEquals(1,weaponCount);
	}
	
}
