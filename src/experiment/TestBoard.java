package experiment;

import java.util.*;
import java.util.Set;
import java.util.HashSet;

public class TestBoard {
	public static final int BOARD_WIDTH = 4;
	public static final int BOARD_HEIGHT = 4;
	private TestBoardCell[][] BOARD_LAYOUT;
	
	public TestBoard() {
		super();
		BOARD_LAYOUT = new TestBoardCell[BOARD_WIDTH][BOARD_HEIGHT];
		for(int i = 0; i < BOARD_HEIGHT; i++) {
			for(int j = 0; j < BOARD_WIDTH; j++) {
				BOARD_LAYOUT[i][j] = new TestBoardCell(i,j);
			}
		}
		
		for(int i = 0; i < BOARD_HEIGHT; i++) {
			for(int j = 0; j < BOARD_WIDTH; j++) {
				if(i > 0) {
					BOARD_LAYOUT[i][j].addAdjacency(BOARD_LAYOUT[i-1][j]);
				}
				if(i < BOARD_HEIGHT - 1) {
					BOARD_LAYOUT[i][j].addAdjacency(BOARD_LAYOUT[i+1][j]);
				}
				if(j > 0) {
					BOARD_LAYOUT[i][j].addAdjacency(BOARD_LAYOUT[i][j-1]);
				}
				if(j < BOARD_WIDTH-1) {
					BOARD_LAYOUT[i][j].addAdjacency(BOARD_LAYOUT[i][j+1]);
				}
			}
		}
	}
	
	public void calcTargets(TestBoardCell startCell, int length) {
		return;
	}
	
	public Set<TestBoardCell> getTargets(){
		HashSet<TestBoardCell> t = new HashSet<TestBoardCell>();
		return t;
	}
	
	public TestBoardCell getCell(int row, int col) {
		return BOARD_LAYOUT[row][col];
	}
}
