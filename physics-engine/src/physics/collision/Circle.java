package physics.collision;

import physics.general.Vector2;

public class Circle extends Shape
{
	private double radius;
	
	public Circle(Vector2 position, double radius)
	{
		super(position);
		this.radius = radius;
	}
	
	public Circle(double x, double y, double radius)
	{
		this(new Vector2(x, y), radius);
	}
	
	public double getRadius()
	{
		return radius;
	}
	
	public boolean contains(Vector2 point)
	{
		Vector2 position = tx.getPosition();
		double dx = Math.abs(point.getX() - position.getX());
		double dy = Math.abs(point.getY() - position.getY());
		return radius * radius >= dx*dx + dy*dy;
	}
}
