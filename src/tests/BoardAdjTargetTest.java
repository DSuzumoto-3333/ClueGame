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
	public static final int WIDTH = 26;
	public static final int HEIGHT = 23;

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
		
		/**
		 * Test the adjacency lists of tiles within rooms that are not the center tile. They should have empty adjacency lists.
		 */
		@Test
		void testAdjacencyRoom() {
			//Pick a random cell in the garden and get it's adjacency list.
			Set<BoardCell> adj = board.getAdjList(4, 0);
			//Assert that it's adjacency list has a size of 0.
			assertEquals(0,adj.size());
			
			//Pick a random cell in the garden and get it's adjacency list.
			adj = board.getAdjList(9, 0);
			//Assert that it's adjacency list has a size of 0.
			assertEquals(0,adj.size());
			
			//Pick a random cell in the garden and get it's adjacency list.
			adj = board.getAdjList(17, 0);
			//Assert that it's adjacency list has a size of 0.
			assertEquals(0,adj.size());
			
			//Pick a random cell in the garden and get it's adjacency list.
			adj = board.getAdjList(0, 12);
			//Assert that it's adjacency list has a size of 0.
			assertEquals(0,adj.size());
			
			//Pick a random cell in the garden and get it's adjacency list.
			adj = board.getAdjList(9, 12);
			//Assert that it's adjacency list has a size of 0.
			assertEquals(0,adj.size());
			
			//Pick a random cell in the garden and get it's adjacency list.
			adj = board.getAdjList(17, 12);
			//Assert that it's adjacency list has a size of 0.
			assertEquals(0,adj.size());
			
			//Pick a random cell in the garden and get it's adjacency list.
			adj = board.getAdjList(24, 0);
			//Assert that it's adjacency list has a size of 0.
			assertEquals(0,adj.size());
			
			//Pick a random cell in the garden and get it's adjacency list.
			adj = board.getAdjList(24, 9);
			//Assert that it's adjacency list has a size of 0.
			assertEquals(0,adj.size());
			
			//Pick a random cell in the garden and get it's adjacency list.
			adj = board.getAdjList(24, 17);
			//Assert that it's adjacency list has a size of 0.
			assertEquals(0,adj.size());
		}
		
		/**
		 * Test to ensure that walkway tiles that are not doors cannot enter into rooms.
		 */
		@Test
		void testAdjacencyOutsideRooms() {
			//Pick a tile just outside of the parlor and below the kitchen.
			Set<BoardCell> t = board.getAdjList(16, 3);
			//It should have an adjacency list with 3 tiles, (15,3), (16,2), (16,4)
			assertEquals(3, t.size());
			assertTrue(t.contains(board.getCell(15, 3)));
			assertTrue(t.contains(board.getCell(16, 2)));
			assertTrue(t.contains(board.getCell(16, 4)));
			
			//Pick a tile just outside of the parlor to it's right.
			t = board.getAdjList(15, 8);
			//It should have an adjacency list with 3 tiles, (14,8), (16,8), (15,9)
			assertEquals(3, t.size());
			assertTrue(t.contains(board.getCell(14, 8)));
			assertTrue(t.contains(board.getCell(16, 8)));
			assertTrue(t.contains(board.getCell(15, 9)));
			
			//Pick a tile just above the pool, below the pantry.
			t = board.getAdjList(8, 14);
			//It should have an adjacency list with 3 tiles, (8,13), (8,15), (7,14)
			assertEquals(3, t.size());
			assertTrue(t.contains(board.getCell(8, 13)));
			assertTrue(t.contains(board.getCell(8, 15)));
			assertTrue(t.contains(board.getCell(7, 14)));
			
			//Pick a tile just outside of the Lounge, to the right of the Ballroom
			t = board.getAdjList(19, 19);
			//It should have an adjacency list with 3 tiles, (19,18), (18,19), (20,19)
			assertEquals(3, t.size());
			assertTrue(t.contains(board.getCell(19, 18)));
			assertTrue(t.contains(board.getCell(18, 19)));
			assertTrue(t.contains(board.getCell(20, 19)));
		}
		
		/**
		 * Test to ensure that the adjacency lists of every tile on the edge of the board have a maximum of 3 tiles adjacent.
		 */
		@Test
		void testAdjacenciesAtEdge() {
			Set<BoardCell> left, right;
			for(int i = 0; i < HEIGHT; i++) {
				left = board.getAdjList(i, 0);
				right = board.getAdjList(i, 25);
				if(left.size() > 3 || right.size() > 3) {
					fail("Invalid adjacency list");
				}
			}
			for(int j = 0; j < WIDTH; j++) {
				left = board.getAdjList(0, j);
				right = board.getAdjList(22, j);
				if(left.size() > 3 || right.size() > 3) {
					fail("Invalid adjacency list");
				}
			}
		}
		
		/*
		 * The next following tests will focus on the targets at specific cells
		 */
	}


