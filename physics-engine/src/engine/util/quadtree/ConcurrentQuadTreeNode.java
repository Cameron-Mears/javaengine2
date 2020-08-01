package engine.util.quadtree;

import physics.collision.Collidable;
import physics.collision.Rectangle;

public class ConcurrentQuadTreeNode<Value> 
{
	private Rectangle bounds;
	private ConcurrentQuadTree parentTree;
	private Collidable collidable;
	private Value value;
	
	boolean checked;
	
	public ConcurrentQuadTreeNode(Rectangle bounds, Collidable c)
	{
		this.bounds = bounds;
		this.collidable = c;
	}
	
	public Value getValue()
	{
		return value;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	void setParent(ConcurrentQuadTree parent)
	{
		this.parentTree = parent;
	}
	
	public void update() 
	{	
		checked = false;
		parentTree.updateValue(this);		
	}
	
	
	
}
