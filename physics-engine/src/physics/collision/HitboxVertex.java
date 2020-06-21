package physics.collision;

import physics.general.Vector2;

public class HitboxVertex 
{
	private HitBox parent;
	private Vector2 position;
	
	
	public HitboxVertex(HitBox parent, Vector2 postion)
	{
		this.parent = parent;
		this.position = postion;
	}
	
	public Vector2 getPosition()
	{
		return position;
	}
	
	public boolean testCollision(HitBox other)
	{
		return false;
	}
	
	public HitBox getHitBox()
	{
		return parent;
	}
}
