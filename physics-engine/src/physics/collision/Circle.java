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
	
	public double getRadius()
	{
		return radius;
	}
	
	public boolean contains(Vector2 point)
	{
		double dx = Math.abs(point.getX() - position.getX());
		double dy = Math.abs(point.getY() - position.getY());
		return radius * radius >= dx*dx + dy*dy;
	}
}
