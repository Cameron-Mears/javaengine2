package physics.collision;

import physics.general.Vector2;

public class Rectangle extends Shape 
{
	private Vector2[] vertices;
	
	public Rectangle(double width, double height)
	{
		super();
		this.width = width;
		this.height = height;
		
		vertices = new Vector2[4];
		vertices[0] = position;
		vertices[1] = new Vector2();
		vertices[2] = new Vector2();
		vertices[3] = new Vector2();
	}
	
	public Rectangle(double x, double y, double width, double height)
	{
		this(width,height);
		position.set(x, y);
	}
	
	public Rectangle(double width, double height, Vector2 position)
	{
		this(width,height);
		this.position = position;
		this.width = width;
		this.height = height;
	}
	
	public double getWidth()
	{
		return width;
	}

	public double getHeight() 
	{
		return height;
	}
	
	
	public void setPosition(Vector2 postion)
	{
		this.position = postion;
	}
	
	public Vector2[] getVerticies()
	{
		vertices[0] = position;
		vertices[1].set(position.getX() + width, position.getY());
		vertices[2].set(position.getX(), position.getY() + height);
		vertices[3].set(position.getX() + width, position.getY() + height);
		
		return vertices;
	}
	
	public boolean contains(Vector2 point)
	{
		return (point.getX() >= position.getX() && point.getX() <= position.getX() + width) 
				&& 
				(point.getY() >= position.getY() && point.getY() <= position.getY() + height);
	}
	
	public boolean contains(Rectangle rect)
	{
		return (rect.getPosition().getX() + rect.getWidth() >= position.getX() && rect.getPosition().getX() <= position.getX() + width)
				&&
				(rect.getPosition().getY() + rect.getHeight() >= position.getY() && rect.getPosition().getY() <= position.getY() + height);
	}
	
	
	/*
	 * Check if each vertex of the other rectangle is a point inside of this rectangle
	 * if every vertex of the rectangle is inside this rectangle the other rectangle
	 * must be fully inside of this rectangle
	 */
	
	public boolean fullyContains(Rectangle rect)
	{
		Vector2[] vertices = rect.getVerticies();
		
		return (this.contains(vertices[0]) &&  this.contains(vertices[3]));
	}
}
