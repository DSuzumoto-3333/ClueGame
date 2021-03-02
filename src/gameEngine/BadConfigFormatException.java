package gameEngine;
/**
 * Custom exception to catch improperly formatted config files 
 * @author Derek Suzumoto
 *
 */
public class BadConfigFormatException extends Exception {
	
	/**
	 * A constructor that can take in a specific error message to throw.
	 * @param Message
	 */
	public BadConfigFormatException(String Message) {
		super(Message);
	}
	
	/**
	 * A constructor with a default error message.
	 */
	public BadConfigFormatException() {
		super("One of the provided configuration files is improperly format. Please check the configuration files in /data/.");
	}
}
