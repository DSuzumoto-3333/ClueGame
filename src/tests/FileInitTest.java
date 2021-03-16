package tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gameEngine.Board;
import gameEngine.BoardCell;
import gameEngine.DoorDirection;
import gameEngine.Room;


class FileInitTest {
	//Initialize intended board dimensions
	public static final int WIDTH = 26;
	public static final int HEIGHT = 23;
	
	//Instantiate singleton board.
	private static Board board;
	
	//Initialize board
	@BeforeAll
	public static void init() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
	/**
	 * Make sure that each room has a center
	 */
	@Test
	void testRoomCenters() {
		//Get the room and cell for the center of the garden, assert that they are equal.
		BoardCell cell = board.getCell(3, 1);
		Room r = board.getRoom(cell);
		assertEquals(cell, r.getCenterCell());
		
		//Get the room and cell for the center of the kitchen, assert that they are equal.
		cell = board.getCell(12, 1);
		r = board.getRoom(cell);
		assertEquals(cell, r.getCenterCell());
		
		//Get the room and cell for the center of the parlor, assert that they are equal.
		cell = board.getCell(20, 5);
		r = board.getRoom(cell);
		assertEquals(cell, r.getCenterCell());

		//Get the room and cell for the center of the pantry, assert that they are equal.
		cell = board.getCell(3, 10);
		r = board.getRoom(cell);
		assertEquals(cell, r.getCenterCell());

		//Get the room and cell for the center of the pool, assert that they are equal.
		cell = board.getCell(12, 13);
		r = board.getRoom(cell);
		assertEquals(cell, r.getCenterCell());

		//Get the room and cell for the center of the ballroom, assert that they are equal.
		cell = board.getCell(19, 14);
		r = board.getRoom(cell);
		assertEquals(cell, r.getCenterCell());

		//Get the room and cell for the center of the study, assert that they are equal.
		cell = board.getCell(3, 22);
		r = board.getRoom(cell);
		assertEquals(cell, r.getCenterCell());

		//Get the room and cell for the center of the closet, assert that they are equal.
		cell = board.getCell(12, 24);
		r = board.getRoom(cell);
		assertEquals(cell, r.getCenterCell());

		//Get the room and cell for the center of the lounge, assert that they are equal.
		cell = board.getCell(19, 22);
		r = board.getRoom(cell);
		assertEquals(cell, r.getCenterCell());
	}
	
	/**
	 * Make sure each room has a label
	 */
	@Test
	void testBoardLabels() {
		//Get the room and cell for the label of the garden, assert that they are equal.
		BoardCell cell = board.getCell(1, 1);
		Room r = board.getRoom(cell);
		assertEquals(cell, r.getLabelCell());
		
		//Get the room and cell for the label of the kitchen, assert that they are equal.
		cell = board.getCell(13, 0);
		r = board.getRoom(cell);
		assertEquals(cell, r.getLabelCell());
		
		//Get the room and cell for the label of the parlor, assert that they are equal.
		cell = board.getCell(20, 2);
		r = board.getRoom(cell);
		assertEquals(cell, r.getLabelCell());

		//Get the room and cell for the label of the pantry, assert that they are equal.
		cell = board.getCell(1,8);
		r = board.getRoom(cell);
		assertEquals(cell, r.getLabelCell());

		//Get the room and cell for the label of the pool, assert that they are equal.
		cell = board.getCell(10, 11);
		r = board.getRoom(cell);
		assertEquals(cell, r.getLabelCell());

		//Get the room and cell for the label of the ballroom, assert that they are equal.
		cell = board.getCell(21, 11);
		r = board.getRoom(cell);
		assertEquals(cell, r.getLabelCell());

		//Get the room and cell for the label of the study, assert that they are equal.
		cell = board.getCell(1, 19);
		r = board.getRoom(cell);
		assertEquals(cell, r.getLabelCell());

		//Get the room and cell for the label of the closet, assert that they are equal.
		cell = board.getCell(10, 23);
		r = board.getRoom(cell);
		assertEquals(cell, r.getLabelCell());

		//Get the room and cell for the label of the lounge, assert that they are equal.
		cell = board.getCell(18, 24);
		r = board.getRoom(cell);
		assertEquals(cell, r.getLabelCell());
	}
	
	/**
	 * Test that not only does every room/space type exist, but that for each type of cell,
	 * The proper quantity of tiles was also generated as well. Helps assure board generation
	 * Went perfectly.
	 */
	@Test
	void testNumTilesPerCard() {
		//Create a map to hold counts.
		Map<Character, Integer> counts = new HashMap<Character, Integer>();
		
		//Iterate through every cell in the map
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				char c = board.getCell(i,j).getInitial();
				
				//If there isn't an existing entry in the map for that type of tile, add it and initialize to 1.
				if(counts.get(c) == null) {
					counts.put(c, 1);
				}
				//If there already is a map entry for that initial, increment the counter.
				else {
					int temp = counts.get(c);
					counts.replace(c, temp + 1);
				}
			}
		}
		//Assert that there must be 37 unused tiles.
		int xcount = counts.get('X');
		assertEquals(xcount, 37);
		
		//Assert that there must be 202 walkway tiles.
		int wcount = counts.get('W');
		assertEquals(wcount, 202);
		
		//Assert that there must be 28 garden tiles.
		int gcount = counts.get('G');
		assertEquals(gcount, 28);
 
		//Assert that there must be 24 kitchen tiles.
		int kcount = counts.get('K');
		assertEquals(kcount, 24);

		//Assert that there must be 51 parlor tiles.
		int pcount = counts.get('P');
		assertEquals(pcount, 51);

		//Assert that there must be 63 pantry tiles.
		int acount = counts.get('A');
		assertEquals(acount, 63);

		//Assert that there must be 36 pool tiles.
		int ocount = counts.get('O');
		assertEquals(ocount, 36);

		//Assert that there must be 40 ballroom tiles.
		int bcount = counts.get('B');
		assertEquals(bcount, 40);

		//Assert that there must be 63 study tiles.
		int scount = counts.get('S');
		assertEquals(scount, 63);

		//Assert that there must be 18 closet tiles.
		int ccount = counts.get('C');
		assertEquals(ccount, 18);

		//Assert that there must be 36 lounge tiles.
		int lcount = counts.get('L');
		assertEquals(lcount, 36);
	}
	
	/**
	 * Make sure that the secret cells are linked
	 */
	@Test
	void testSecretPassageLinked() {
		BoardCell garden = board.getCell(6, 3);
		BoardCell parlor = board.getCell(22, 7);
		BoardCell study = board.getCell(0, 25);
		BoardCell lounge = board.getCell(22, 25);
		
		//Assert that the garden and the lounge are linked.
		assertEquals(garden.getSecretPassage(), lounge.getInitial());
		assertEquals(garden.getInitial(), lounge.getSecretPassage());
		
		//Assert that the parlor and the study are linked.
		assertEquals(parlor.getSecretPassage(), study.getInitial());
		assertEquals(parlor.getInitial(), study.getSecretPassage());
	}
	
	/**
	 * Ensure that the proper number of doors of each type exist. Not only ensures that 
	 * All door enumerations are used, but that all doors are loaded, and loaded in the
	 * Proper direction.
	 */
	@Test
	void testDoorCountAndDirection() {
		//Create a map to hold counts.
		Map<DoorDirection, Integer> counts = new HashMap<DoorDirection, Integer>();

		//Iterate through every cell in the map
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				DoorDirection d = board.getCell(i,j).getDoorDirection();
				//Skip DoorDirection.NONE since it's not important to us.
				if(d != DoorDirection.NONE) {
					//If there isn't an existing entry in the map for that type of tile, add it and initialize to 1.
					if(counts.get(d) == null) {
						counts.put(d, 1);
					}
					//If there already is a map entry for that initial, increment the counter.
					else {
						int temp = counts.get(d);
						counts.replace(d, temp + 1);
					}
				}
			}
		}
		
		//Assert that there should be 7 up doors, 6 right doors, 6 left doors, and 4 down doors
		int ucount = counts.get(DoorDirection.UP);
		int dcount = counts.get(DoorDirection.DOWN);
		int lcount = counts.get(DoorDirection.LEFT);
		int rcount = counts.get(DoorDirection.RIGHT);
		assertEquals(ucount,7);
		assertEquals(rcount,6);
		assertEquals(lcount,6);
		assertEquals(dcount,4);
		
		//Set the total expected number of doors to the sum of the counters, initialize new counter.
		int dexpected = ucount + dcount + lcount + rcount;
		int count = 0;
		
		//Iterate through the whole board, and count every tile that returns true on .isDoorway()
		for(int i = 0; i < HEIGHT; i++) {
			for(int j = 0; j < WIDTH; j++) {
				if(board.getCell(i, j).isDoorway()) {
					count++;
				}
			}
		}
		
		//Assert that there are the expected number of doors, an that isDoorway() works.
		assertEquals(dexpected, count);
	}
	
	/**
	 * Make sure that the size of the loaded board matches that of the size of the input file.
	 */
	@Test
	void testLoadedBoardSize() {
		assertEquals(board.getNumRows(),HEIGHT);
		assertEquals(board.getNumColumns(), WIDTH);
	}
}
