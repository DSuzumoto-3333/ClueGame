package gameEngine;

public abstract class Player {
	private String name;
	//private color Color;
	private int row, col;
	
	public Player() {
		
	}
	
	public abstract void updateHand(Card card);
}
