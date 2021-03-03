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
	 * @param rowPos
	 * @param colPos
	 */
	public BoardCell(int rowPos, int colPos) {
		super();
		//Set the provided position
		this.rowPos = rowPos;
		this.colPos = colPos;
		//Set occupied to false by default
		isOccupied = false;		
		doorDirection = DoorDirection.NONE;
		//Initialize the adjacency set
		adjacentCells = new HashSet<BoardCell>();
	}
	
	/**
	 * Used to initialize a room instance. Caries all the data a room tile needs at minimum.
	 * @param initial
	 * @param label
	 * @param center
	 */
	
	public void setRoom(char initial, boolean label, boolean center) {
		isInRoom = true;
		roomInitial = initial;
		roomLabel = label;
		roomCenter = center;
	}
	
	/**
	 * If a room tile is a door, give it a DoorDirection that is not NONE
	 * @param d
	 */
	public void setDoor(DoorDirection d) {
		doorDirection = d;
	}
	
	/**
	 * Initialize a walkway tile, all room related data is set to false and it's given the label W
	 */
	public void setWalkway() {
		isInRoom = false;
		roomInitial = 'W';
		roomLabel = false;
		roomCenter = false;
	}
	
	/**
	 * Initialize an unusable tile, all room related data is set to false and it's given the label X
	 */
	public void setUnused() {
		isInRoom = false;
		roomInitial = 'X';
		roomLabel = false;
		roomCenter = false;
	}
	
	/**
	 * If the tile is a secret passage, set the char to the connected room's character.
	 * @param passage
	 */
	public void setSecretPassage(char passage) {
		secretPassage = passage;
	}
	
	/**
	 * Adds a cell to the list of cells adjacent to the callee cell.
	 * @param cell
	 */
	public void addAdjacency(BoardCell cell) {
		adjacentCells.add(cell);
	}
	
	/**
	 * Returns the list of all cells adjacent to the callee cell.
	 * @return
	 */
	public Set<BoardCell> getAdjList(){
		return adjacentCells;
	}
	
	/**
	 * Returns true if the tile is part of a room, false if not.
	 * @return
	 */
	public boolean getisInRoom() {
		return isInRoom;
	}
	
	/**
	 * Returns whether or not a room is occupied.
	 * @return
	 */
	public boolean getOccupied() {
		return isOccupied;
	}

	/**
	 * Sets the tile to occupied if passed in true, unsets if tile is occupied if passed in false.
	 * @param isOccupied
	 */
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
	/**
	 * Returns whether or not the cell is a doorway.
	 * @return
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
	 * @return
	 */
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
	/**
	 * Returns whether the tile is the room center or not.
	 * @return
	 */
	public boolean isRoomCenter() {
		return roomCenter;
	}
	
	/**
	 * Returns whether the tile is where the room label is displayed.
	 * @return
	 */
	public boolean isLabel() {
		return roomLabel;
	}
	
	/**
	 * Returns the secret passage character for the tile.
	 * @return
	 */
	public char getSecretPassage() {
		return secretPassage;
	}
	
	/**
	 * Returns the initial of the tile used to signify it.
	 * @return
	 */
	public char getInitial() {
		return roomInitial;
	}
	
}
