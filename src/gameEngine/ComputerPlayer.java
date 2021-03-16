package gameEngine;

import java.awt.Color;
import java.util.Set;
import java.util.HashSet;

public class ComputerPlayer extends Player{

	public ComputerPlayer(String name, Color color) {
		super(name, color);
	}
	
	@Override
	public boolean equals(Player player) {
		if(player instanceof ComputerPlayer &&
			player.getName().equals(this.getName()) &&
			player.getColor().equals(this.getColor())) {
			return true;
		}else {
			return false;
		}
	}
}