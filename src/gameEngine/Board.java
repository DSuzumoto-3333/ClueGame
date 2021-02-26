package gameEngine;

import java.util.*;
import java.util.Set;
import java.util.HashSet;

/**
 * Represents the game board itself, and contains methods pertaining to player movement. Also handles loading in data from setup and layout files provided.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 */
public class Board {
	//Constants for grid size
	private static int boardWidth;
	private static int boardHeight;
	//The game board itself
	private BoardCell[][] gameBoard;
	//Sets for calculating viable targets to move to.
	private Set<BoardCell> targets, visited;
	//Strings for loading in data from the layout and setup configuration files.
	private String layoutConfigFile, setupConfigFile;
	//Map to contain all the tile characters
	private Map<Character, Room> roomCharMap;
	private static Board boardInstance = new Board();
	/**
	 * Since we're using a singleton patter, our constructor is essentially empty. We will use .initialize() after setting and loading the configuration files.
	 * This allows us to re-use the same instance, as we don't want to have multiple game-boards, but for the sake of testing bad config files, we need to redefine it.
	 */
	public Board() {
		super();	
	}
	
	/**
	 * Takes in the string for the layout and setup files, and sets the instance variable strings to them.
	 * @param layoutFile
	 * @param setupFile
	 */
	public void setConfigFiles(String layoutFile, String setupFile) {
		layoutConfigFile = layoutFile;
		setupConfigFile = setupFile;
	}
	
	public void loadSetupConfig() {
		
	}
	
	public void loadLayoutConfig() {
		
	}
	/**
	 * Once the game config files have been read, the board array can be populated, and the cells can be given their necessary properties.
	 */
	public void initialize() {
		//Creates a new gameBoard and populates it with new cells.
				gameBoard = new BoardCell[boardWidth][boardHeight];
				for(int i = 0; i < boardHeight; i++) {
					for(int j = 0; j < boardWidth; j++) {
						gameBoard[i][j] = new BoardCell(i,j);
					}
				}
				
				//Reiterates through the board to populate adjacency lists.
				for(int i = 0; i < boardHeight; i++) {
					for(int j = 0; j < boardWidth; j++) {
						if(i > 0) {
							gameBoard[i][j].addAdjacency(gameBoard[i-1][j]);
						}
						if(i < boardHeight - 1) {
							gameBoard[i][j].addAdjacency(gameBoard[i+1][j]);
						}
						if(j > 0) {
							gameBoard[i][j].addAdjacency(gameBoard[i][j-1]);
						}
						if(j < boardWidth-1) {
							gameBoard[i][j].addAdjacency(gameBoard[i][j+1]);
						}
					}
				}
	}
	
	public static Board getInstance() {
		return boardInstance;
	}
	
	/**
	 * Moves through the board, checking for all possible paths a player could take away from the starting cell, and
	 * adds all possible locations to targets. Adds tiles that have already been visited by the calculation function, 
	 * but can't be landed on by the player, to visited.
	 * @param startCell
	 * @param length
	 */
	public void calcTargets(BoardCell startCell, int length) {
		return;
	}
	
	/**
	 * Returns the targets set.
	 * @return
	 */
	public Set<BoardCell> getTargets(){
		HashSet<BoardCell> t = new HashSet<BoardCell>();
		return t;
	}
	
	/**
	 * Grabs a cell from the gameBoard at row, col, and returns it.
	 * @param row
	 * @param col
	 * @return
	 */
	public BoardCell getCell(int row, int col) {
		return gameBoard[row][col];
	}
	
	public static int getNumRows() {
		return boardWidth;
	}
	
	public static int getNumColumns() {
		return boardWidth;
	}
}
