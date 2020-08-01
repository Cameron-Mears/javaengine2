package game;

import java.util.Map.Entry;
import java.util.Properties;

import engine.core.Engine;
import engine.core.console.ConsoleHandler;
import game.renderer.Renderer;
import graphics.Camera;
import graphics.layer.GraphicsLayerManager;
import physics.collision.Rectangle;

public class Game
{
	
	public static void main(String[] args)
	{ 
		System.setProperty("sun.java2d.opengl", "true");
		Engine.getInstance();
		ConsoleHandler handler = new ConsoleHandler();
		Renderer r = new Renderer();
		GraphicsLayerManager.getInstance().setRenderer(r);
		Camera camera = new Camera(new Rectangle(1600, 1000),1, "main");
		Camera c2 = new Camera(new Rectangle(1000, 1000),0.1, "shit");
		r.addCamera(0, camera);
		r.addCamera(1, c2);
		Engine.getInstance().start();
	}
	
}

