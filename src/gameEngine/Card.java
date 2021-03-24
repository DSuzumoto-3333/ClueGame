package gameEngine;

/**
 * Class representing a card object for use in the clue game. Cards contain a name string that elaborate what they are,
 * and an enumeration of either type ROOM, WEAPON, or PERSON to denote how they should be handled, and if they form a
 * valid solution together.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 */

public class Card {
	//Name of the room, weapon, or player the card represents.
	private String name;
	//The type of card, either ROOM, WEAPON, or PERSON.
	private CardType type;
	
	/**
	 * A constructor to create a new card from the name and type desired.
	 * @param cardName - A string containing the name of the card.
	 * @param cardType - A CardType Enum containing the proper type.
	 */
	public Card(String cardName, CardType cardType) {
		super();
		name = cardName;
		type = cardType;
	}
	
	/**
	 * A copy constructor to create a new card equivalent to an old one.
	 * @param card - The card to duplicate.
	 */
	public Card(Card card) {
		super();
		name = card.name;
		type = card.type;
	}
	
	/**
	 * Returns true if the provided card contains equivalent data to the current card.
	 * @param target - The card to compare.
	 * @return - True/False if cards are equivalent.
	 */
	public boolean equals(Card target) {
		if (target.name.equals(name) && target.type.equals(type)) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Method to get the card's CardType enum
	 * @return - CardType representing the type of card
	 */
	public CardType getType() {
		return type;
	}
	
	/**
	 * Method to get the name of the card as a String
	 * @return - A string containing the card's name.
	 */
	public String getName() {
		return name;
	}
	
}
