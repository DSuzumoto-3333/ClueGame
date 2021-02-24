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
	}
	
	public void calcTargets(TestBoardCell startCell, int length) {
		return;
	}
	
	public Set<TestBoardCell> getTargets(){
		HashSet<TestBoardCell> t = new HashSet<TestBoardCell>();
		return t;
	}
	
	public TestBoardCell getCell(int row, int col) {
		return null;
	}
}
