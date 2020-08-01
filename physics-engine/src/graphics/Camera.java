package graphics;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import engine.core.exceptions.EngineException;
import graphics.image.CompatibleImageFactory;
import graphics.layer.GraphicsLayer;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class Camera 
{
	private String name;
	private Rectangle bounds;
	private boolean enabled;
	private double scale;
	
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
	
	public Camera(Rectangle bounds, double scale, String name)
	{
		this.name = name;
		this.scale = scale;
		this.enabled = true;
		this.bounds = bounds;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	
	public BufferedImage capture(LinkedList<GraphicsLayer> layerQueue)
	{
		BufferedImage img = CompatibleImageFactory.createCompatibleImage((int)bounds.getWidth(), (int)bounds.getHeight(), CompatibleImageFactory.DEFAULT_TRANSPARENCY);
		Graphics2D g2 = img.createGraphics();
		
		for (GraphicsLayer graphicsLayer : layerQueue) 
		{
			capture(graphicsLayer, g2);
		}
		g2.dispose();
		return img;
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
	public void capture(GraphicsLayer layer, Graphics2D g2)
	{
		g2.setTransform(new AffineTransform());
		g2.scale(scale, scale);
		g2.translate(bounds.getPosition().getX(), bounds.getPosition().getY());
		layer.render(this,g2);
	}

	public String getName() 
	{
		return name;
	}
}
