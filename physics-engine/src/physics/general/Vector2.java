package physics.general;

import engine.util.json.JSONSerializable;
import external.org.json.JSONObject;

public class Vector2 implements JSONSerializable
{
	public double x;
	public double y;
	
	public static void translateVectors(double dx, double dy, Vector2 ...vectors)
	{
		for (Vector2 vector : vectors) 
		{
			vector.addX(dx);
			vector.addY(dy);
		}
	}
	
	public static Vector2 fromJSON(JSONObject obj)
	{
		return new Vector2(obj.getDouble("x"), obj.getDouble("y"));
	}
	
	
	@Override
	public boolean equals(Object object)
	{
		if (object instanceof Vector2)
		{
			Vector2 other = (Vector2) object;
			return other.x == this.x && other.y == this.y;
		}
		return false;
	}
	
	
	public Vector2 duplicate()
	{
		return new Vector2(x, y);
	}
	
	@Override
	public String toString()
	{
		return "(" + Double.toString(x) + ", " + Double.toString(y) + ")";
	}
	
	public boolean equals(double x, double y)
	{
		return x == this.x && y == this.y;
	}
	
	@Override
	public Vector2 clone()
	{
		return new Vector2(x, y);
	}
	
	public Vector2()
	{
		x = 0;
		y = 0;
	}
	
	public Vector2(JSONObject object)
	{
		this.x = object.getDouble("x");
		this.y = object.getDouble("y");
	}
	
	public Vector2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void set(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the x and y components of the vector based off a directon and mangitude
	 * @param theta the angle in radians
	 * @param mangnitude the magnitude of the vectoe
	 */
	public void setDirMag(double theta, double mangnitude)
	{
		this.x = Math.cos(theta) * mangnitude;
		this.y = Math.sin(theta) * mangnitude;
	}

	public double getX() 
	{
		return x;
	}

	public void setX(double x) 
	{
		this.x = x;
	}

	public double getY() 
	{
		return y;
	}

	public void setY(double y) 
	{
		this.y = y;
	}
	
	public double multiply(double a)
	{
		return this.x * a + this.y * a;
	}

	public void add(Vector2 other) 
	{
		this.y += other.y;
		this.x += other.x;
	}
	
	public void addX(double x)
	{
		this.x += x;
	}
	
	public void addY(double y)
	{
		this.y += y;
	}
	
	public void add(double x, double y)
	{
		this.x += x;
		this.y += y;
	}
	
	public Vector2 scaled(double scalar)
	{
		return new Vector2(this.x * scalar, this.y * scalar);
	}
	
	public double angleTo(Vector2 other)
	{
		double angle =  Math.atan2(-(other.y - y), other.x - x);
		return (angle < 0)? angle+Math.PI*2:angle;
	}
	
	public double ato(Vector2 o)
	{
		double dx = o.x-x;
		double dy = -(o.y-y);
		if (dx == 0) return Double.NaN;
		if (dy == 0) return (dx > 0)? 0:Math.PI;
		double ref = Math.atan(Math.abs(dy)/Math.abs(dx));
		if (dx > 0 && dy > 0) return ref; //quadrant 1
		if (dx < 0 && dy > 0) return Math.PI-ref; //quadrant 2
		if (dx < 0 && dy < 0) return Math.PI+ref; //quadrant 3
		return (Math.PI*2) - ref; //quadrant 4
	}
	public double direction()
	{
		double angle =  Math.atan2(-y, x);
		return (angle < 0)? (2*Math.PI)+angle:angle;
	}
	
	public double getMagnitude()
	{
		return Math.sqrt(x*x+y*y);
	}
	
	public double dotProduct(Vector2 other)
	{
		return other.x * this.x + this.y*other.y;
	}


	public double distanceTo(Vector2 position) 
	{
		double dx = position.x - x;
		double dy =  position.y - y;
		return Math.sqrt(dx*dx+dy*dy);
	}
	/**
	 * returns the square of the distance to the specified position avoiding expensive square root calculations
	 */
	
	public double distanceToSq(Vector2 position)
	{
		double dx = position.x - x;
		double dy =  position.y - y;
		return (dx*dx)+(dy*dy);
	}

	@Override
	public JSONObject serialize()
	{
		JSONObject obj = new JSONObject();
		obj.put("x", x);
		obj.put("y", y);
		return obj;
	}
	
}
