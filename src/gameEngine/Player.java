package gameEngine;

import java.awt.Color;
import java.util.Set;
import java.util.HashSet;

public abstract class Player {
	private int row, col;
	private String name;
	private Color color;
	private Set<Card> hand;
	public static final int MAX_CARDS = 3;
	
	public Player(String playerName, Color playerColor) {
		super();
		name = playerName;
		color = playerColor;
		hand = new HashSet<Card>();
	}
	
	public void updateHand(Card card) {
		if(!(hand.size() == MAX_CARDS)) {
			hand.add(card);
		}
	}
	
	public Set<Card> getHand(){
		return hand;
	}
	
	public boolean equals(Player p) {
		return false;
	}
}
