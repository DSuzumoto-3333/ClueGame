package gameEngine;

/**
 * A simple enumeration class to determine the direction a door faces. All non-door tiles store the NONE value by default, while doors indicate the direction to enter the room.
 * @author Derek Suzumoto
 * @author Luke Wakumoto
 *
 */
public enum DoorDirection {
	UP, DOWN, LEFT, RIGHT, NONE;
}
