package engine.util.quadtree;

import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class CollisionNode<Value> extends QuadTreeNode<Value>
{

	private HitBox bounds;
	private boolean wasCheckedForChildNode = false;
	
	
	public CollisionNode(Vector2 position, Value value, HitBox bounds) 
	{
		super(position, value);
		this.bounds = bounds;
	}
	
	public boolean wasCheckAgainstChild()
	{
		return wasCheckedForChildNode;
	}
	
	public void checked()
	{
		this.wasCheckedForChildNode = true;
	}
	
	public void unChecked()
	{
		this.wasCheckedForChildNode = false;
	}
	
	public HitBox getHitbox()
	{
		return bounds;
	}
	
	public Rectangle getBounds()
	{
		return bounds.getBounds();
	}
}
