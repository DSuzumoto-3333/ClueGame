package gameEngine;

import java.util.*;
import java.util.Set;

import experiment.TestBoardCell;

import java.util.HashSet;
import java.io.*;

/**
 * Represents the game board itself, and contains methods pertaining to player movement. Also handles loading in data from setup and layout files provided. The board uses a singleton design,
 * so the method .getInstance() must be used rather than creating a new instance through the constructor.
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
	private Set<BoardCell> targets;
	private Stack<BoardCell> visited;
	//Strings for loading in data from the layout and setup configuration files.
	private String layoutConfigFile, setupConfigFile;
	//Map to contain all the tile characters
	private Map<Character, Room> roomMap;
	private static Board boardInstance = new Board();
	//A small enumeration used in calculating adjacencies to denote the direction the tile being tested is with respect to the current tile.
	private enum tileDirection {UP, DOWN, LEFT, RIGHT};
	/*
	 * Since we're using a singleton pattern, our constructor is essentially empty. We will use .initialize() after setting and loading the configuration files.
	 * This allows us to re-use the same instance, as we don't want to have multiple game-boards, but leaves us the freedom to re-call .setConfigFiles() and .initialize() and "reset"
	 * The board.
	 */
	public Board() {
		super();	
	}
	
	/**
	 * Takes in the string for the layout and setup files, and sets the instance variable strings to them.
	 * @param layoutFile - A string containing the name of a .cvs file that holds information about every cell and their attributes. Assumed to be in the "data/" directory.
	 * @param setupFile - A string containing the name of a .txt file that holds the name and character of every room on the board, as well as defines Walkway and Unused spaces. Assumed to be in the "data/" directory.
	 */
	public void setConfigFiles(String layoutFile, String setupFile) {
		layoutConfigFile = "data/" + layoutFile;
		setupConfigFile = "data/" + setupFile;
	}
	
	/**
	 * A void method used to read in the provided room configuration file, and populate roomMap with new room objects. 
	 * @throws BadConfigFormatException - In the event that the setup file is bad, the program will throw an exception with a message explaining the problem.
	 */
	public void loadSetupConfig() throws BadConfigFormatException {
		try {
			//Open the file and prepare a string to read the line into. Add a line counter in case of errors.
			FileReader fr = new FileReader(setupConfigFile);
			Scanner s = new Scanner(fr);
			String line;
			int i = 0;
			
			//Read all lines in the file.
			while(s.hasNextLine()) {
				line = s.nextLine();
				
				//If the line is null, the bottom of the file has been reached.
				if(line.length() == 0) {
					break;
				}
				
				//If the line doesn't start with a slash indicating that it's a comment, split it.
				if(!(line.charAt(0) == '/')) {
					String[] data = line.split(",");
					
					//Ensure there are 3 strings in data, and that the entry is valid.
					if(data.length == 3) {
						
						//If the line specifies a room, create a room with the provided name and initial, saved to roomMap (Must filter out spaces)
						if(data[0].equals("Room") || data[0].equals("Space")) {
							Room r = new Room(data[1].replaceFirst("\\s+",""));
							roomMap.put(data[2].replaceFirst("\\s+","").charAt(0), r);
						}
						//If something other than a room or space is read, throw a new BadConfigFormatException
						else if (!data[0].equals("Space")) {
							throw new BadConfigFormatException("Attempted to specify tile of unknown type: " + data[0] + " in " + setupConfigFile + " On line " + i);
						}
					}
					
					//If the entry has too many or two few data points, throw an error
					else{
						throw new BadConfigFormatException("Invalid line format on line " + i + " in " + setupConfigFile);
					}
				}
				//Increment the counter and close the file.
				i++;
			}
			s.close();
		}
		//If the file is not found, throw a new BadConfigFormatException.
		catch(FileNotFoundException e){
			throw new BadConfigFormatException("File 'data/" + setupConfigFile + " not found. Please check the data directory.");
		}
	}
	
	/**
	 * A void method used to read in the board configuration file, create a new gameBoard array, and populate it with cells based on the config data.
	 * @throws BadConfigFormatException - In the event that the setup file is bad, the program will throw an exception with a message explaining the problem.
	 */
	public void loadLayoutConfig() throws BadConfigFormatException {
		try {
			//Open the layout file and create an ArrayList of strings to hold each line in the file.
			FileReader fr = new FileReader(layoutConfigFile);
			Scanner s = new Scanner(fr);
			ArrayList<String> board = new ArrayList<String>();
			
			//Save each line from the file to board, and close.
			while(s.hasNextLine()) {
				board.add(s.nextLine());
			}
			s.close();
			
			//The height of the board is the number of rows, and the width is the length of each row.
			boardHeight = board.size();
			boardWidth = board.get(0).split(",").length;
			
			//Iterate through board and begin creating tiles. Store them in gameBoard. Initialize a line counter.
			gameBoard = new BoardCell[boardHeight][boardWidth];
			int i = 0;
			for(String line : board) {
				//Split the line into tiles and ensure there are the right amount of tiles specified.
				String[] data = line.split(",");
				
				if(data.length == boardWidth) {
					for(int j = 0; j < data.length; j++) {
						//Check the first char in the cell's data 
						char Initial = data[j].charAt(0);
						
						//Put a new cell in gameBoard at i,j
						gameBoard[i][j] = new BoardCell(i,j);
						
						//If it's X, specify an unused tile at i,j in gameBoard.
						if(Initial == 'X') {
							gameBoard[i][j].setUnused();
						}
						
						//If it's a W, create a walkway tile.
						else if(Initial == 'W') {
							gameBoard[i][j].setWalkway();
							
							//Check to see if it's a doorway.
							if(data[j].length() == 2) {
								char direction = data[j].charAt(1);
								
								//Set the door direction to the indicated direction.
								switch(direction){
								case '^':
									gameBoard[i][j].setDoor(DoorDirection.UP);
									break;
								case '<':
									gameBoard[i][j].setDoor(DoorDirection.LEFT);
									break;
								case '>':
									gameBoard[i][j].setDoor(DoorDirection.RIGHT);
									break;
								case 'v':
									gameBoard[i][j].setDoor(DoorDirection.DOWN);
									break;
								}
							}
						}
						
						//If it's a room specified, check to see if it's valid and set it as a room tile.
						else {
							Room r = roomMap.get(Initial);
							//Make sure the room is specified in roomMap.
							if(!(r == null)) {
								//Determine if the room is the center tile or label tile, or if it is part of a secret passage.
								if(data[j].length() == 1) {
									gameBoard[i][j].setRoom(Initial, false, false);
								}
								//If the room is the center tile, save the tile in the room, and mark the tile as a center tile
								else if (data[j].charAt(1) == '*') {
									gameBoard[i][j].setRoom(Initial, false, true);
									r.setCenterCell(gameBoard[i][j]);
								}
								//If the room is the label tile, save the tile in the room, and mark the tile as a label tile.
								else if (data[j].charAt(1) == '#') {
									gameBoard[i][j].setRoom(Initial, true, false);
									r.setLabelCell(gameBoard[i][j]);
								}
								//If the room specifies something else, determine if it is either invalid or a secret passage.
								else {
									r = roomMap.get(data[j].charAt(1));
									//If it's a valid secret passage, set the cells secretPassage char.
									if(!(r == null)) {
										gameBoard[i][j].setRoom(Initial, false, false);
										gameBoard[i][j].setSecretPassage(data[j].charAt(1));
									}
									//If it's not, throw a new exception.
									else {
										throw new BadConfigFormatException("Invalid line format on line " + i + " in " + layoutConfigFile + ", Secret passage to invalid room specified.");
									}
								}
							}
							//If it's not found in roomMap, throw a new exception.
							else {
								throw new BadConfigFormatException("Invalid line format on line " + i + " in " + layoutConfigFile + ", Invalid room of type " + Initial + " specified.");
							}
						}
					}
				}
				//Throw an error if an invalid entry is detected.
				else {
					throw new BadConfigFormatException("Invalid line format on line " + i + " in " + layoutConfigFile + ", wrong number of tiles specified.");
				}
				i++;
			}
		}
		//If the file is not found, throw a new BadConfigFormatException.
		catch(FileNotFoundException e){
			throw new BadConfigFormatException("File 'data/" + setupConfigFile + " not found. Please check the data directory.");
		}
	}
	/**
	 * Method to handle any thrown BadConfigFormatExceptions from loadSetupConfig() and loadLayoutConfig(). Can be recalled to clear and reset the game board, or load a new board if setConfigFiles() is called first. 
	 * After config file data is loaded, the method also calls for adjacency lists to be populated.
	 */
	public void initialize() {
		//Try to load both files, catching BadConfigFormatExceptions here
		targets = new HashSet<BoardCell>();
		visited = new Stack<BoardCell>();
		roomMap = new HashMap<Character, Room>();
		
		try {
			loadSetupConfig();
			loadLayoutConfig();
			setAdjLists();
		}
		//In the event of a bad input file, print out the error message to the console, and write the error to a log.
		catch (BadConfigFormatException e) {
			System.out.println("Invalid config file detected. Please check the log file in data for more information.");
		}
		
	}
	
	/**
	 * A method to set every tile in the gameBoard's adjacency lists. Non-central room tiles and unused tiles will have empty adjacency lists. Walkway tiles will only hold adjacencies with other walkway tiles,
	 * Unless the walkway is a door, in which case it will also hold it's corresponding room's center tile. Room centers will hold adjacency to all doors connecting to the room, and the room center of any rooms
	 * linked by secret passage. 
	 */
	public void setAdjLists() {
		//Iterate through every tile.
		for(int i = 0; i < boardHeight; i++) {
			for(int j = 0; j < boardWidth; j++) {
				//All walkway and door tiles will have an adjacency list.
				BoardCell current = gameBoard[i][j];
				if(current.getInitial() == 'W') {
					//Check tile above.
					if(i > 0) {
						checkAdjTile(current, i - 1, j, tileDirection.UP);
					}
					
					//Check tile below
					if(i < boardHeight - 1) {
						checkAdjTile(current, i + 1, j, tileDirection.DOWN);
					}
					
					//Check tile to the left
					if(j > 0) {
						checkAdjTile(current, i, j - 1, tileDirection.LEFT);
					}
					
					//Check tile to the right
					if(j < boardWidth - 1) {
						checkAdjTile(current, i, j + 1, tileDirection.RIGHT);
					}
				}
				
				//Otherwise, if the tile has a secret passage, link the secret passage tile of this room to the center of the other room.
				else if (!(current.getSecretPassage() == 'X')){
					BoardCell secret = getRoom(current.getSecretPassage()).getCenterCell();
					BoardCell center = getRoom(current).getCenterCell();
					secret.addAdjacency(center);
				}
			}
		}
	}
	
	/**
	 * A method used to determine if a tile will be a valid adjacency to the current tile.
	 * @param current - The board cell that will have it's adjacency list added to if the tile tested is valid.
	 * @param i - The row position of the cell that is being tested for valid adjacency.
	 * @param j - The column position of the cell that is being tested for valid adjacency.
	 * @param t - The direction that the tile being tested is in with respect to the current tile. 
	 */
	public void checkAdjTile(BoardCell current, int i, int j, tileDirection t) {
		BoardCell nextTo = gameBoard[i][j];
		
		//If the tile above exists, and is another walkway tile, add it to the adjacency list.
		if(nextTo.getInitial() == 'W') {
			current.addAdjacency(nextTo);
		}
		
		//If the tile next to the current tile is a room tile, and the current tile is a doorway facing the proper direction, add the center of the room to the adjacency list.
		else if(current.isDoorway() && !(nextTo.getInitial() == 'X')){
			switch(t) {
			case UP:
				if(current.getDoorDirection() == DoorDirection.UP) {
					BoardCell center = getRoom(nextTo).getCenterCell();
					current.addAdjacency(center);
					//Also add the door to the center's adjacency list.
					center.addAdjacency(current);
				}
				break;
			case DOWN:
				if(current.getDoorDirection() == DoorDirection.DOWN) {
					BoardCell center = getRoom(nextTo).getCenterCell();
					current.addAdjacency(center);
					//Also add the door to the center's adjacency list.
					center.addAdjacency(current);
				}
				break;
			case LEFT:
				if(current.getDoorDirection() == DoorDirection.LEFT) {
					BoardCell center = getRoom(nextTo).getCenterCell();
					current.addAdjacency(center);
					//Also add the door to the center's adjacency list.
					center.addAdjacency(current);
				}
				break;
			case RIGHT:
				if(current.getDoorDirection() == DoorDirection.RIGHT) {
					BoardCell center = getRoom(nextTo).getCenterCell();
					current.addAdjacency(center);
					//Also add the door to the center's adjacency list.
					center.addAdjacency(current);
				}
				break;
			}
		}
	}
	/**
	 * Returns the instance of the board, saved in boardInstance. This is the easiest way for us to access the singleton instance of the game board.
	 * @return - The static, single Board object instance.
	 */
	public static Board getInstance() {
		return boardInstance;
	}
	/**
	 * Wrapper method for the calcTargetsRecursive() method. Clears both targets and visited, then runs the first iteration of
	 * calcTargetsRecursive. Sets the target list to a list of possible board cells based on a player's roll that the player could move to.
	 * @param startCell
	 * @param length
	 */
	public void calcTargets(BoardCell startCell, int length) {
		targets.clear();
		visited.clear();
		calcTargetsRecursive(startCell, length);
	}
	
	/**
	 * Moves through the board, checking for all possible paths a player could take away from the starting cell, and
	 * adds all possible locations to targets. Adds tiles that have already been visited by the calculation function, 
	 * but can't be landed on by the player, to visited.
	 * @param startCell - The starting position to calculate a roll from.
	 * @param length - The length of the roll, an integer from 0-6.
	 */
	public void calcTargetsRecursive(BoardCell startCell, int length) {
		//Push the current cell to the top of the visited stack.
		visited.push(startCell);
		
		//If we have made it to the end of our roll, determine if the cell is a valid target
		if(length == 0) {
			
			//If the cell is not occupied, or if it is a room (which can be occupied by multiple people), the cell is a valid target
			if(!(startCell.getOccupied()) || startCell.isRoomCenter()) {
				//The cell is added to the targets set.
				targets.add(startCell);
			}
		}
		
		//If the end of the roll has not been reached, but the cell is a room center, determine if the roll can stop here.
		else if(startCell.isRoomCenter()){
			
			//If more tiles than just the room center have been visited, the player is not starting on the room center, and can opt to enter the new room.
			if(!(visited.size() == 1)) {
				targets.add(startCell);
			}
			
			//If the only tile in visited is the current room center, the player is starting in this room, and must leave.
			else{ 
				//Grab every tile in the cell's adjacency list, and iterate through them.
				Set<BoardCell> T = startCell.getAdjList();
				for(BoardCell t : T) {
					
					//Ensure that the cell grabbed is not one already visited.
					if(!(visited.contains(t))) {
						
						//If the cell isn't occupied, move to it and continue searching for targets.
						if(!(t.getOccupied())) {
							calcTargets(t, length-1);
						}
						
						//If the cell is occupied, but it is also the center of a room, move to it and determine if it is a viable target.
						else if (t.isRoomCenter()) {
							calcTargets(t, length-1);
						}
					}
				}
			}
		}else {
			//Grab every cell directly adjacent to the start cell, and put the start cell in visited so we don't backtrack.
			Set<BoardCell> T = startCell.getAdjList();
			for(BoardCell t : T) {
				//For every cell in the T, if it's not occupied or a tile we've visited before, run calcTargets from it's perspective, and with length - 1
				if(!(visited.contains(t))) {
					if(!(t.getOccupied())) {
						calcTargets(t, length-1);
					}else if (t.isRoomCenter()) {
						calcTargets(t, length-1);
					}
				}
			}
		}
		//Pop the tile once all the paths branching from below are analyzed so that a different path can use this tile again.
		visited.pop();
	}
	
	/**
	 * Returns a set of board cells representing the possible places a player could move to after using calcTargets() given a certain starting point and roll length.
	 * @return - A set of board cells the player can move to.
	 */
	public Set<BoardCell> getTargets(){
		return targets;
	}
	
	/**
	 * Grabs a cell from the gameBoard at row, col, and returns it.
	 * @param row - The row position of the desired cell
	 * @param col - The column position of the desired cell.
	 * @return - A board cell that occupies the desired location.
	 */
	public BoardCell getCell(int row, int col) {
		return gameBoard[row][col];
	}
	
	/**
	 * Returns the number of rows in the board, or it's height.
	 * @return - An int representing the height of the board.
	 */
	public static int getNumRows() {
		return boardHeight;
	}
	
	/**
	 * Returns the number of columns in the board, or it's width.
	 * @return - An int representing the width of the board.
	 */
	public static int getNumColumns() {
		return boardWidth;
	}
	
	/**
	 * Returns the room with the initial passed in.
	 * @param Key - The char initial of the desired room
	 * @return - The room identified by the character provided, null if not found.
	 */
	public Room getRoom(char Key) {
		return roomMap.get(Key);
	}
	
	/**
	 * returns the room that the cell passed in is a part of, including unused and walkway.
	 * @param c - A board cell
	 * @return - The room the board cell occupies, including "Walkway" and "Unused", as both are specified in roomMap.
	 */
	public Room getRoom(BoardCell c) {
		return roomMap.get(c.getInitial());
	}
	/**
	 * Returns the adjacency list of the tile at the position provided.
	 * @param row - The row position of the desired cell
	 * @param col - The column position of the desired cell
	 * @return - The adjacency list of the tile at (row, col)
	 */
	public Set<BoardCell> getAdjList(int row, int col){
		return gameBoard[row][col].getAdjList();
	}
}
