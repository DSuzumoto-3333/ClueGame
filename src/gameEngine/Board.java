package gameEngine;

import java.util.*;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.HashSet;
import java.io.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Represents the game board itself, and contains methods pertaining to player movement. Also handles loading in data from setup and layout files provided. The board uses a singleton design,
 * so the method .getInstance() must be used rather than creating a new instance through the constructor.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 */
public class Board extends JPanel implements MouseListener{
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
	//Set to hold all player objects involved in the game
	private ArrayList<Player> players;
	//ArrayList to hold all the cards in the game's deck.
	private ArrayList<Card> deck;
	//A set to hold the answer to the game's mystery, or the solution.
	private Set<Card> solution;
	//Variables to determine how the state of the turn engine
	private boolean turnComplete;
	private int currentPlayerNumber = 0, currentRoll;
	private Player currentPlayer;
	//Save the game's JFrame
	private ClueGame frame;
	
	/**
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
	 * Method to handle any thrown BadConfigFormatExceptions from loadSetupConfig() and loadLayoutConfig(). Can be recalled to clear and reset the game board, or load a new board if setConfigFiles() is called first. 
	 * After config file data is loaded, the method also calls for adjacency lists to be populated.
	 */
	public void initialize() {
		//Allocate memory for instance variables
		targets = new HashSet<BoardCell>();
		visited = new Stack<BoardCell>();
		roomMap = new HashMap<Character, Room>();
		players = new ArrayList<Player>();
		deck = new ArrayList<Card>();
		
		//Add the mouse listener to the panel
		addMouseListener(this);
		
		//Try to load the setup and layout config files, and initialize the game instance variables. Catch and handle BadConfigFormatExceptions here.
		try {
			//Load the setup file first.
			loadSetupConfig();
			
			//Then generate the game board based on the rooms specified in the setup file
			loadLayoutConfig();
			
			//Once the files are loaded properly, parse the newly generated board and build adjacency lists.
			setAdjLists();
			
			//Deal the cards and determine the solution to the game.
			dealCards();
		}
		//In the event of a bad input file, print out the error message to the console, and write the error to a log.
		catch (BadConfigFormatException e) {
			System.out.println("Invalid config file detected. Please check the log file in data for more information.");
		}
	}
	
	/**
	 * A void method used to read in the provided room configuration file, and populate roomMap with new room objects. 
	 * @throws BadConfigFormatException - In the event that the setup file is bad, the program will throw an exception with a message explaining the problem.
	 */
	public void loadSetupConfig() throws BadConfigFormatException {
		try {
			//Open the file and prepare a string to read the line into. Add a line counter in case of errors.
			FileReader reader = new FileReader(setupConfigFile);
			Scanner scanner = new Scanner(reader);
			String line;
			int i = 0;
			
			//Read all lines in the file.
			while(scanner.hasNextLine()) {
				line = scanner.nextLine();
				
				//If the line is null, the bottom of the file has been reached.
				if(line.length() == 0) {
					scanner.close();
					break;
				}
				
				//If the line doesn't start with a slash indicating that it's a comment, split it.
				if(!(line.charAt(0) == '/')) {
					String[] data = line.split(",");

					//Pre-declare a few commonly used variables.
					Room room;
					char initial;
					//Since the 2nd entry is always a name, format it here to remove the space at the beginning.
					String name = data[1].replaceFirst("\\s+","");
					switch(data[0]) {
					
					//If the entry declares a room
					case "Room":
						//Verify the entry has the proper length
						if(data.length == 3) {
							//Create a new room and put it in the map with the key being it's initial.
							room = new Room(name);
							initial = data[2].replaceFirst("\\s+","").charAt(0);
							roomMap.put(initial, room);

							//Create a room card, and add it to the deck.
							deck.add(new Card(name, CardType.ROOM));
						}
						//Throw an exception if the entry is not the correct length.
						else {
							scanner.close();
							throw new BadConfigFormatException("Invalid format in " + setupConfigFile + " on line " + i + ": Line contains the wrong number of entries for a Room object.");
						}
						break;
						
					//If the entry declares a non-room space
					case "Space":
						//Verify the entry has the proper length
						if(data.length == 3) {
							//Create a new room and put it in the map with the key being it's initial.
							room = new Room(name);
							initial = data[2].replaceFirst("\\s+","").charAt(0);
							roomMap.put(initial, room);
						}
						//Throw an exception if the entry is not the correct length.
						else {
							scanner.close();
							throw new BadConfigFormatException("Invalid format in " + setupConfigFile + " on line " + i + ": Line contains the wrong number of entries for a Space object.");
						}
						break;
						
					//If the entry declares a weapon
					case "Weapon":
						//Verify the entry has the proper length
						if(data.length == 2) {
							//Add a new weapon card to the deck.
							deck.add(new Card(name, CardType.WEAPON));
						}
						//Throw an exception if the entry is not the correct length.
						else {
							scanner.close();
							throw new BadConfigFormatException("Invalid format in " + setupConfigFile + " on line " + i + ": Line contains the wrong number of entries for a Weapon object.");
						}
						break;
						
					case "Player":
						
						if(data.length == 7) {
							//Create a new person object
							//Read in all the RGB values the file provides, and convert them to ints.
							int redVal = Integer.parseInt(data[2].replaceFirst("\\s+","").replace(",",""));
							int greenVal = Integer.parseInt(data[3].replaceFirst("\\s+","").replace(",",""));
							int blueVal = Integer.parseInt(data[4].replaceFirst("\\s+","").replace(",",""));
							int playerRow = Integer.parseInt(data[5].replaceFirst("\\s+","").replace(",",""));
							int playerCol = Integer.parseInt(data[6].replaceFirst("\\s+","").replace(",",""));
							//Create the new player object.
							players.add(new HumanPlayer(name, new Color(redVal, greenVal, blueVal), playerRow, playerCol));
							
							//Add a new weapon card to the deck.
							deck.add(new Card(name, CardType.PERSON));
						}
						//Throw an exception if the entry is not the correct length.
						else {
							scanner.close();
							throw new BadConfigFormatException("Invalid format in " + setupConfigFile + " on line " + i + ": Line contains the wrong number of entries for a Player object.");
						}
						break;
						
					case "NPC":
						if(data.length == 7) {
							//Create a new person object
							//Read in all the RGB values the file provides, and convert them to ints.
							int redVal = Integer.parseInt(data[2].replaceFirst("\\s+","").replace(",",""));
							int greenVal = Integer.parseInt(data[3].replaceFirst("\\s+","").replace(",",""));
							int blueVal = Integer.parseInt(data[4].replaceFirst("\\s+","").replace(",",""));
							int playerRow = Integer.parseInt(data[5].replaceFirst("\\s+","").replace(",",""));
							int playerCol = Integer.parseInt(data[6].replaceFirst("\\s+","").replace(",",""));
							//Create the new player object.
							players.add(new ComputerPlayer(name, new Color(redVal, greenVal, blueVal), playerRow, playerCol));
							
							//Add a new weapon card to the deck.
							deck.add(new Card(name, CardType.PERSON));
						}
						//Throw an exception if the entry is not the correct length.
						else {
							scanner.close();
							throw new BadConfigFormatException("Invalid format in " + setupConfigFile + " on line " + i + ": Line contains the wrong number of entries for an NPC object.");
						}
						break;
						
					default:
						throw new BadConfigFormatException("Attempted to specify tile of unknown type: " + data[0] + " in " + setupConfigFile + " On line " + i);
					}

				}
			//Increment the counter and close the file.
			i++;
			}
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
			FileReader reader = new FileReader(layoutConfigFile);
			Scanner scanner = new Scanner(reader);
			ArrayList<String> board = new ArrayList<String>();

			//Save each line from the file to board, and close.
			while(scanner.hasNextLine()) {
				board.add(scanner.nextLine());
			}
			scanner.close();

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
						//Put a new cell in gameBoard at i,j
						gameBoard[i][j] = new BoardCell(i,j);

						switch(data[j].charAt(0)) {
						case 'X':
							gameBoard[i][j].setUnused();
							break;
						case 'W':
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
							break;

						default:
							char Initial = data[j].charAt(0);
							Room room = roomMap.get(Initial);
							//Make sure the room is specified in roomMap.
							if(!(room == null)) {
								//If the room tile has only one character, it's just an empty room tile.
								if(data[j].length() == 1) {
									gameBoard[i][j].setRoom(Initial, false, false);
								}

								else {
									//If the room cell has two character specified, determine what type of cell it is. 
									switch(data[j].charAt(1)) {

									//If it's a center cell, set it as such and save it in the room object.
									case '*':
										gameBoard[i][j].setRoom(Initial, false, true);
										room.setCenterCell(gameBoard[i][j]);
										break;

										//If it's a label cell, set it as such and save it in the room object.
									case '#':
										gameBoard[i][j].setRoom(Initial, true, false);
										room.setLabelCell(gameBoard[i][j]);
										break;

										//If it's a secret passage, determine if the secret passage is valid.
									default:
										room = roomMap.get(data[j].charAt(1));
										//If it's a valid secret passage, set the cells secretPassage char.
										if(!(room == null)) {
											gameBoard[i][j].setRoom(Initial, false, false);
											gameBoard[i][j].setSecretPassage(data[j].charAt(1));
										}
										//If it's not, throw a new exception.
										else {
											throw new BadConfigFormatException("Invalid line format on line " + i + " in " + layoutConfigFile + ", Secret passage to invalid room specified.");
										}
										break;
									}
								}
							}
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
					//Pass in a DoorDirection to specify the direction the next tile is from the current tile, useful for linking doors to the proper rooms.
					
					//Check tile above.
					if(i > 0) {
						checkAdjTile(current, i - 1, j, DoorDirection.UP);
					}
					
					//Check tile below
					if(i < boardHeight - 1) {
						checkAdjTile(current, i + 1, j, DoorDirection.DOWN);
					}
					
					//Check tile to the left
					if(j > 0) {
						checkAdjTile(current, i, j - 1, DoorDirection.LEFT);
					}
					
					//Check tile to the right
					if(j < boardWidth - 1) {
						checkAdjTile(current, i, j + 1, DoorDirection.RIGHT);
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
	 * @param direction - The direction that the tile being tested is in with respect to the current tile. 
	 */
	public void checkAdjTile(BoardCell current, int i, int j, DoorDirection direction) {
		BoardCell nextTo = gameBoard[i][j];
		
		//If the tile above exists, and is another walkway tile, add it to the adjacency list.
		if(nextTo.getInitial() == 'W') {
			current.addAdjacency(nextTo);
		}
		
		//If the tile next to the current tile is a room tile, and the current tile is a doorway facing the proper direction, add the center of the room to the adjacency list.
		else if(current.isDoorway() && 
				!(nextTo.getInitial() == 'X') &&
				!(nextTo.getInitial() == 'W') &&
				current.getDoorDirection().equals(direction)){
			BoardCell center = getRoom(nextTo).getCenterCell();
			current.addAdjacency(center);
			center.addAdjacency(current);
		}		
		
	}

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
		
		//If the end of the roll has not been reached, but the cell is a room center, determine if the roll can stop here (If the room center is the start of the roll, we must leave).
		else if(startCell.isRoomCenter() && visited.size() > 1){
			targets.add(startCell);
		
		
		}else{ 
			//Grab every tile in the cell's adjacency list, and iterate through them.
			Set<BoardCell> T = startCell.getAdjList();
			for(BoardCell t : T) {	
				//If the cell isn't in visited, and is either not occupied or a room center, run calcTargetsRecursive() again.
				if(!(visited.contains(t)) && (!t.getOccupied() || t.isRoomCenter())) {
					calcTargetsRecursive(t, length-1);
				}
			}
		}
		//Pop the tile once all the paths branching from below are analyzed so that a different path can use this tile again.
		visited.pop();
	}
	/**
	 * Grabs 3 cards, one of each CardType, to be the answer to the game and stores it in solution. Then it randomly gives each player 3 cards, removing cards that have been
	 * dealt or saved in the solution set as it goes.
	 */
	public void dealCards() throws BadConfigFormatException{
		//Declare a set to count how many types of cards the setup file specified.
		Set<CardType> types = new HashSet<CardType>();
		for(Card card : deck) {
			//If a card in the deck specifies a new type of card, add it to the set.
			if(!(types.contains(card.getType()))) {
				types.add(card.getType());
			}
		}
		
		//Ensure that the setup file specified all 3 types of cards expected: (room, weapon, and person)
		if(types.size() == 3) {
			//Allocate memory for the solution set.
			solution = new HashSet<Card>();
			//Create a copy of deck
			ArrayList<Card> deckCopy = new ArrayList<Card>(deck);
			
		
			//Add one card of each type to the set, removing each card from the deck so it is not used again.
			solution.add(getCardOfType(CardType.ROOM));
			solution.add(getCardOfType(CardType.PERSON));
			solution.add(getCardOfType(CardType.WEAPON));
			
			Random rand = new Random();
			for(Player player : players) {
				for(int i = 0; i < 3; i++) {
					int random = rand.nextInt(deck.size());
					player.updateHand(new Card(deck.get(random)));
					deck.remove(random);
				}
			}
			
			//Restore the deck.
			deck = deckCopy;
		}
		//If not, throw a new BadConfigFormatException.
		else {
			throw new BadConfigFormatException("Error: Setup file " + setupConfigFile + " does not specify all 3 required types of cards.");
		}
		
		
	}
	
	/**
	 * Used to grab a card of a certain type from the deck. Once a random card of the correct type is found, it will remove it from the deck and return a deep copy.
	 * Will continue to loop until a card of the correct type is found.
	 * @param type
	 * @return
	 */
	public Card getCardOfType(CardType type) {
		//Loop until the correct type of card is found.
		Random rand = new Random();
		while(true) {
			//Generate a random number.
			int random = rand.nextInt(deck.size());
			
			//If the card at that index in the deck is the proper type, return it.
			if(deck.get(random).getType() == type) {
				//Use a copy constructor to make a deep copy.
				Card card = new Card(deck.get(random));
				//Remove the card from the deck so it's not used again.
				deck.remove(random);
				//Return the new card.
				return card;
			}
		}
	}
	
	/**
	 * Returns true if the accusation set contains the same elements as the solution set, indicating the player made
	 * a correct accusation.
	 * @param accusation - A set of cards representing the accusation being made.
	 * @return
	 */
	public boolean checkAccusation(Set<Card> accusation) {
		//Iterate through each card in both set
			for(Card solutionCard : solution) {
				for(Card accusationCard : accusation) {
					//If they are the same type of card but are not equal, return false.
					if(solutionCard.getType().equals(accusationCard.getType()) && !accusationCard.equals(solutionCard)) {
						return false;
					}
				}
			}
			//If the loop does not find an incorrect card, return true
			return true;
	}
	/**
	 * A method to handle the made by a player. Checks all players that are not the accusor for the cards in their hand
	 * to determine if the suggestion is valid.
	 * @param suggestion - The suggestion made by a player being handled.
	 * @param suggestor - The person who made the suggestion
	 * @return - The card that debunks the suggestion, null if none found.
	 */
	public Card handleSuggestion(Set<Card> suggestion, Player suggester) {
		//Check every player's hand
		for(Player player : players) {
			//If the player isn't the suggester, and the player has a debunking card
			if(!player.equals(suggester)) {
				Card match = player.disproveSuggestion(suggestion);
				if(match != null) {
					return match;
				}
			}
		}
		//If nothing is found, return null.
		return null;
	}
	
	public void handleTurn() {
		//Ensure that, while the turn is happening, the next turn cannot begin.
		turnComplete = false;
		//Set the current player.
		
		currentPlayer = players.get(currentPlayerNumber);
		
		//Iterate to the next player, looping at 6
		currentPlayerNumber++;
		if(currentPlayerNumber == 6) {
			currentPlayerNumber = 0;
		}
		
		//Get a roll length 
		Random rand = new Random();
		currentRoll = rand.nextInt(6) + 1;
		
		//Calculate the possible targets for the player to move to.
		calcTargets(currentPlayer.getPosition(), currentRoll);
		
		//If the player is a computer player, call currentPlayer.move() and note that the turn is over.
		if(currentPlayer instanceof ComputerPlayer) {
			
			
			//TODO code for accusations
			
			
			//Move the current player
			currentPlayer.move();
			
			
			//TODO Code for suggestions
			
			
			//Repaint the board and let the turn end
			repaint();
			turnComplete = true;
		}
		//If it's a human, don't note that the turn is over yet and draw the targets.
		else {
			
		}
	}
	
	/**
	 * A method used to draw the game board and the objects moving along it as the game plays
	 * @param g - The graphics object to draw on.
	 */
	public void paintComponent(Graphics g) {
		//Call the superclass
		super.paintComponent(g);
		//Calculate the tile sizes
		int tileWidth = getWidth() / 26;
		int tileHeight = getHeight() / 23;
		//Draw the board
		for(int i = 0; i < 23; i++) {
			for(int j = 0; j < 26; j++) {
				int xOffset = tileWidth * j;
				int yOffset = tileHeight * i;
				gameBoard[i][j].draw(tileWidth, tileHeight, xOffset, yOffset, g);
			}
		}
		//Draw the room labels
		for(Map.Entry<Character, Room> entry : roomMap.entrySet()) {
			entry.getValue().draw(tileWidth, tileHeight, g);
		}
		//Draw the players
		for(Player player : players) {
			player.draw(tileWidth, tileHeight, g);
		}
	}
	
	/**
	 * Abstract Methods to handle all types of Mouse Events, since the board implements MouseListener. Most of these will
	 * remain empty, as we don't want to perform any actions during them.
	 */
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		//When the mouse is pressed, make sure it's the player's turn.
		if(currentPlayer instanceof HumanPlayer) {
			//Search for every cell in targets and see if the click landed inside of it.
			BoardCell newTarget = null;
			for(BoardCell cell : targets) {
				if(cell.containsClick(e.getX(), e.getY())) {
					newTarget = cell;
				}
			}
			System.out.println(newTarget.toString());
			//If no target was clicked, display an error.
			if(newTarget == null) {
				JOptionPane.showMessageDialog(frame, 
						"Error: Please select a valid target.",
						"Invalid Target",
						JOptionPane.ERROR_MESSAGE);
			}
			//If a target was clicked, move the player to it and allow them to end their turn.
			else {
				((HumanPlayer) currentPlayer).setNewTarget(newTarget);
				currentPlayer.move();
				
				//Repaint the board and end
				repaint();
				turnComplete = true;
			}
		}
		//If not, display an error.
		else {
			JOptionPane.showMessageDialog(frame, 
					"Error: You may not select a new position while it is not your turn.",
					"Wait your Turn",
					JOptionPane.ERROR_MESSAGE);
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
	 * @param cell - A board cell
	 * @return - The room the board cell occupies, including "Walkway" and "Unused", as both are specified in roomMap.
	 */
	public Room getRoom(BoardCell cell) {
		return roomMap.get(cell.getInitial());
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
	/**
	 * Returns the list of players in the game.
	 * @return
	 */
	public ArrayList<Player> getPlayers(){
		return players;
	}
	/**
	 * Returns the solution to the game.
	 * @return
	 */
	public Set<Card> getSolution(){
		return solution;
	}
	/**
	 * Set the solution to the game, for testing purposes.
	 * @param soln - The desired solution to the game as a set
	 */
	public void setSolution(Set<Card> soln) {
		solution = new HashSet<Card>(soln);
	}
	
	/**
	 * Method to get the game's card deck from the board.
	 * @return - An arraylist representing the deck of cards in play.
	 */
	public ArrayList<Card> getDeck(){
		return deck;
	}
	
	/**
	 * Method to return if the current turn is complete.
	 * @return - Boolean, true if turn is complete, false if not.
	 */
	public boolean getTurnComplete() {
		return turnComplete;
	}
	
	/**
	 * Method to return the current roll for the current target set.
	 * @return - Int representing the roll of a die.
	 */
	public int getCurrentRoll() {
		return currentRoll;
	}
	
	/**
	 * Method to get the current turn's player.
	 * @return - The player who's turn it is.
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setFrame(ClueGame cluegame) {
		frame = cluegame;
	}
}
