package graphics.util;

import java.awt.Graphics2D;

import physics.collision.Circle;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class GraphicsUtils 
{
	/**
	 * 
	 * @param width the width of the line to center
	 * @return the x coordinate for where the line should be draw to be centered with the totalWidth
	 */
	public static int center(int width, int totalWidth)
	{
		return (totalWidth-width)/2;
	}
	
	public static final int putRight(int width, int totalWidth)
	{
		return totalWidth - width;
	}
	
	public static final int putBottom(int height, int totalHeight)
	{
		return totalHeight - height;
	}

	public static void drawRect(Graphics2D g2, Rectangle r) {
		
		g2.drawRect((int)r.getX(),(int) r.getY(), (int)r.getWidth(), (int)r.getHeight());
		
	}
	
	public static void fillRect(Graphics2D g2, Rectangle r) {
		
		g2.fillRect((int)r.getX(),(int) r.getY(), (int)r.getWidth(), (int)r.getHeight());
		
	}
	
	public static void drawCircle(Graphics2D g2, Circle r) {
		double rad = r.getRadius();
		g2.drawOval((int)(r.getX()-rad),(int) (r.getY()-rad), (int)r.getRadius()*2, (int)r.getRadius()*2);
		
	}
	
	public static void drawLine(Graphics2D g2, Vector2 p, Vector2 p2) 
	{		
		g2.drawLine((int)p.x,(int) p.y, (int)p2.x, (int)p2.y);
		
	}
}
