package graphics.instance;

import java.awt.Graphics2D;

import physics.collision.Rectangle;

public interface IGraphics 
{
	public void render(Graphics2D g2);
	
	/**
	 * 
	 * @return cannot be null
	 */
	public Rectangle renderBoundingArea();
	
	public default int getRenderPriority()
	{
		return -1;
	}
}
