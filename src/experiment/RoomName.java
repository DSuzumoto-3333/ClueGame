package experiment;

public enum RoomName {
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
	
	RoomName (String roomName){
		this.roomName = roomName;
	}
	
	@Override
	public String toString() {
		return roomName;
	}
}
