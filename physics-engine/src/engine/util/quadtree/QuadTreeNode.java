package engine.util.quadtree;

import java.util.LinkedList;

import physics.general.Vector2;

public class QuadTreeNode<Value>
{
	private Value value;
	private Vector2 position;
	private LinkedList<QuadTreeNode<Value>> parentlist;
	
	public QuadTreeNode(Vector2 position, Value value)
	{
		this.value = value;
		this.position = position;
	}
	
	public void setParentList(LinkedList<QuadTreeNode<Value>> parentlist)
	{
		this.parentlist = parentlist;
	}
	
	public Value get()
	{
		return value;
	}
	
	public Vector2 getPosition()
	{
		return position;
	}
	
	public boolean removeFromQuadTree()
	{
		if (parentlist != null) return parentlist.remove(this);
		return false;
	}
}
