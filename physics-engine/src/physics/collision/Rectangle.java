package physics.collision;

import graphics.transform.Matrix;
import physics.general.Vector2;

public class Rectangle extends Shape 
{
	private Vector2[] vertices;
	private double area;
	
	@Override
	public String toString()
	{
		return "[at=" + getPosition().toString() + "][width=" + Double.toString(width) + "height=" + Double.toString(width) + "]";
	}
	
	public Rectangle(double width, double height)
	{
		super();
		this.width = width;
		this.height = height;
		
		vertices = new Vector2[4];
		vertices[0] = tx.getPosition();
		vertices[1] = new Vector2();
		vertices[2] = new Vector2();
		vertices[3] = new Vector2();
		calculateArea();
	}
	
	public Rectangle(double x, double y, double width, double height)
	{
		this(width,height);
		tx.getPosition().set(x, y);
	}
	
	public Rectangle(double width, double height, Vector2 position)
	{
		this(width,height);
		tx.setPosition(position);
		this.width = width;
		this.height = height;
	}
	
	public double area()
	{
		return area;
	}
	
	private void calculateArea()
	{
		area = width*height;
	}
	
	public double getWidth()
	{
		return width;
	}

	public double getHeight() 
	{
		return height;
	}
	
	
	public double getRotation()
	{
		return tx.getRotation();
	}
	
	public void setPosition(Vector2 postion)
	{
		tx.setPosition(postion);
	}
	
	public Vector2[] getVerticies()
	{
			Vector2 position = tx.getPosition();
			vertices[0] = tx.getPosition();
			vertices[1].set(position.getX() + width, position.getY());
			vertices[2].set(position.getX(), position.getY() + height);
			vertices[3].set(position.getX() + width, position.getY() + height);
		
		if (tx.getRotation() != 0d)
		{
			double[][] transform = new double[2][2];
			transform[0][0] = Math.cos(getRotation());
			transform[0][1] = -Math.sin(getRotation());
			transform[1][0] = Math.sin(getRotation());
			transform[1][1] = Math.cos(getRotation());
			Matrix matrix = new Matrix(transform);
			
		}
		
		return vertices;
	}
	
	public boolean contains(Vector2 point)
	{
		Vector2 position = tx.getPosition();
		return (point.getX() >= position.getX() && point.getX() <= position.getX() + width) 
				&& 
				(point.getY() >= position.getY() && point.getY() <= position.getY() + height);
	}
	
	public boolean contains(double x, double y)
	{
		Vector2 position = tx.getPosition();
		return (x >= position.x && x <= position.x+ width) 
				&& 
				(y >= position.y && y <= position.y + height);
	}
	
	
	public boolean contains(Shape shape)
	{
		if (shape instanceof Rectangle)
		{
			return contains((Rectangle)shape);
		}
		
		if (shape instanceof Circle)
		{
			return contains((Circle)shape);
		}
		
		return false;
	}
	
	public boolean contains(Rectangle rect)
	{
		Vector2 position = tx.getPosition();
		return (rect.getPosition().getX() + rect.getWidth() >= position.getX() && rect.getPosition().getX() <= position.getX() + width)
				&&
				(rect.getPosition().getY() + rect.getHeight() >= position.getY() && rect.getPosition().getY() <= position.getY() + height);
	}
	
	public boolean contains(Circle circle)
	{
		Vector2 position = tx.getPosition();
		double testX = circle.getPosition().getX();
		double testY = circle.getPosition().getY();
		if (circle.getPosition().getX() < position.getX()) testX = position.getX();      // test left edge
		else if (circle.getPosition().getX() > position.getX()+width) testX = position.getX()+width;   // right edge
		if (circle.getPosition().getY() < position.getY())         testY = position.getY();      // top edge
		else if (circle.getPosition().getY() > position.getY()+height) testY = position.getY()+height;   // bottom edge
		
		  // get distance from closest edges
		double dx= circle.getPosition().getX()-testX;
		double dy = circle.getPosition().getY()-testY;
		double distance = dx*dx + dy*dy;
		
		  // if the distance is less than the radius, collision!
		if (distance <= circle.getRadius() * circle.getRadius()) 
		{
			return true;
		}
		return false;
	}
	
	
	public void clamp(Rectangle rect)
	{
		Vector2 other = rect.getPosition();
		if (other.x  < position.x) other.x = position.x;
		if (other.x + rect.width  > position.x + width) other.x = position.x + width - rect.width;
		if (other.y  < position.y) other.y = position.y;
		if (other.y + rect.height > position.y + height) other.y = position.y + height - rect.height;
	}
	
	
	private boolean rotatedRectCollision(Rectangle other)
	{
		return false;
	}
	
	/*
	 * Check if each vertex of the other rectangle is a point inside of this rectangle
	 * if every vertex of the rectangle is inside this rectangle the other rectangle
	 * must be fully inside of this rectangle
	 */
	
	public boolean fullyContains(Rectangle rect)
	{		
		return (this.contains(rect.getPosition()) && this.contains(rect.getPosition().x + rect.width, rect.getPosition().y + rect.height));
	}
}
