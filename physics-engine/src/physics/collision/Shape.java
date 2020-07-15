package physics.collision;

import physics.general.Transform;
import physics.general.Vector2;

public class Shape 
{
	protected Transform tx;
	protected double width, height;
	
	protected Rectangle bounds;
	
	public Shape()
	{
		tx = new Transform();
	}
	
	public Shape(Vector2 position)
	{
		tx = new Transform(position);
	}
	

	public Vector2 getPosition() 
	{
		return tx.getPosition();
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
