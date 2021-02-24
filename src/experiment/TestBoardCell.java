package experiment;

import java.util.Set;
import java.util.HashSet;

public class TestBoardCell {
	private int rowPos; 
	private int colPos;
	private boolean isOccupied;
	private boolean isInRoom;
	private Set<TestBoardCell> adjacentCells;
	
	public TestBoardCell(int rowPos, int colPos) {
		super();
		this.rowPos = rowPos;
		this.colPos = colPos;
	}
	
	public void addAdjacency(TestBoardCell cell) {
		adjacentCells.add(cell);
	}
	
	public Set<TestBoardCell> getAdjList(){
		return adjacentCells;
	}
	
	public void setInRoom(boolean isInRoom) {
		this.isInRoom = isInRoom;
	}
	
	public boolean getInRoom() {
		return isInRoom;
	}

	public boolean getIsOccupied() {
		return isOccupied;
	}

	public void setIsOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	
	
	
}
