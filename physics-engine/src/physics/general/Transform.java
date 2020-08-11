package physics.general;

public class Transform 
{
	private Vector2 position;
	private double rotation;

	public Transform()
	{
		position = new Vector2(0,0);
	}
	
	public Transform(double x, double y)
	{
		position = new Vector2(x,y);
	}
	public Transform(Vector2 vector)
	{
		position = vector;
	}
	
	public void setPosition(Vector2 vector)
	{
		position = vector;
	}
	public Vector2 getPosition()
	{
		return position;
	}
	
	public double getRotation()
	{
		return rotation;
	}
	
	public void addRotation(double rotation)
	{
		this.rotation += rotation;
	}
	
	public void setRotation(double rotation)
	{
		this.rotation = rotation; 
	}

}
