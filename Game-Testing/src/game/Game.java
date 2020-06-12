package game;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import engine.core.Engine;
import engine.core.exceptions.EngineException;
import external.org.json.JSONException;
import game.entities.Player;
import graphics.instance.InvalidInstanceException;
import graphics.viewer.Window;

public class Game 
{
	
	public static void main(String[] args) throws JSONException, IOException, EngineException, InvalidInstanceException
	{
		Window.init();
		
		Engine.getInstance().start();
	}
	
}
