package gameEngine;

import java.util.Set;
import java.util.HashSet;

/**
 * Represents a single square on the game board. Holds the cell's position, references to all tiles directly adjacent to it, the room it is stored in, if any,
 * and whether the tile is occupied. 
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 */
public class BoardCell {
	//Cell Position
	private int rowPos, colPos;
	//Determine if player is occupying the cell
	private boolean isOccupied;
	//The room the cell is in, if any, and variables pertaining to rooms.
	private boolean isInRoom;
	private DoorDirection doorDirection;
	private char roomInitial;
	private boolean roomLabel;
	private boolean roomCenter;
	private char secretPassage;
	//Set of cells adjacent to the current cell. 
	private Set<BoardCell> adjacentCells;
	
	/**
	 * Initializes a game tile that is not a part of any room. The position, occupancy, and initial are set here for walkway and unused tiles.
	 * To instantiate a room tile, setRoom must be called with the proper parameters.
	 * @param rowPos - The row that the BoardCell occupies.
	 * @param colPos - The column that the boardCell occupies.
	 */
	public BoardCell(int rowPos, int colPos) {
		super();
		
		//Set the provided position
		this.rowPos = rowPos;
		this.colPos = colPos;
		
		//Set certain traits to false by default
		isOccupied = false;		
		isInRoom = false;
		roomLabel = false;
		roomCenter = false;
		doorDirection = DoorDirection.NONE;
		
		//Initialize the adjacency set
		adjacentCells = new HashSet<BoardCell>();
		secretPassage = 'X';
	}
	
	/**
	 * Used to initialize a room instance. Carries all the data a room tile needs at minimum.
	 * @param initial - The initial char of the room this tile is a part of.
	 * @param label - A boolean representing if the cell is the label cell of the room.
	 * @param center - A boolean representing if the cell is the center cell of the room.
	 */
	
	public void setRoom(char initial, boolean label, boolean center) {
		//If a tile is set as a room tile, ensure the tile is labels as in a room.
		isInRoom = true;
		//Set the provided parameters.
		roomInitial = initial;
		roomLabel = label;
		roomCenter = center;
	}
	
	/**
	 * If a room tile is a door, give it a DoorDirection that is not NONE
	 * @param d - A value from the DoorDirection enumeration. Default for a board cell is NONE, but if the cell is a door, it's direction must be set to UP, DOWN, LEFT, or RIGHT here.
	 */
	public void setDoor(DoorDirection d) {
		doorDirection = d;
	}
	
	/**
	 * Initialize a walkway tile, all room related data is set to false and it's given the label W. This is a void method, and returns nothing.
	 */
	public void setWalkway() {
		roomInitial = 'W';
	}
	
	/**
	 * Initialize an unusable tile, all room related data is set to false and it's given the label X. This is a void method, and returns nothing.
	 */
	public void setUnused() {
		roomInitial = 'X';
	}
	
	/**
	 * If the tile is a secret passage, set the char to the connected room's character.
	 * @param passage - The char initial of the room this secret passage connects to, or the 2nd character in the tile's string representation in the config files.
	 */
	public void setSecretPassage(char passage) {
		secretPassage = passage;
	}
	
	/**
	 * Adds a cell to the list of cells adjacent to the callee cell.
	 * @param cell - A board cell that will be added to the adjacency list of the callee cell.
	 */
	public void addAdjacency(BoardCell cell) {
		adjacentCells.add(cell);
	}
	
	/**
	 * Returns the list of all cells adjacent to the callee cell.
	 * @return - A Hash Set of Board Cells representing the tiles that are considered "next to" the callee cell
	 */
	public Set<BoardCell> getAdjList(){
		return adjacentCells;
	}
	
	/**
	 * Returns true if the tile is part of a room, false if not.
	 * @return - A boolean representing if the cell is in a room or not.
	 */
	public boolean getisInRoom() {
		return isInRoom;
	}
	
	/**
	 * Returns whether or not a room is occupied.
	 * @return - A boolean representing if the cell is occupied or not.
	 */
	public boolean getOccupied() {
		return isOccupied;
	}

	/**
	 * Sets the tile to occupied if passed in true, un-sets if tile is occupied if passed in false.
	 * @param isOccupied - A boolean representing the occupancy of the tile.
	 */
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
	/**
	 * Returns whether or not the cell is a doorway.
	 * @return - A boolean representing if the tile is a doorway.
	 */
	public boolean isDoorway() {
		switch(doorDirection) {
		case NONE:
			return false;
		default:
			return true;
		}
	}
	
	/**
	 * Returns the door direction, DoorDirection.None if none
	 * @return - A DoorDirection enumeration representing the direction of the door on the cell, if any.
	 */
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	/**
	 * Returns whether the tile is the room center or not.
	 * @return - A boolean representing if the tile is the room center.
	 */
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	/**
	 * Returns whether the tile is where the room label is displayed.
	 * @return - A boolean representing if the tile is the room label cell.
	 */
	public boolean isLabel() {
		return roomLabel;
	}
	
	/**
	 * Returns the secret passage character for the tile.
	 * @return - A char representing the room the secret passage links to, or 'X' if the tile is not a secret passage.
	 */
	public char getSecretPassage() {
		return secretPassage;
	}
	
	/**
	 * Returns the initial of the room the tile is in, 'W' for walkway tiles, and 'X' for unused ones. 
	 * @return - A char representing the initial of the tile, 'W' for walkway, 'X' for unused, and the initial of the room the tile is a part of for any other case.
	 */
	public char getInitial() {
		return roomInitial;
	}
}
