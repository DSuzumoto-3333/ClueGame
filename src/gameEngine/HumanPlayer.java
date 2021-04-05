package gameEngine;

import java.awt.Color;
import java.util.Set;
import java.util.HashSet;

/**
 * A simple class to contain methods unique to the Human player.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 *
 */
public class HumanPlayer extends Player{

	public HumanPlayer(String name, Color color, int row, int col) {
		super(name, color, row, col);
	}
	
	/**
	 * Overrides .equals in Player.java to ensure that the compared Player object is an instance of HumanPlayer, and not
	 * ComputerPlayer.
	 */
	@Override
	public boolean equals(Player player) {
		if(player instanceof HumanPlayer &&
			player.getName().equals(this.getName()) &&
			player.getColor().equals(this.getColor())) {
			return true;
		}else {
			return false;
		}
	}
}
