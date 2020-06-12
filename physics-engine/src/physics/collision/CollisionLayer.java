package physics.collision;

import graphics.layer.Layer;
import physics.body.PhysicsBody;

public class CollisionLayer extends Layer
{
	
	public CollisionLayer(String name) 
	{
		super(name);
		
	}

	public boolean addBody(PhysicsBody body)
	{
		return false;
	}
	
	public void resloveCollisions()
	{
		
	}
}
