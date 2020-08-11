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
		double angle =  Math.atan2(other.getY() - this.y,other.getX() - this.x);
		return (angle < 0)? (2*Math.PI)+angle:angle;
	}
	
	public double direction()
	{
		double angle =  Math.atan2(y, x);
		return (angle < 0)? (2*Math.PI)+angle:angle;
	}
	
	public double getMagnitude()
	{
		return Math.hypot(x, y);
	}
	
	public double dotProduct(Vector2 other)
	{
		return other.x * this.x + this.y*other.y;
	}


	public double distanceTo(Vector2 position) 
	{
		return Math.hypot(position.x - x, position.y - y);
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
