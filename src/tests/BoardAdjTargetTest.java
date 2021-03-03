package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gameEngine.Board;
import gameEngine.BoardCell;

class BoardAdjTargetTest {

	private static Board board;

	@BeforeAll
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	
	/*
	 * These first few tests will test various adjacency logic and ensure that it works properly
	 */
	

	// Test adjacencies of various pieces in the walkways; these pieces are colored Majenta in the sheet
	
		@Test
		public void testAdjacenciesWalkway()
		{
			// we want to test a couple cells and make sure their adjencies make sense
			Set<BoardCell> testList = board.getAdjList(3 , 9);
			assertEquals(4, testList.size());
			assertTrue(testList.contains(board.getCell(2, 9)));
			assertTrue(testList.contains(board.getCell(4 , 9)));
			assertTrue(testList.contains(board.getCell(3 , 10)));
			assertTrue(testList.contains(board.getCell(3 , 8)));
			
			testList = board.getAdjList(11 , 8);
			assertEquals(43, testList.size());
			assertTrue(testList.contains(board.getCell(11 , 8)));
			assertTrue(testList.contains(board.getCell(11 , 8)));
			assertTrue(testList.contains(board.getCell(11 , 8)));

		}
	}


