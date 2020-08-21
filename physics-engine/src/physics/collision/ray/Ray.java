package physics.collision.ray;

import java.util.LinkedList;

import engine.util.math.Range;
import physics.PhysicsException;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class Ray 
{
	private LinkedList<Vector2> hits;
	
	private Range xRange;
	private Range yRange;
	
	public static enum Mode
	{
		
	}
	
	private double m, b; // y = mx + b
	
	private Vector2 s; //start
	private Vector2 e; //end
	
	public Ray(Vector2 origin, double direction, double length)
	{
		this.s = origin;
		this.e = new Vector2();
		e.setDirMag(direction, length);
		e.add(origin);
		init();
	}
	
	public Ray(Vector2 origin, Vector2 end)
	{
		if (origin.equals(end)) throw new PhysicsException("Origin and End of the cannot be the same");
		s= origin;
		e=end;
		init();
	}
	
	private void init() // setup y = mx + b
	{
		m = (s.y-e.y)/(s.x-e.x); // m = dy/dx
	 	b = -(m*s.x - s.y); // m p mx + b --> -b = mx - y
	 	xRange = new Range(s.x, e.x,true);
	 	yRange = new Range(s.y, e.y,true);
	}
	
	public LinkedList<Vector2> getHits(Rectangle rect)
	{
		LinkedList<Vector2> hits = new LinkedList<>();
		Vector2[] rv = rect.getVerticies();
		/*
		 * [0]----[1]
		 * |	    |
		 * |	    |
		 * |	    |
		 * [3]----[2]
		 */
		
		
				
		
		//left x = ly , right x = ry
		double ly = yAt(rv[0].x);
		double ry = yAt(rv[1].x);
		
		double tx = xAt(rv[0].y);
		double bx = xAt(rv[2].y);
		
		if (ly >= rv[0].y && ly <= rv[3].y) //intersect between topleft and bottom left verticie
		{
			if (yRange.contains(ly)) hits.add(new Vector2(rv[0].x,ly));
		}
		
		if (ry >= rv[1].y && ry <= rv[2].y)
		{
			if (yRange.contains(ry)) hits.add(new Vector2(rv[1].x,ry));
		}
		
		if (bx >= rv[3].x && bx <= rv[2].x)
		{
			if (xRange.contains(bx)) hits.add(new Vector2(bx,rv[2].y));
		}
		
		if (tx >= rv[0].x && tx <= rv[1].x)
		{
			if (xRange.contains(tx)) hits.add(new Vector2(tx,rv[0].y));
		}
		
		
		return hits;
	}
	
	public boolean intersects(Rectangle rect)
	{
		Vector2[] rv = rect.getVerticies();
		//left x = ly , right x = ry
		double ly = yAt(rv[0].x);
		double ry = yAt(rv[1].x);
		
		double tx = xAt(rv[0].y);
		double bx = xAt(rv[2].y);
		if (ly >= rv[0].y && ly <= rv[3].y) //intersect between topleft and bottom left verticie
		{
			if (yRange.contains(ly)) return true;
		}
		
		if (ry >= rv[1].y && ry <= rv[2].y)
		{
			if (yRange.contains(ry)) return true;
		}
		
		if (bx >= rv[3].x && bx <= rv[2].x)
		{
			if (xRange.contains(bx))  return true;
		}
		
		if (tx >= rv[0].x && tx <= rv[1].x)
		{
			if (xRange.contains(tx))  return true;
		}
		return false;
	}
	
	
	public Vector2 intersects(Ray ray)
	{
		double intersect = getIntersect(ray);
		if (intersect == Double.NaN);
		return new Vector2(intersect, yAt(intersect));
	}
	
	public double getIntersect(Ray ray)
	{
		if (ray == null || hasSolution(m, ray.m)) return Double.NaN;
		/*
		 *	m1 * x + b1 = m2 * x + b2
		 * 	m1 * x - (m2*x) = b2-b1
		 *  x(m1-m2) =b2-b1
		 *  x = (b2/b1)/(m1-m2)
		 */
		return (ray.b - b)/(m - ray.m);
	}
	 
	private boolean hasSolution(double m, double m2)
	{
		//SLOPES CANT BE THE SAME
		return m != m2;
	}
	
	
	public Vector2 closestHit(Rectangle rect)
	{
		LinkedList<Vector2> hits = getHits(rect);
		if (hits.size() == 0) return null;
		Vector2 closest = hits.poll();
		double distanceSq = s.distanceToSq(closest);
		while (!hits.isEmpty())
		{
			Vector2 vec = hits.poll();
			double distance = s.distanceToSq(vec);
			if (distance < distanceSq) closest = vec;
		}
		return closest;
	}
	
	
	
	public double xAt(double y)  // x = (y-b)/m
	{
		return (y-b)/m;
	}
	
	public double yAt(double x) // y = mx + b
	{
		return m*x +b;
	}

	public Vector2 origin() {
		return s;
	}
	
	public Vector2 end() {
		return e;
	}
}
