package graphics.instance;

import java.awt.Graphics2D;

import engine.core.instance.EngineComponent;
import physics.collision.Rectangle;

public interface IGraphics extends EngineComponent
{
	public void render(Graphics2D g2);
	
	public Rectangle renderBoundingArea();
	
	public default int getRenderPriority()
	{
		return -1;
	}
}
