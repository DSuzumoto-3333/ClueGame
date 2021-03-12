package gameEngine;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Custom exception to catch improperly formatted config files 
 * @author Derek Suzumoto
 *
 */
public class BadConfigFormatException extends Exception {
	
	/**
	 * A constructor that can take in a specific error message to throw.
	 * @param Message - A message that adequately describes the issue found in the config files.
	 */
	public BadConfigFormatException(String Message) {
		super(Message);
		System.out.println(Message + "\n Writing error to data/errorlog.txt...");
		try {
			FileWriter fw = new FileWriter("data/errorlog.txt", true);
			fw.write(Message + "\n");
			fw.close();
		}catch (IOException e) {
			System.out.println("Could not write to file.");
		}finally {
			System.out.println("Done.");
		}
	}
	
	/**
	 * A constructor with a default error message.
	 */
	public BadConfigFormatException() {
		super("One of the provided configuration files is improperly format. Please check the configuration files in /data/.");
	}
}
