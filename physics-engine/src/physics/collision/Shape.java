package physics.collision;

import physics.general.Vector2;

public class Shape 
{
	protected Vector2 position;
	protected double width, height;
	
	protected Rectangle bounds;
	
	public Shape()
	{
		this.position = new Vector2(0,0);
	}
	
	public Shape(Vector2 position)
	{
		this.position = position;
	}
	

	public Vector2 getPosition() 
	{
		return position;
	}
	
	protected double max(double... values)
	{
		double max = Double.MIN_VALUE;
		
		for (double d : values) {
			if (d > max) max = d;
		}
		return max;
	}

}
