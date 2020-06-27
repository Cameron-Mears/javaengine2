package graphics;

import java.awt.Transparency;
import java.awt.image.BufferedImage;

import engine.core.exceptions.EngineException;
import graphics.image.CompatibleImageFactory;
import graphics.layer.GraphicsLayer;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class Camera 
{
	private Rectangle bounds;
	private boolean enabled;
	
	public boolean isEnabled() 
	{
		return enabled;
	}

	public void enable()
	{
		this.enabled = true;
	}
	
	public void disable()
	{
		this.enabled = false;
	}


	public void setPosition(double x, double y)
	{
		bounds.getPosition().set(x, y);
	}
	
	public Camera(Rectangle bounds)
	{
		this.enabled = true;
		this.bounds = bounds;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	/**
	 * 
	 * @param pos - The position to the camera to go to
	 * @param follow - if true the camera position vector will become the the Vector2 provided, if false
	 * the camera position will just become the current position of the Vector2 supplied
	 * @throws EngineException if the Vector provided is null
	 */
	
	public void setPosition(Vector2 pos, boolean follow) throws EngineException
	{
		if (pos != null)
		{
			if (follow)
			{
				bounds.setPosition(pos);
				return;
			}
			bounds.getPosition().set(pos.getX(), pos.getY());
		} else throw new EngineException("Camera Position Vector2 cannot be null");
	}
	
	public Vector2 getPosition()
	{
		return bounds.getPosition();
	}
	
	/**
	 * Will render all instances inside the graphics layer that inside the graphics layer
	 * @param layer The GraphicsLayer to be captured
	 * @return a bufferedImage of the 
	 */
	public BufferedImage capture(GraphicsLayer layer)
	{
		//create the img to be capture
		BufferedImage img = CompatibleImageFactory.createCompatibleImage((int)bounds.getWidth(), (int)bounds.getHeight(), Transparency.BITMASK);
		return null;
	}
}
