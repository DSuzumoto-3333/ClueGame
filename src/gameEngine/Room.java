package gameEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Represents a room on the game board. Holds information for which tile is the center and which tile is the label tile.
 * @author Derek Suzumoto
 *
 */
public class Room {
	private String name;
	private BoardCell centerCell, labelCell;
	
	/**
	 * Constructor that creates a room object with the name provided
	 * @param name - The name the room will be identified by.
	 */
	public Room(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * Method to draw labels in all rooms on the proper label cell.
	 * @param tileWidth - Width of each tile on the board (For calculating offset)
	 * @param tileHeight - Height of each tile on the board (For calculating offset)
	 * @param g - The Graphics to draw on.
	 */
	public void draw(int tileWidth, int tileHeight, Graphics g) {
		//Ensure the room has a label cell, as Walkway and Unused do not.
		if(!(labelCell == null)) {
			//Set the color and font
			g.setColor(Color.blue);
			Font font = new Font("Ariel", Font.PLAIN, 18);
			g.setFont(font);
			//Draw the name starting at the label cell.
			g.drawString(name, labelCell.getCol() * tileWidth, labelCell.getRow() * tileHeight);
		}
	}
	/**
	 * Sets the center cell to whatever cell is passed in.
	 * @param c - A board cell that will be the only navigable cell in the room, and hold adjacency with all the doors and secret passages connected to the room.
	 */
	public void setCenterCell(BoardCell c) {
		centerCell = c;
	}
	
	/**
	 * Sets the label cell to whatever cell is passed in.
	 * @param l - A board cell that will be used to determine where to draw the room's label.
	 */
	public void setLabelCell (BoardCell l) {
		labelCell = l;
	}
	
	/**
	 * Returns the name of the room
	 * @return - A string representing the room's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the center cell of the room.
	 * @return - A board cell that acts as the only navigable cell in the room, and holds the adjacency list with all the doors and secret passages.
	 */
	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	/**
	 * Returns the label cell of the room.
	 * @return - A board cell that represents where the label of the room is drawn.
	 */
	public BoardCell getLabelCell() {
		return labelCell;
	}
	
}
