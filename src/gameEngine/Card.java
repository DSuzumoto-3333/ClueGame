package gameEngine;

public class Card {
	private String name;
	private CardType type;
	
	public Card(String cardName, CardType cardType) {
		super();
		name = cardName;
		type = cardType;
	}
	
	public Card(Card card) {
		super();
		name = card.name;
		type = card.type;
	}
	
	public boolean equals(Card target) {
		if (target.name.equals(name)) {
			return true;
		}else {
			return false;
		}
	}
	
	public CardType getType() {
		return type;
	}
	
}
