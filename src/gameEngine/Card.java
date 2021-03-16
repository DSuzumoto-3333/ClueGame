package gameEngine;

public class Card {
	private String name;
	private CardType type;
	
	public Card(String cardName, CardType cardType) {
		super();
		name = cardName;
		type = cardType;
	}
	
	public boolean equals(Card target) {
		return false;
	}
	
	public CardType getType() {
		return type;
	}
}
