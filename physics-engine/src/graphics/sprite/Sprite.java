package graphics.sprite;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.core.instance.EngineInstance;
import engine.core.tick.TickInfo;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class Sprite 
{
	private double frameDelta;
	private double currentDelta;
	
	private int currentFrame;
	private String name;
	private double FPS;
	private ArrayList<BufferedImage> frames;
	private boolean animated;
	
	@Override
	public Sprite clone()
	{
		return new Sprite(FPS, frames, name);
	}
	
	
	public Sprite(double fps, ArrayList<BufferedImage> frames, String assetName)
	{
		this.frames = frames;
		this.name = assetName;
		this.setSpriteFPS(fps);
		this.currentFrame = 0;
		this.currentDelta = 0;
		this.animated = true;
	}
	
	public boolean isAnimated()
	{
		return true;
	}
	
	public void setAnimated(boolean animated)
	{
		this.animated = animated;
	}
	
	public double getCurrentFPS()
	{
		return FPS;
	}
	
	public HitBox getHitBox(Vector2 position, EngineInstance owner)
	{
		Rectangle rect = new Rectangle(frames.get(0).getWidth(), frames.get(0).getHeight(), position);
		return new HitBox(rect, owner);
	}
	
	public void setSpriteFPS(double fps)
	{
		this.FPS = fps;
		if (fps <= 0)
		{
			animated = false;
			return;
		}
		this.frameDelta = 1.0/this.FPS;
	}
	
	
	public BufferedImage getFrame(int index)
	{
		return frames.get(index);
	}
	
	public void tick(TickInfo tickInfo)
	{
		if (animated)
		{
			
			currentDelta += tickInfo.delta;
			
			if (currentDelta >= frameDelta)
			{
				currentDelta = 0;
				currentFrame++;
				
				if (currentFrame > frames.size()-1)
				{
					currentFrame = 0;
				}
			}			
		}
		
	}
	
	public BufferedImage getCurrentFrame()
	{
		return frames.get(currentFrame);
		
	}
}
