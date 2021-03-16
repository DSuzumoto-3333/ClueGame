package gameEngine;

import java.awt.Color;
import java.util.Set;
import java.util.HashSet;

public abstract class Player {
	private String name;
	private Color color;
	private int row, col;
	private Set<Card> hand;
	
	public Player() {
		
	}
	
	public abstract void updateHand(Card card);
	
	public abstract Set<Card> getHand();
}
