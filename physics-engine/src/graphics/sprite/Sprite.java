package graphics.sprite;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.core.tick.TickInfo;

public class Sprite 
{
	private long frameDeltaNanos;
	private long currentDelta;
	
	private int currentFrame;
	private String name;
	private int FPS;
	private ArrayList<BufferedImage> frames;
	private boolean animated;
	
	
	@Override
	public Sprite clone()
	{
		return new Sprite(FPS, frames, name);
	}
	
	
	public Sprite(int fps, ArrayList<BufferedImage> frames, String assetName)
	{
		this.frames = frames;
		this.name = assetName;
		this.setSpriteFPS(fps);
		this.currentFrame = 0;
		this.currentDelta = 0;
	}
	
	public boolean isAnimated()
	{
		return true;
	}
	
	public void setAnimated(boolean animated)
	{
		this.animated = animated;
	}
	
	public int getCurrentFPS()
	{
		return FPS;
	}
	
	public void setSpriteFPS(int fps)
	{
		this.FPS = fps;
		this.frameDeltaNanos = (long)(1E9)/this.FPS;
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
			
			if (currentDelta >= frameDeltaNanos)
			{
				currentDelta = 0;
				currentFrame++;
				
				if (currentFrame > frames.size())
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
