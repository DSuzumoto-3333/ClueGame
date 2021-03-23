package gameEngine;

import java.awt.Color;
import java.util.Set;
import java.util.HashSet;
/**
 * Object that simulates another player in the game, ran entirely by the computer. Contains much of the same data 
 * and functions as HumanPlayer, so the Player abstract class is shared between them.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 */
public class ComputerPlayer extends Player{
	/**
	 * The NPC version of the Player class. Has the capability to make suggestions for the game without input, and is
	 * played entirely by the computer.
	 * @param playerName - The name of the player.
	 * @param playerColor - The color used to signify the player.
	 */
	public ComputerPlayer(String playerName, Color playerColor) {
		super(playerName, playerColor);
	}
	
	@Override
	/**
	 * A simple comparison method to determine if player objects are equivalent
	 * @param player - Another player object.
	 */
	public boolean equals(Player player) {
		//Ensure the player object is the same type (not HumanPlayer), and that they have identical names and colors.
		if(player instanceof ComputerPlayer &&
			player.getName().equals(this.getName()) &&
			player.getColor().equals(this.getColor())) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * A method used to create a suggestion set based on all the cards the player has not seen, and the room that it
	 * currently occupies.
	 * @return - A set of cards making up a suggestion for the game.
	 */
	public Set<Card> createSuggestion(){
		//Create a new set to hold the suggestion.
		Set<Card> suggestion = new HashSet<Card>();
		//Get the name of the room that the player occupies as a string.
		BoardCell pos = this.getPosition();
		Board board = Board.getInstance();
		Room room = board.getRoom(pos);
		String roomName = room.getName();
		//Add a card based on the name of the room the player occupies
		suggestion.add(new Card(roomName, CardType.ROOM));
		
		
		return suggestion;
	}
}