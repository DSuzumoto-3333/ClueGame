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
			Set<BoardCell> testList = board.getAdjList(4 , 8);
			assertEquals(4, testList.size());
			assertTrue(testList.contains(board.getCell(2 , 8)));
			assertTrue(testList.contains(board.getCell(3 , 8)));
			assertTrue(testList.contains(board.getCell(4 , 9)));
			assertTrue(testList.contains(board.getCell(4 , 10)));
			
			testList = board.getAdjList(16 , 8);
			assertEquals(4, testList.size());
			assertTrue(testList.contains(board.getCell(15 , 8)));
			assertTrue(testList.contains(board.getCell(16 , 8)));
			assertTrue(testList.contains(board.getCell(16 , 9)));
			assertTrue(testList.contains(board.getCell(16 , 10)));

		}
		
		
		/*
		 * Cells that are in the center of rooms should be adjacent both to secret passages and to doorways.
		 * These cells are marked in Cyan in the spreadsheet
		 */
		@Test
		public void testAdjacencytoSecretPassage() {
			Set<BoardCell> testList = board.getAdjList(5, 20);
			
			assertEquals(4, testList.size());
			assertTrue(testList.contains(board.getCell(7 , 22))); // The Secret Passage
			assertTrue(testList.contains(board.getCell(16 , 15))); // The Doorways
			assertTrue(testList.contains(board.getCell(19 , 20)));
			assertTrue(testList.contains(board.getCell(19 , 19)));
			
		}
		
		/*
		 * Cells that are doorways should be adjacent to the center of the room as well as any cells directly outside the doorway
		 * These cells are marked in Light Blue in the spreadsheet
		 */
		@Test
		public void testAdjacencyDoorway() {
			Set<BoardCell> testList = board.getAdjList(21, 12);
			
			assertEquals(4, testList.size());
			assertTrue(testList.contains(board.getCell(24 , 12))); // The Center of the room
			assertTrue(testList.contains(board.getCell(21 , 11)));
			assertTrue(testList.contains(board.getCell(21 , 13)));
			assertTrue(testList.contains(board.getCell(20 , 12)));
			
			testList = board.getAdjList(4, 12);
			
			assertEquals(4, testList.size());
			assertTrue(testList.contains(board.getCell(1 , 12))); // The Center of the room
			assertTrue(testList.contains(board.getCell(4 , 11)));
			assertTrue(testList.contains(board.getCell(4 , 13)));
			assertTrue(testList.contains(board.getCell(5 , 12)));
		}
		
		/*
		 * Cells in the room, but not in the center should not have any adjacencies
		 * These cells are marked in Orange in the spreadsheet
		 */
		@Test
		public void testAdjacencytoOffcenterRoom() {
			Set<BoardCell> testList = board.getAdjList(23, 20);
			
			assertEquals(0, testList.size());
		}
		
		/*
		 * These cells are next to a room and not a doorway, and shouldn't have any access to any cells in the room
		 * These cells are marked in Dark Green in the spreadsheet
		 */
		@Test
		public void testCellNextToRoom() {
			Set<BoardCell> testList = board.getAdjList(5, 1);
			
			assertEquals(3, testList.size());
			assertTrue(testList.contains(board.getCell(5 , 0))); 
			assertTrue(testList.contains(board.getCell(5 , 2)));
			assertTrue(testList.contains(board.getCell(4 , 1)));
			assertTrue(!testList.contains(board.getCell(6, 1))); // This spot is in the room, so it should be invalid
			
			testList = board.getAdjList(9, 11);
			
			assertEquals(3, testList.size());
			assertTrue(testList.contains(board.getCell(9 , 10)));
			assertTrue(testList.contains(board.getCell(9 , 12)));
			assertTrue(testList.contains(board.getCell(8 , 11)));
			assertTrue(!testList.contains(board.getCell(10 , 11))); // This spot is in the room, so it should be invalid
			
			testList = board.getAdjList(17, 7);
			
			assertEquals(3, testList.size());
			assertTrue(testList.contains(board.getCell(16 , 7))); 
			assertTrue(testList.contains(board.getCell(18 , 7)));
			assertTrue(testList.contains(board.getCell(17 , 8)));
			assertTrue(!testList.contains(board.getCell(17 , 6))); // This spot is in the room, so it should be invalid
		}
		
		/*
		 * These cells are on the edge of the board and thus should have a limited number of adjacencies
		 * These cells are marked in Berry in the spreadsheet
		 */
		@Test
		public void testCellOnEdgeofBoard() {
			Set<BoardCell> testList = board.getAdjList(15, 0);
			
			assertEquals(2, testList.size());
			assertTrue(testList.contains(board.getCell(15 , 1))); 
			assertTrue(testList.contains(board.getCell(16 , 0)));
			assertTrue(!testList.contains(board.getCell(14 , 0))); // This spot is in a room, so it should be invalid
			assertTrue(!testList.contains(board.getCell(15, -1))); // This spot is off the board, so it should be invalid
			
			testList = board.getAdjList(18, 22);
			
			assertEquals(3, testList.size());
			assertTrue(testList.contains(board.getCell(17 , 22)));
			assertTrue(testList.contains(board.getCell(19 , 22)));
			assertTrue(testList.contains(board.getCell(18 , 21)));
			assertTrue(!testList.contains(board.getCell(18 , 23))); // This spot is in the room, so it should be invalid
			
		}
		
		/*
		 * The next following tests will focus on the targets at specific cells
		 */
	}


