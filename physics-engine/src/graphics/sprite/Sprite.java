package graphics.sprite;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.core.tick.TickInfo;

public class Sprite 
{
	private double frameDelta;
	private double currentDelta;
	
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
	
	public int getCurrentFPS()
	{
		return FPS;
	}
	
	public void setSpriteFPS(int fps)
	{
		this.FPS = fps;
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
