package game;

import engine.core.Engine;
import engine.core.console.ConsoleHandler;

public class Game
{
	
	public static void main(String[] args)
	{ 
		Engine.getInstance();
		ConsoleHandler handler = new ConsoleHandler();
		Engine.getInstance().start();
	}
	
}

