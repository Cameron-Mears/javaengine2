package physics.general;

public class Vector2 
{
	private double x;
	private double y;
	
	
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
	
	public Vector2 scaled(double scalar)
	{
		return new Vector2(this.x * scalar, this.y * scalar);
	}
	
	public double angleTo(Vector2 other)
	{
		double angle =  Math.atan2(other.getY() - this.y,other.getX() - this.x);
		return (angle < 0)? (2*Math.PI)+angle:angle;
	}
	
	public double getAngle()
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
	
}
