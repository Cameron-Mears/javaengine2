package game;


import engine.core.Engine;
import engine.core.console.ConsoleHandler;
import game.renderer.Renderer;
import graphics.Camera;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;
import physics.collision.Rectangle;

public class Game
{
	
	public static void main(String[] args)
	{ 
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		System.setProperty("sun.java2d.opengl", "true");
		Engine.getInstance();
		ConsoleHandler handler = new ConsoleHandler();
		Renderer r = new Renderer();
		GraphicsLayerManager glm =  GraphicsLayerManager.getInstance();
		glm.setRenderer(r);
		
		GraphicsLayer turrets = new GraphicsLayer("turrets", Level.turretTree);
		
		GraphicsLayer ui = new GraphicsLayer("ui");
		glm.addLayer(ui, Long.MAX_VALUE);
		glm.addLayer(turrets, 11);
		Camera camera = new Camera(new Rectangle(860, 540),1, "main");
		Camera c2 = new Camera(new Rectangle(-100,100,860, 540),0.1, "shit");
		r.addCamera(0, camera);
		//r.addCamera(1, c2);
		Engine.getInstance().start();
	}
	
}

