package gameEngine;

import java.awt.Color;
import java.util.Set;
import java.util.HashSet;

/**
 * An abstract class to hold common methods and functions for HumanPlayer and ComputerPlayer objects.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 *
 */
public abstract class Player {
	//The player's current position cell
	private BoardCell pos;
	//The player's name
	private String name;
	//A color to signify the player
	private Color color;
	//The player's hand, and the maximum number of cards they can hold, 3
	private Set<Card> hand;
	public static final int MAX_CARDS = 3;
	//A set of all cards seen by the players
	private Set<Card> seen;
	/**
	 * The player object is used to represent either a Human player or an NPC. It contains the ability to hold cards,
	 * disprove suggestions, and move about the board.
	 * @param playerName - The name of the player
	 * @param playerColor - The color the player will be indicated by.
	 */
	public Player(String playerName, Color playerColor) {
		super();
		name = playerName;
		color = playerColor;
		hand = new HashSet<Card>();
		seen = new HashSet<Card>();
	}
	
	/**
	 * Method for determining if the player has any card from a given suggestion in their hand, thus disproving the
	 * suggestion. The program automatically returns the first match found, even if multiple matches are possible.
	 * @param suggestion - A set of cards representing the suggestion
	 * @return - The card held by the player that appears in the suggestion, or null.
	 */
	public Card disproveSuggestion(Set<Card> suggestion) {
		//Compare each card in the player's hand to each card in the suggestion
		for(Card handCard : hand) {
			for(Card sugCard : suggestion) {
				//If they are equal, return the match.
				if (handCard.equals(sugCard)) {
					return sugCard;
				}
			}
		}
		//If no match is found, return null.
		return null;
	}
	
	/**
	 * A method to add a card to the player's hand.
	 * @param card - The card to be added, assuming their hand is not already full.
	 */
	public void updateHand(Card card) {
		if(!(hand.size() == MAX_CARDS)) {
			hand.add(card);
		}
	}
	
	/**
	 * A method to get the player's hand
	 * @return - The player's hand.
	 */
	public Set<Card> getHand(){
		return hand;
	}
	
	/**
	 * A method to set the player's hand immediately. Used for testing.
	 * @param hand - The hand to be given to the player.
	 */
	public void setHand(Set<Card> hand) {
		this.hand = new HashSet<Card>(hand);
	}
	/**
	 * A method to get the player's name as a string.
	 * @return - The player's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * A method to get the color assigned to the player
	 * @return - The player's color as a java.awt.Color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Method to set the player object's position.
	 * @param position - The board cell the player occupies.
	 */
	public void setPosition(BoardCell position) {
		pos = position;
	}
	/**
	 * Method to get the player's position.
	 */
	public BoardCell getPosition() {
		return pos;
	}
	
	/**
	 * Method to add a card to the seen set, shared by all players.
	 * @param card - The card that has been seen.
	 */
	public void addSeen(Card card) {
		seen.add(card);
	}
	
	public Set<Card> getSeen() {
		return seen;
	}
	
	/**
	 * A method to determine if one player is equal to another. Abstract since only HumanPlayer and ComputerPlayer
	 * can be instantiated, and require different comparisons.
	 * @param player - The player to be compared.
	 * @return
	 */
	public abstract boolean equals (Player player);
}
