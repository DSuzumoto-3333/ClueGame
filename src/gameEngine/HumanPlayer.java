package gameEngine;

import java.awt.Color;
import java.util.Set;
import java.util.HashSet;

public class HumanPlayer extends Player{

	public HumanPlayer(String name, Color color) {
		super(name, color);
	}

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
