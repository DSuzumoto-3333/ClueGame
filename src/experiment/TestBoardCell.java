package experiment;

import java.util.Set;
import java.util.HashSet;

/**
 * A test class to understand the basic concepts of the code we'll be using to simulate the game board
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 */
public class TestBoardCell {
	//Cell Position
	private int rowPos, colPos;
	//Determine if player is occupying the cell
	private boolean isOccupied;
	//The room the cell is in, if any
	private RoomName room;
	//Set of cells adjacent to the current cell. 
	private Set<TestBoardCell> adjacentCells;
	
	/**
	 * Basic constructor, creates empty tile with no associated room. It is not occupied by default,
	 * and initializes the adjacentCells set as a HashSet.
	 * @param rowPos
	 * @param colPos
	 */
	public TestBoardCell(int rowPos, int colPos) {
		super();
		this.rowPos = rowPos;
		this.colPos = colPos;
		isOccupied = false;
		room = RoomName.NONE;
		adjacentCells = new HashSet<TestBoardCell>();
	}
	
	/**
	 * Constructor with Room provided. Creates a tile at the position provided, in the room provided. It is not occupied by default,
	 * and initializes the adjacentCells set as a HashSet.
	 * @param rowPos
	 * @param colPos
	 * @param room
	 */
	public TestBoardCell(int rowPos, int colPos, RoomName room) {
		super();
		this.rowPos = rowPos;
		this.colPos = colPos;
		isOccupied = false;
		this.room = room;
		adjacentCells = new HashSet<TestBoardCell>();
	}
	
	/**
	 * Adds a cell to the list of cells adjacent to the callee cell.
	 * @param cell
	 */
	public void addAdjacency(TestBoardCell cell) {
		adjacentCells.add(cell);
	}
	
	/**
	 * Returns the list of all cells adjacent to the callee cell.
	 * @return
	 */
	public Set<TestBoardCell> getAdjList(){
		return adjacentCells;
	}
	
	/**
	 * Returns true if the tile is part of a room, false if not.
	 * @return
	 */
	public boolean getisInRoom() {
		if(room == RoomName.NONE) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * Returns whether or not a room is occupied.
	 * @return
	 */
	public boolean getIsOccupied() {
		return isOccupied;
	}

	/**
	 * Sets the tile to occupied if passed in true, unsets if tile is occupied if passed in false.
	 * @param isOccupied
	 */
	public void setIsOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	
}
