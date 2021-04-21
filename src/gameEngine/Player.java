package gameEngine;

import java.awt.Color;
import java.awt.Graphics;
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
	private int rowPos, colPos;
	//The player's name
	private String name;
	//A color to signify the player
	private Color color;
	//The player's hand, and the maximum number of cards they can hold, 3
	private Set<Card> hand;
	public static final int MAX_CARDS = 3;
	//A set of all cards seen by the players
	private Set<Card> seen;
	//A set to hold the player's accusation
	private Set<Card> accusation;
	//The game board, for use with calculating moves and suggestions
	private Board board = Board.getInstance();
	//A boolean to indicate if the player can stay in the room they are in (set if they are dragged to a room via a suggestion)
	private boolean canStayOnTile;
	//A static int to calculate the tile offset for a player instance
	static int playerOffsetIndex = -3;
	//The individual player's offset to be drawn.
	private int drawOffset;
	
	/**
	 * The player object is used to represent either a Human player or an NPC. It contains the ability to hold cards,
	 * disprove suggestions, and move about the board.
	 * @param playerName - The name of the player
	 * @param playerColor - The color the player will be indicated by.
	 */
	public Player(String playerName, Color playerColor, int row, int col) {
		super();
		name = playerName;
		color = playerColor;
		hand = new HashSet<Card>();
		seen = new HashSet<Card>();
		rowPos = row;
		colPos = col;
		drawOffset = playerOffsetIndex * 5;
		playerOffsetIndex += 1;
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
	 * Method to draw players on the game board
	 * @param tileWidth - The width of each tile being drawn on the game board
	 * @param tileHeight - The height of each tile being drawn
	 * @param g - The graphics to draw on.
	 */
	public void draw(int tileWidth, int tileHeight, Graphics g) {
		//Determine if the player is in a room.
		if(board.getCell(rowPos, colPos).isRoomCenter()) {
			//Draw a circle/oval over the tile that the player occupies with the necessary room offset.
			g.setColor(color);
			g.fillOval(colPos * tileWidth + drawOffset, rowPos * tileHeight, tileWidth, tileHeight);
		}else {
			//Draw a circle/oval over the tile that the player occupies.
			g.setColor(color);
			g.fillOval(colPos * tileWidth, rowPos * tileHeight, tileWidth, tileHeight);
		}
	}
	
	/**
	 * Abstract method for players to move.
	 */
	public abstract void move();
	
	/**
	 * A method to add a card to the player's hand.
	 * @param card - The card to be added, assuming their hand is not already full.
	 */
	public void updateHand(Card card) {
		if(!(hand.size() == MAX_CARDS)) {
			hand.add(card);
			seen.add(card);
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
		seen.addAll(hand);
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
		rowPos = position.getRow();
		colPos = position.getCol();
	}
	/**
	 * Method to get the player's position.
	 */
	public BoardCell getPosition() {
		return board.getCell(rowPos, colPos);
	}
	
	/**
	 * Method to add a card to the seen set, shared by all players.
	 * @param card - The card that has been seen.
	 */
	public void addSeen(Card card) {
		seen.add(card);
	}
	
	/**
	 * Method to get the set of all cards this particular player has seen.
	 * @return - Set of cards representing the cards this player has seen.
	 */
	public Set<Card> getSeen() {
		return seen;
	}
	
	/**
	 * Method to get the instance of the game board saved in the player object.
	 * @return - A Board object representing the current game board.
	 */
	public Board getBoard() {
		return board;
	}
	/**
	 * Method to set the players accusation set when they are ready to make an accusation.
	 * @param accusationSet - A set of cards representing a possible solution.
	 */
	public void setAccusation(Set<Card> accusationSet) {
		accusation = accusationSet;
	}
	
	/**
	 * Method to return the player's accusation.
	 * @return - A set of 3 cards representing a possible solution.
	 */
	public Set<Card> getAccusation(){
		return accusation;
	}
	
	/**
	 * A method to determine if one player is equal to another. Abstract since only HumanPlayer and ComputerPlayer
	 * can be instantiated, and require different comparisons.
	 * @param player - The player to be compared.
	 * @return
	 */
	public abstract boolean equals (Player player);
	
	/**
	 * A method to set the flag denoting that the player may stay in the room they are currently in (called when the
	 * player is dragged to a room via a suggestion.)
	 * @param canStay - Boolean whether or not a player can stay on their tile for the next turn.
	 */
	public void setCanStay(boolean canStay) {
		canStayOnTile = canStay;
	}
	
	/**
	 * A method to get the flag denoting if a player may remain in the room they are in for the current turn.
	 * @return - A boolean, true if the player may stay on the tile they are currently occupying.
	 */
	public boolean getCanStay() {
		return canStayOnTile;
	}
}
