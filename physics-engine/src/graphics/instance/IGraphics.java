package graphics.instance;

import java.awt.Graphics2D;

import physics.collision.Rectangle;

public interface IGraphics 
{
	public void render(Graphics2D g2);
	
	public Rectangle renderBoundingArea();
	
	public default int getRenderPriority()
	{
		return -1;
	}
}
