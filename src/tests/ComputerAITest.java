package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import gameEngine.*;
import junit.framework.Assert;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;

class ComputerAITests {


	private static Board board;

	@BeforeEach
	public void initializeBoard() {
		board = Board.getInstance();
		board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
		board.initialize();
	}
	
}

