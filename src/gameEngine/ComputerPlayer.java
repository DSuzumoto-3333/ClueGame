package gameEngine;

import java.awt.Color;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
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
		//Create a copy of the deck.
		ArrayList<Card> deckCopy = new ArrayList<Card>(Board.getInstance().getDeck());
		//Get the seen list
		Set<Card> seen = getSeen();
		//Iterate through the deck and the seen list
		for(Card dCard : Board.getInstance().getDeck()) {
			for(Card sCard : seen) {
				//Any elements that have already been seen or that are room cards are removed.
				if (dCard.equals(sCard) || dCard.getType().equals(CardType.ROOM)) {
					deckCopy.remove(dCard);
					break;
				}
			}
		}
		
		//Create 2 sets to hold all the remaining weapon and person cards.
		Set<Card> weapons = new HashSet<Card>();
		Set<Card> people = new HashSet<Card>();
		//Separate deckCopy into sets based on their card type.
		for(Card card : deckCopy) {
			switch(card.getType()) {
			case PERSON:
				people.add(card);
				break;
			case WEAPON:
				weapons.add(card);
				break;
			default:
				break;
			}
		}
		
		//If either set is empty, break by returning the empty set.
		if(people.size() == 0 || weapons.size() == 0) {
			return suggestion;
		}
		
		//If not, populate the suggestion set.
		else {
			//Create a random number generator, and pick a random number
			Random random = new Random();
			int rand = random.nextInt(weapons.size());
			//Iterate through weapons, incrementing i until the random element selected is found.
			int i = 0;
			for(Card card : weapons) {
				if(i == rand) {
					//Add the randomly selected element to the suggestion set.
					suggestion.add(card);
					break;
				}
				i++;
			}
			
			//Pick another random number
			rand = random.nextInt(people.size());
			i = 0;
			//Iterate through people, incrementing i until the random element selected is found.
			for(Card card : people) {
				if(i == rand) {
					//Add the randomly selected element to the suggestion set.
					suggestion.add(card);
					break;
				}
				i++;
			}

			//Get the name of the room that the player occupies as a string.
			BoardCell pos = this.getPosition();
			Board board = Board.getInstance();
			Room room = board.getRoom(pos);
			String roomName = room.getName();
			//Add a card based on the name of the room the player occupies
			suggestion.add(new Card(roomName, CardType.ROOM));
			
			//Return the suggestion.
			return suggestion;
		}
		
	}
}