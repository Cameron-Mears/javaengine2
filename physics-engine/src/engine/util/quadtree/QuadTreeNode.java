package engine.util.quadtree;

import physics.general.Vector2;

public class QuadTreeNode<Value>
{
	private Value value;
	private Vector2 position;
	
	public QuadTreeNode(Vector2 position, Value value)
	{
		this.value = value;
		this.position = position;
	}
	
	public Value get()
	{
		return value;
	}
	
	public Vector2 getPosition()
	{
		return position;
	}
}
