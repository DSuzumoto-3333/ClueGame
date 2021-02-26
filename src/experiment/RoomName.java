package experiment;

/**
 * Enumeration to hold the room name, and a string version of the room name.
 * Should be useful later on when implementing the secret passages.
 */
public enum RoomName {
	//Enum for every room on the board
	NONE ("None"),
	GARDEN ("Garden"),
	KITCHEN ("Kitchen"),
	PARLOR ("Parlor"),
	PANTRY ("Pantry"),
	POOL ("Pool"),
	BALLROOM ("Ballroom"),
	STUDY ("Study"),
	CLOSET ("Closet"),
	LOUNGE ("Lounge");
	
	private String roomName;
	
	/**
	 * Basic constructor
	 * @param roomName
	 */
	RoomName (String roomName){
		this.roomName = roomName;
	}
	
	/**
	 * Overridden toString function, returns room name as a string
	 */
	@Override
	public String toString() {
		return roomName;
	}
}
