package physics.collision.ray;

import java.awt.Graphics2D;
import java.util.LinkedList;

import physics.collision.Rectangle;
import physics.general.Vector2;

public class Polygon 
{
	private LinkedList<Ray> rays;
	private LinkedList<Vector2> verticies;
	
	public Polygon(boolean loop, Vector2 ...verticies)
	{
		if (verticies.length < 2) throw new IllegalArgumentException("Polygon must contain at least 2 verticies");
		
		Vector2 start = verticies[0];
		Vector2 end = verticies[verticies.length-1];
		
		Vector2 prev = start;
		this.verticies.add(start);
		for (int i = 1; i < verticies.length; i ++)
		{
			Vector2 current = verticies[i];
			Ray ray = new Ray(prev, current);
			rays.add(ray);
			prev = current;
			this.verticies.add(current);
		}
		
		if (loop)
		{
			rays.add(new Ray(start,end));
		}
	}
	
	
	public Polygon(Rectangle rect)
	{
		
	}
	
	public void render(Graphics2D g2)
	{
		
	}
	
	public Vector2 getClosestHit(Ray ray)
	{
		Vector2 closest = null;
		for (Ray r : rays) 
		{
			Vector2 hit = r.intersects(ray);
			if (hit != null)
			{
				if (closest == null) closest = hit;
				else if (closest.distanceToSq(ray.origin()) > hit.distanceToSq(ray.origin())) closest = hit; 
			}
		}
		return null;
	}
	
	public boolean intersects(Ray ray)
	{
		for (Ray r : rays) {
			if (r.getIntersect(ray) != Double.NaN) return true;
		}
		return false;
	}
}
