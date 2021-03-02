package experiment;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;
import java.util.Set;
import java.util.HashSet;

/**
 * Test Class to experiment with code pertaining to holding the game board and adding simple movement to it.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 */
public class TestBoard {
	//Constants for grid size
	public static final int BOARD_WIDTH = 4;
	public static final int BOARD_HEIGHT = 4;
	//The game board itself
	private TestBoardCell[][] gameBoard;
	//Sets for calculating viable targets to move to.
	private Set<TestBoardCell> targets, visited;
	private static TestBoard instance = new TestBoard();
	
	/**
	 * Simple constructor, instantiates the game board by filling gameBoard with tiles, and populates each tile's adjacency list.
	 */
	public TestBoard() {
		super();
	}
	
	public void initialize() {
		gameBoard = new TestBoardCell[BOARD_HEIGHT] [BOARD_WIDTH];
		//Sets for calculating viable targets to move to.
		targets = new HashSet<TestBoardCell>();
		visited = new HashSet<TestBoardCell>();
		
		targets.clear();
		visited.clear();
		
		for(int i = 0; i < BOARD_HEIGHT; i++) {
			for(int j = 0; j < BOARD_WIDTH; j++) {
				gameBoard[i][j] = new TestBoardCell(i,j);
			}
		}
		
		//Reiterates through the board to populate adjacency lists.
		for(int i = 0; i < BOARD_HEIGHT; i++) {
			for(int j = 0; j < BOARD_WIDTH; j++) {
				if(i > 0) {
					gameBoard[i][j].addAdjacency(gameBoard[i-1][j]);
				}
				if(i < BOARD_HEIGHT - 1) {
					gameBoard[i][j].addAdjacency(gameBoard[i+1][j]);
				}
				if(j > 0) {
					gameBoard[i][j].addAdjacency(gameBoard[i][j-1]);
				}
				if(j < BOARD_WIDTH-1) {
					gameBoard[i][j].addAdjacency(gameBoard[i][j+1]);
				}
			}
		}
	}
	
	/**
	 * Moves through the board, checking for all possible paths a player could take away from the starting cell, and
	 * adds all possible locations to targets. Adds tiles that have already been visited by the calculation function, 
	 * but can't be landed on by the player, to visited.
	 * @param startCell
	 * @param length
	 */
	public void calcTargets(TestBoardCell startCell, int length) {
		//If we're at length=0, the program can't move anymore. If the cell is valid, add it to targets.
		if(length == 0) {
			if(!startCell.getIsOccupied()) {
				targets.add(startCell);
			}
			return;
		}else {
			//Grab every cell directly adjacent to the start cell, and put the start cell in visited so we don't backtrack.
			Set<TestBoardCell> T = startCell.getAdjList();
			visited.add(startCell);
			for(TestBoardCell t : T) {
				//For every cell in the T, if it's not occupied or a tile we've visited before, run calcTargets from it's perspective, and with length - 1
				if(!t.getIsOccupied() && !visited.contains(t)) {
					calcTargets(t, length-1);
				}
			}
		}
		
	}
	
	/**
	 * Returns the targets set.
	 * @return
	 */
	public Set<TestBoardCell> getTargets(){
		return targets;
	}
	
	/**
	 * Grabs a cell from the gameBoard at row, col, and returns it.
	 * @param row
	 * @param col
	 * @return
	 */
	public TestBoardCell getCell(int row, int col) {
		return gameBoard[row][col];
	}
	
	public static TestBoard getBoardInstance() {
		return instance;
	}

}
