package physics.general;

public class Vector2 
{
	private double x;
	private double y;
	
	public Vector2()
	{
		
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
	
}
