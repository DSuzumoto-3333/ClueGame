package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import experiment.TestBoard;
import experiment.TestBoardCell;
import junit.framework.Assert;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;

class BoardTestsExp {
	TestBoard board;

	@BeforeEach
	public void initializeBoard() {
		board = new TestBoard();
	}
	
	/*
	 * Tests adjacencies for several different locations, including edges
	 */
	@Test
	public void testPositions() {
		// Upper left board should only have two adjacent cells: (1,0) and (0,1)
		TestBoardCell upperLeft = board.getCell(0, 0);
		Set<TestBoardCell> adjacencyList = upperLeft.getAdjList();
		assertEquals(2, adjacencyList.size());
		assertTrue(adjacencyList.contains(board.getCell(1, 0)));
		assertTrue(adjacencyList.contains(board.getCell(0,1)));
		// A cell not in any corner or on any edge should have exactly four adjacency cells, (x-1,y), (x+1,y), (x,y + 1), (x,y-1)
		// Test 100 cells that are not on an edge or in a corner
		Random rand = new Random();
		for (int x = 0; x < 100; x ++) { 
			//Generate a random cell away from the board boundaries
			int randY = rand.nextInt(board.BOARD_HEIGHT -2) + 1;
			int randX = rand.nextInt(board.BOARD_WIDTH -2) + 1;  
			TestBoardCell randCell = board.getCell(randX, randY); 
			//Test the adjacency list for the randomly selected cell.
			Set<TestBoardCell> adjacent = randCell.getAdjList();
			
			assertEquals(4, adjacent.size());
			assertTrue(adjacent.contains(board.getCell(randX - 1,randY)));
			assertTrue(adjacent.contains(board.getCell(randX + 1,randY)));
			assertTrue(adjacent.contains(board.getCell(randX,randY + 1)));
			assertTrue(adjacent.contains(board.getCell(randX,randY - 1)));
		}
		//Any cell on the edge of the board, but not in a corner, must have 3 adjacencies.
		for(int i = 1; i < 3; i++) {
			//Get the cells on the left and right edges of the board sequentially.
			TestBoardCell leftCell = board.getCell(i, 0);
			TestBoardCell rightCell = board.getCell(i, 3);
			//Each cell must have 3 targets, all 1 tile away from them
			assertEquals(leftCell.getAdjList().size(),3);
			assertTrue(leftCell.getAdjList().contains(board.getCell(i-1, 0)));
			assertTrue(leftCell.getAdjList().contains(board.getCell(i+1, 0)));
			assertTrue(leftCell.getAdjList().contains(board.getCell(i, 1)));
			assertEquals(rightCell.getAdjList().size(),3);
			assertTrue(rightCell.getAdjList().contains(board.getCell(i-1, 3)));
			assertTrue(rightCell.getAdjList().contains(board.getCell(i+1, 3)));
			assertTrue(rightCell.getAdjList().contains(board.getCell(i, 2)));

		}
	}
	
	/*
	 * Test to ensure that if a tile is occupied by another player, it cannot be moved to.
	 */
	@Test
	public void testCantEnterOccupiedCell() {
		//Set the bottom 3 rows of the game board to occupied
		for (int i = 1; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				board.getCell(i,j).setIsOccupied(true);
			}
		}
		//Calculate a 2-tile roll, and return the set of targets.
		TestBoardCell startCell = board.getCell(0, 0);
		board.calcTargets(startCell, 2);
		Set<TestBoardCell> targets = board.getTargets();
		//The set should only have 1 element being position (0,2)
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCell(0,2)));
	}
	
	/*
	 * Test to ensure that occupied tiles can still be walked around, but not crossed over.
	 */
	@Test
	public void testMustGoAroundOccupiedCell() {
		//Set the position (2,1 to occupied)
		board.getCell(2, 1).setIsOccupied(true);
		//Calculate a 4-tile roll and return the valid targets.
		TestBoardCell startCell = board.getCell(1,1);
		board.calcTargets(startCell,4);
		Set<TestBoardCell> targets = board.getTargets();
		//The set must have the position (3,1), and must not have either (2,1) or (4,1)
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(!(targets.contains(board.getCell(2, 1))));
		assertTrue(!(targets.contains(board.getCell(4, 1))));
	}
	
	/*
	 * Test to ensure that testCalcTarget() only returns squares that are exactly n tiles away (The algorithm is not back-tracking, moving diagonally, or otherwise misbehaving)
	 */
	@Test
	public void testCalcTargetLengthCheck() {
		//Test a movement of 1 tile
		TestBoardCell startCell = board.getCell(1, 1);
		board.calcTargets(startCell, 1);
		Set<TestBoardCell> targets = board.getTargets();
		//There should only be 4 targets, (0,1), (1,0), (2,1), (1,2)
		assertEquals(targets.size(), 4);
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
	}
	/*
	 * Ensures that the targets calculated, as well as how many targets are calculated make sense.
	 */
	@Test 
	public void testCalcTarget() {
		board.calcTargets(new TestBoardCell(board.BOARD_WIDTH/2, board.BOARD_HEIGHT/2), 6);
		
		Set<TestBoardCell> targets = board.getTargets();
		assertTrue(targets.size() > 0); // there must always be at least one target
		assertTrue(targets.size() < board.BOARD_WIDTH * board.BOARD_HEIGHT); // simple sanity check, there cannot be as many targets are there are cells
		
		for (TestBoardCell target: targets) {
			assertTrue(!target.getIsOccupied());	// a target may not be a spot that is already occupied by another player.
		}
	}
}

