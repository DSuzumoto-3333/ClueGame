package gameEngine;
/**
 * Custom exception to catch improperly formatted 
 * @author Derek Suzumoto
 *
 */
public class BadConfigFormatException extends Exception {
	public BadConfigFormatException(int line, String file) {
		super("Error: File improperly formatted at " + line + " in file " + file);
	}
}
