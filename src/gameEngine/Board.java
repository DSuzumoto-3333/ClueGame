package gameEngine;

import java.util.*;
import java.util.Set;
import java.util.HashSet;
import java.io.*;

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
	private Map<Character, Room> roomMap;
	private static Board boardInstance = new Board();
	/**
	 * Since we're using a singleton patter, our constructor is essentially empty. We will use .initialize() after setting and loading the configuration files.
	 * This allows us to re-use the same instance, as we don't want to have multiple game-boards, but for the sake of testing bad config files, we need to redefine it.
	 */
	public Board() {
		super();	
		targets = new HashSet<BoardCell>();
		visited = new HashSet<BoardCell>();
		roomMap = new HashMap<Character, Room>();
	}
	
	/**
	 * Takes in the string for the layout and setup files, and sets the instance variable strings to them.
	 * @param layoutFile
	 * @param setupFile
	 */
	public void setConfigFiles(String layoutFile, String setupFile) {
		layoutConfigFile = "data/" + layoutFile;
		setupConfigFile = "data/" + setupFile;
	}
	
	/**
	 * A void method used to read in the provided room configuration file, and populate roomMap with new room objects.
	 * @throws BadConfigFormatException
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
							throw new BadConfigFormatException("Attempted to specify tile of unknown type: " + data[0] + " in " + setupConfigFile + "On line " + i);
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
		//If the file is not found, print error to console.
		catch(FileNotFoundException e){
			System.out.println("File not found.");
		}
	}
	
	/**
	 * A void method used to read in the board configuration file, and prepare the gameBoard to be populated.
	 * @throws BadConfigFormatException
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
								throw new BadConfigFormatException("Invalid line format on line " + i + " in " + layoutConfigFile + ", Invalid room specified.");
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
		//If the file is not found, print error to console.
		catch(FileNotFoundException e){
			System.out.println("File not found.");
		}
	}
	/**
	 * Once the game config files have been read, the board array can be populated, and the cells can be given their necessary properties.
	 */
	public void initialize() {
		try {
			loadSetupConfig();
			loadLayoutConfig();
		}catch (BadConfigFormatException e) {
			System.out.println(e);
		}
		
	}
	/**
	 * Returns the instance of the board, saved in boardInstance.
	 * @return
	 */
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
	
	/**
	 * Returns the number of rows in the board, or it's height.
	 * @return
	 */
	public static int getNumRows() {
		return boardHeight;
	}
	
	/**
	 * Returns the number of columns in the board, or it's width.
	 * @return
	 */
	public static int getNumColumns() {
		return boardWidth;
	}
	
	/**
	 * Returns the room with the initial passed in.
	 * @param Key
	 * @return
	 */
	public Room getRoom(char Key) {
		return roomMap.get(Key);
	}
	
	/**
	 * returns the room that the cell passed in is a part of, including unused and walkway.
	 * @param c
	 * @return
	 */
	public Room getRoom(BoardCell c) {
		return roomMap.get(c.getInitial());
	}
}
