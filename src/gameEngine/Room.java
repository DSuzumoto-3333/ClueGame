package gameEngine;

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
