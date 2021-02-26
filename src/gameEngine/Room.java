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
	 * @param name
	 */
	public Room(String name) {
		super();
		this.name = name;
	}
	/**
	 * Sets the center cell to whatever cell is passed in.
	 * @param c
	 */
	public void setCenterCell(BoardCell c) {
		centerCell = c;
	}
	
	/**
	 * Sets the label cell to whatever cell is passed in.
	 * @param l
	 */
	public void setLabelCell (BoardCell l) {
		labelCell = l;
	}
	
	/**
	 * Returns the name of the room
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the center cell of the room.
	 * @return
	 */
	public BoardCell getCenterCell() {
		return centerCell;
	}
	
	/**
	 * Returns the label cell of the room.
	 * @return
	 */
	public BoardCell getLabelCell() {
		return labelCell;
	}
	
}
