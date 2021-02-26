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
	
	/**
	 * Simple constructor, instantiates the game board by filling gameBoard with tiles, and populates each tile's adjacency list.
	 */
	public TestBoard() {
		super();
		targets = new HashSet<TestBoardCell>();
		visited = new HashSet<TestBoardCell>();
		//Creates a new gameBoard and populates it with new cells.
		gameBoard = new TestBoardCell[BOARD_WIDTH][BOARD_HEIGHT];
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
		for( TestBoardCell adjCell: startCell.getAdjList()) {
			if (visited.contains(adjCell) || adjCell.getIsOccupied()) {
				return;
			}
			
			visited.add(adjCell);
			if (length == 1) {
				targets.add(adjCell);
			}
			else {
				calcTargets(adjCell,length-1);
			}
			
			visited.remove(adjCell);
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
}
