package engine.core;

import java.awt.Graphics2D;
import java.io.IOException;

import engine.core.exceptions.EngineException;
import engine.core.tick.TickHandler;
import external.org.json.JSONException;
import graphics.instance.InvalidInstanceException;
import graphics.layer.GraphicsLayerManager;
import graphics.viewer.Window;
import physics.collision.CollisionLayerManager;

class GameLoop extends Thread
{
	private int tickRate;
	private int frameRate;
	private boolean running = true;
	private long lastTime = 0;
	
	private boolean doSleep;
	
	private TickHandler tickHandler;
	private GraphicsLayerManager gManager;
	private CollisionLayerManager collisionManager;
	
	private long deltaF; //time delta for a frame render
	private long deltaU; //time delta for a tick
	
	public GameLoop(Engine engine, int tickRate, int frameRate) throws EngineException, JSONException, InvalidInstanceException, IOException
	{
		deltaF = ((long)(1e9))/frameRate;
		deltaU = ((long)(1e9))/tickRate;
		tickHandler = TickHandler.getInstance();
		collisionManager = CollisionLayerManager.getInstance();
	}
	
	
	private void loopAccurate()
	{
		
	}
	
	private void loopSleep()
	{
		
	}
	
	@Override
	public void start()
	{
		
		long deltaFsum = 0;
		
		long deltaUsum = 0;
		long deltaSum = 0;
		
		int frames = 0, ticks = 0;
		
		lastTime = System.nanoTime();
		while (running)
		{
			long now = System.nanoTime();
			long delta = now - this.lastTime;
			
			deltaFsum += delta;
			deltaUsum += delta;
			deltaSum += delta;
			
			if (deltaFsum >= deltaF)
			{
				try {
					this.render();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EngineException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				deltaFsum = 0;
				frames++;
			}
			
			if (deltaUsum >= deltaU)
			{
				this.tick(deltaUsum);
				deltaUsum = 0;
				ticks++;
			}
			
			
			if (deltaSum >= (long)(1e9))
			{
				try
				{			
				
					if ((boolean) Engine.getInstance().getProperty("printRates"))
					{
						System.out.println("FPS: " + Long.toString(frames));
						System.out.println("TICKS: " + Long.toString(ticks));
					}
				}
				catch (Exception e) {}
				ticks = 0;
				frames = 0;
				deltaSum = 0;
			}
			this.lastTime = now;
			
			
			
		}
	}
	
	
	
	public void recompute()
	{
		
	}
	
	private void tick(long delta)
	{
		tickHandler.tick(delta);
		collisionManager.resloveAllCollisions();
	}
	
	private void render() throws JSONException, IOException, EngineException
	{
		Graphics2D g2 = Window.getInstance().createGraphics();
		GraphicsLayerManager.getInstance().render(g2);
		Window.getInstance().showGraphics();
	}
	

}
