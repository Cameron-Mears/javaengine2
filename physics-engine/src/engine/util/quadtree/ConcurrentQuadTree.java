package engine.util.quadtree;

import java.util.ArrayList;
import java.util.LinkedList;

import physics.collision.Rectangle;
import physics.general.Vector2;

public class ConcurrentQuadTree
{
	private static final int LEAF_CAPACITY = 10;
	
	private Rectangle bounds;
	private LinkedList<ConcurrentQuadTreeNode<?>> leafs;
	//private Root<V> root;
	private boolean divided;
	private int leafCapacity;
	
	private Object divideLock = new Object();
	
	//children
	private ConcurrentQuadTree topRight;
	private ConcurrentQuadTree bottomRight;
	private ConcurrentQuadTree topLeft;
	private ConcurrentQuadTree bottomLeft;
	private int height;
	
	
	private ConcurrentQuadTree parent;
	/**
	 * Provides subtrees acees to a map of the entire tree
	 * @author Cameron
	 *
	 * @param <K> a Key to qucikly access data in tree
	 * @param <V>
	 */
	private static class Root
	{
//		ConcurrentHashMap<V,V> map;
//		
//		Root()
//		{
//			map = new ConcurrentHashMap<V, V>();
//		}
	}
	
	public ConcurrentQuadTree(Rectangle bounds)
	{
		this.bounds = bounds;
		//this.root = new Root();
		this.leafs = new LinkedList<ConcurrentQuadTreeNode<?>>();
		this.leafCapacity = ConcurrentQuadTree.LEAF_CAPACITY;
	}
	
	public ConcurrentQuadTree(Rectangle bounds, int leafCapacity)
	{
		this(bounds);
		this.leafCapacity = leafCapacity;
	}
	
	public ConcurrentQuadTree(Vector2 origin, double width, double height)
	{
		this(new Rectangle(height, width, origin));
	}
	

	public ConcurrentQuadTree(double x, double y, double width, double height, int leafCapacity)
	{
		this(new Rectangle(x,y,width,height), leafCapacity);
	}
	
	public ConcurrentQuadTree(double x, double y, double width, double height)
	{
		this(new Rectangle(x,y,width,height));
	}
	
	public ConcurrentQuadTree(Vector2 origin, double width, double height, int leafCapacity)
	{
		this(new Rectangle(height, width, origin), leafCapacity);
	}
	
	private ConcurrentQuadTree(Vector2 origin, double width, double height, int leafCapacity, ConcurrentQuadTree parent)
	{
		this(new Rectangle(height, width, origin), leafCapacity);
		this.parent = parent;
		parent.height++;
	}
	
	private void insert (ConcurrentQuadTreeNode<?> value)
	{
		if (leafs.size() < leafCapacity && !divided)
		{
			synchronized (leafs) {
				leafs.add(value);
			}
			value.setParent(this);
			return;
		}
		else
		{
			Rectangle area = value.getBounds();
			if (!this.divided) subdivide();
			
			if (topRight.fullyContains(area))
			{
				topRight.insert(value);
				return;
			}
			else if (topLeft.fullyContains(area))
			{
				topLeft.insert(value);
				return;
			}
			else if (bottomRight.fullyContains(area))
			{
				bottomRight.insert(value);
				return;
			}
			else if (bottomLeft.fullyContains(area))
			{
				bottomLeft.insert(value);
				return;
			}
			
		}
		leafs.add(value); //cant fit into a subtree so insert into leafs
		value.setParent(this);
		
	}
	
	private boolean fullyContains(final Rectangle area)
	{
		synchronized (area) 
		{
			return area.fullyContains(bounds);
		}
	}
	
	/**
	 * 
	 * @param area the area of the
	 * @return
	 */
	public LinkedList<ConcurrentQuadTreeNode<?>> query(Rectangle area)
	{
		LinkedList<ConcurrentQuadTreeNode<?>> result = new LinkedList<ConcurrentQuadTreeNode<?>>();
		query(result, area);
		return result;
	}
	
	private void query(LinkedList<ConcurrentQuadTreeNode<?>> list, Rectangle area)
	{
		if (this.fullyContains(area))
		{
			synchronized (leafs) {
				list.addAll(leafs);
			}
		}
		else
		{
			for (ConcurrentQuadTreeNode<?> concurrentQuadTreeNode : leafs) 
			{
				if (concurrentQuadTreeNode.getBounds().contains(area)) list.add(concurrentQuadTreeNode);
			}
		}
		
		if (!divided) return;
		if (topRight.contains(area)) topRight.query(list, area);
		if (topLeft.contains(area)) topLeft.query(list, area);
		if (bottomRight.contains(area)) bottomRight.query(list, area);
		if (bottomLeft.contains(area)) bottomLeft.query(list, area);
		
		return;
		
	}
	
	public boolean contains(Rectangle area)
	{
		return bounds.contains(area);
	}
	
	private void subdivide()
	{
		synchronized (divideLock) 
		{	
			if (!divided)
			{
				double childWidth = bounds.getWidth()/2;
				double childHeight = bounds.getHeight()/2;
				double x = bounds.getPosition().getX();
				double y = bounds.getPosition().getY();
				
				this.topRight = new ConcurrentQuadTree(new Vector2(x + childWidth,y), childWidth, childHeight, leafCapacity, this);
				this.topLeft =  new ConcurrentQuadTree(new Vector2(x,y), childWidth, childHeight, leafCapacity, this);
				this.bottomRight =  new ConcurrentQuadTree(new Vector2(x + + childWidth,y + childHeight), childWidth, childHeight, leafCapacity, this);
				this.bottomLeft = new ConcurrentQuadTree(new Vector2(x,y + childHeight), childWidth, childHeight, leafCapacity, this);
				this.divided = true;
			}
		}
		
		for (int i = 0; i < leafs.size(); i++) 
		{
			synchronized (leafs) {
				ConcurrentQuadTreeNode<?> value = leafs.poll();
				insert(value);
			}
		}
	}
	
	/**
	 * Updates the position of a node in the quadtree, if the node no longer fits in the tree it will be deleted
	 * @param value
	 */
	public void updateValue(ConcurrentQuadTreeNode<?> value)
	{
		Rectangle bounds = value.getBounds();
		if (this.fullyContains(bounds))
		{
			value.checked = true;
			return; //nothing has changed still fits in its current place
		}
		ConcurrentQuadTree parentTree = parent; //create a tree pointer to iterate through parent trees
		if (parentTree == null) this.remove(value); //this is the parentTree, so the value no longer fits in quadtree remove it
		do
		{
			if (parentTree.fullyContains(bounds)) //parent tree contains value remove from this tree and insert into parent tree
			{
				this.remove(value);
				parentTree.insert(value);
				return;
			}
			
		} while ((parentTree = parentTree.parent) != null);
		this.remove(value); //not in any tree
			
	}
	
	private void remove(ConcurrentQuadTreeNode<?> value) 
	{
		leafs.remove(value);
	}

	public void put(ConcurrentQuadTreeNode<?> value)
	{
		if (value == null) return;
		
		insert(value);
		
	}
	
}
