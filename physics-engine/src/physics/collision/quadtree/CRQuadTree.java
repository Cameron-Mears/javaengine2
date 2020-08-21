package physics.collision.quadtree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import engine.core.instance.InstanceID;
import engine.core.instance.InstanceMap;
import physics.collision.Rectangle;
import physics.collision.ray.Ray;
import physics.general.Vector2;

/**
 * CRQuadTree -> ConcurrentRegionQuadTree
 * 
 * @author Cameron
 *
 * @param <V>
 */

public class CRQuadTree<V> implements Iterable<V>
{
	private InstanceMap<Node<V>> map;
	private InstanceMap<CRQuadTree<V>> treemap;
	private CRQuadTree<V> parent;
	private CRQuadTree<V> root;
	private InstanceID<CRQuadTree<V>> id;
	private Rectangle bounds;
	private int leafCapacity;	
	
	@Override
	public boolean equals(Object anObject)
	{
		return (anObject instanceof CRQuadTree)? ((CRQuadTree<?>)anObject).id.equals(id):false;
	}
	
	@Override
	public String toString()
	{
		return bounds.toString() + "[divided=" + Boolean.toString(divided) + "]";
	}
	
	//private AVL<Long, Node<V>> leafs;
	private LinkedList<Node<V>> leafs;
	public static class Node<V>
	{
		V value;
		long id;
		Rectangle bounds;
		LinkedList<Node<V>> myList;
		CRQuadTree<V> tree;
	}
	
	private Object divideLock = new Object();
	
	private CRQuadTree<V> northWest;
	private CRQuadTree<V> northEast;
	private CRQuadTree<V> southWest;
	private CRQuadTree<V> southEast;
	
	private boolean divided;
	
	public CRQuadTree(int leafCapaity, Rectangle boundary)
	{
		this(leafCapaity);
		this.parent = null;
		this.root = this;
		this.bounds = boundary;
		this.map = new InstanceMap<Node<V>>();
		this.treemap = new InstanceMap<CRQuadTree<V>>();
		this.id = treemap.newInstanceID(this);
	}
	
	private CRQuadTree(int leafCapacity)
	{
		this.leafCapacity = leafCapacity;
		this.leafs = new LinkedList<>();
	}
	
	private CRQuadTree(int leafCapacity, Rectangle boundary, CRQuadTree<V> parent, CRQuadTree<V> root)
	{
		this(leafCapacity);
		this.map = parent.map;
		this.treemap = parent.treemap;
		this.id = treemap.newInstanceID(this);
		this.parent = parent;
		this.bounds = boundary;
	}
	
	
	private void subdivide()
	{
		synchronized (divideLock) 
		{
			if (!divided)
			{
				double x = bounds.getX();
				double y = bounds.getY();
				double w = bounds.getWidth()/2.0;
				double h = bounds.getHeight()/2.0;
				
				northEast = new CRQuadTree<V>(leafCapacity, new Rectangle(x, y, w, h), this, root);
				northWest = new CRQuadTree<V>(leafCapacity, new Rectangle(x+w, y, w, h), this, root);
				southEast = new CRQuadTree<V>(leafCapacity, new Rectangle(x, y+h, w, h), this, root);
				southWest = new CRQuadTree<V>(leafCapacity, new Rectangle(x+w, y+h, w, h), this, root);
				divided = true;
				checkLeafs();
			}
		}
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	/**
	 * opposite of subdivide
	 */
	private void eatChildren()
	{
		LinkedList<Node<V>> nodes = getAllNodes();
		leafs.clear();
		synchronized (leafs) 
		{
			for (Node<V> node : nodes) {
				add(node);
			}
		}
		divided = false;
		synchronized (divideLock) 
		{
			northEast = null;
			northWest = null;
			southEast = null;
			southWest = null;
		}
	}
	
	public int sum(int sum)
	{
		sum += leafs.size();
		if (sum > leafCapacity) return sum;
		if (!divided) return sum;
		else
		{
			sum += northEast.sum(sum);
			sum += northWest.sum(sum);
			sum += southEast.sum(sum);
			sum += southWest.sum(sum);
			return sum;
		}
		
	}
	
	private boolean insertToChildren(Node<V> entry)
	{
		if (northWest.insert(entry)) return true;
		if (northEast.insert(entry)) return true;
		if (southWest.insert(entry)) return true;
		if (southEast.insert(entry)) return true;
		return false;
	}
	
	private boolean insert(Node<V> entry)
	{
		Rectangle rect =  entry.bounds;
		if (this.bounds.fullyContains(rect))
		{
			if (hasCapacity())
			{
				synchronized (leafs) 
				{
					add(entry);
				}
			}
			else
			{
				if (!divided) subdivide();
				if (!insertToChildren(entry)) add(entry);
				
			}
			return true;
		}
		return false;
	}
	/**
	 * entry does not fit into children so it has to be kept in this layer
	 * @param node
	 */
	private void add(Node<V> node)
	{
		synchronized (leafs) 
		{
			if (node.myList != null)
			{
				node.myList.remove(node);
			}
			leafs.add(node);
			node.myList = leafs;
			node.tree = this;
		}
		return;
		
		/*
		if (node.tree == null)
		{
			synchronized (leafs) 
			{
				if (node.myList != null)
				{
					node.myList.remove(node);
				}
				leafs.add(node);
				node.myList = leafs;
				node.tree = this;
			}
			return;
		}
		
		if (!this.equals(node.tree)) //check if adding to same level from updateEntry()
		{
			synchronized (leafs) 
			{
				if (node.myList != null)
				{
					node.myList.remove(node);
				}
				leafs.add(node);
				node.myList = leafs;
				node.tree = this;
			}
		}
		*/
	}
	
	
	/**
	 *  Returns True if the leafs capacity will not be exceeded by adding another entry
	 * @return 
	 */
	private boolean hasCapacity()
	{
		return leafs.size() + 1 <= leafCapacity;
	}
	
	/**
	 * Checks if leaves can be fit into subtress if not they stay at the current level
	 */	
	private void checkLeafs() 
	{	
		synchronized (leafs) 
		{			
			Node<V> node = null;
			int size = leafs.size();
			for (int i = 0; i < size; i++)
			{
				node = leafs.poll();
				if (!insertToChildren(node)) leafs.add(node);
			}
		}
	}
	
	public int size()
	{
		return map.size();
	}
	
	/**
	 * Returns all values for which the queried region may intersect
	 * @param region
	 * @return
	 */
	public LinkedList<V> queryPossible(Rectangle region)
	{
		LinkedList<V> list = new LinkedList<>();
		this.queryPossible(region, list);
		return list;
	}
	
	private void queryPossible(Rectangle region, LinkedList<V> list)
	{
		if (this.bounds.contains(region))
		{
			synchronized (leafs) 
			{
				for (Node<V> node : leafs) 
				{
					list.add(node.value);
				}
			}
			
			if (divided)
			{
				northWest.queryPossible(region, list);
				northEast.queryPossible(region, list);
				southWest.queryPossible(region, list);
				southEast.queryPossible(region, list);
			}
			
		}
		return;
	}
	
	/**
	 * Returns all values for which the queried region intersects
	 * @param region
	 * @return
	 */
	public LinkedList<V> queryCollisions(Rectangle region)
	{
		LinkedList<V> list = new LinkedList<>();
		this.queryCollisions(region, list);
		return list;
	}
	
	private void queryCollisions(Rectangle region, LinkedList<V> list)
	{
		if (this.bounds.contains(region))
		{
			synchronized (leafs) 
			{
				for (Node<V> node: leafs) 
				{
					if (region.contains(node.bounds)) list.add(node.value);
				}	
			}
			
			if (divided)
			{
				northWest.queryCollisions(region, list);
				northEast.queryCollisions(region, list);
				southWest.queryCollisions(region, list);
				southEast.queryCollisions(region, list);
			}
			
		}
		return;
	}
	
	private void queryNodes(Rectangle region, LinkedList<Node<V>> list)
	{
		if (this.bounds.contains(region))
		{
			synchronized (leafs) 
			{
				for (Node<V> node : leafs) 
				{
					list.add(node);
				}
			}
			
			if (divided)
			{
				northWest.queryNodes(region, list);
				northEast.queryNodes(region, list);
				southWest.queryNodes(region, list);
				southEast.queryNodes(region, list);
			}
		}
	}
	
	public void queryRay(Ray ray, LinkedList<V> list)
	{
		if (ray.intersects(bounds) || (bounds.contains(ray.origin()) && bounds.contains(ray.end())))
		{
			for (Node<V> node : leafs) 
			{
				if (ray.intersects(node.bounds)) list.add(node.value);
			}
			if (divided)
			{
				northWest.queryRay(ray, list);
				northEast.queryRay(ray, list);
				southWest.queryRay(ray, list);
				southEast.queryRay(ray, list);
			}
		}
	}
	
	private static class DoubleBox
	{
		double num;
	}
	
	private Vector2 queryClosestHit(Ray ray, DoubleBox distanceSq, Vector2 closest)
	{
		if (ray.intersects(bounds) || (bounds.contains(ray.origin()) && bounds.contains(ray.end())))
		{
			for (Node<V> node : leafs) 
			{
				Vector2 test = ray.closestHit(node.bounds);
				if (test != null) if (ray.origin().distanceToSq(test) < distanceSq.num)
				{
					closest = test;
					distanceSq.num = ray.origin().distanceToSq(test);
				}
				
			}
			if (divided)
			{
				closest = northWest.queryClosestHit(ray, distanceSq, closest);
				closest = northEast.queryClosestHit(ray, distanceSq, closest);
				closest = southWest.queryClosestHit(ray, distanceSq, closest);
				closest = southEast.queryClosestHit(ray, distanceSq, closest);
			}
		}
		return closest;
	}
	
	public void remove(InstanceID<Node<V>> id, boolean cleanup)
	{
		Node<V> node = map.getInstanceFromID(id);
		if (node == null) throw new NoSuchElementException("InstanceID does not point to any node");
		freeNode(node);
		if (node.tree.parent != null && cleanup) node.tree.parent.cleanUp();
	}
	
	private void clean()
	{
		if (sum(0) <= leafCapacity)
		{
			eatChildren();
		}
	}
	
	public void cleanUp()
	{
		this.clean();
		if (divided)
		{
			northWest.cleanUp();
			northEast.cleanUp();
			southWest.cleanUp();
			southEast.cleanUp();
		}
	}
	
	/**
	 * 
	 * @param rect
	 * @param value the value for the rectangle to store
	 * @return the instanceID assigned to the internal node of the quadtree or null if the rectangle does not fit in the quadtree
	 */
	
	public InstanceID<Node<V>> put(Rectangle rect, V value)
	{
		Node<V> node = new Node<>();
		node.value = value;
		node.bounds = rect;
		InstanceID<Node<V>> nodeID = map.newInstanceID(node);
		node.id = nodeID.getID();
		
		if (insert(node)) return nodeID;
		else return null;
	}
	
	/**
	 * Updates the position if the node in the QuadTree, note this should only be called if the node has moved or
	 * the node has been resized
	 * @param id the InstanceID of the entry returned from {@code CRQuadTree.put(Rectangle rect, V value)}
	 * @return The function will return true if the node still fits into the QuadTree, returns false if the node
	 * no longer fits into the QuadTree and is removed from the QuadTree
	 */
	public boolean updateEntry(InstanceID<Node<V>> id)
	{
		Node<V> node = map.getInstanceFromID(id);
		if (node == null) throw new NoSuchElementException("The provided Instance ID does not point to any Node, Node may been removed due leaving QuadTree boundary");
		
		if (node != null)
		{
			node.tree.updateEntry(node);
			return true;
		}
		return false;
	}
	
	/**
	 * Removes the node from the InstanceMap and removes the node the leaf list it is contained in
	 * @param node
	 */
	private void freeNode(Node<V> node) 
	{		
		map.removeID(node.id);
		synchronized (node.myList) 
		{
			node.myList.remove(node);	
		}
	}

	/**
	 * 
	 * @param node
	 */
	private boolean updateEntry(Node<V> node)
	{
		
		if (bounds.fullyContains(node.bounds))
		{	
			//if (node.tree.equals(this)) return true; //same tree nothing changed
			return insert(node); //put the node into the current level, different tree may cause subdivde
		}
		else //no longer fits in current level check
		{
			if (parent != null) //check if the node can be moved into parent tree
			{
				return parent.updateEntry(node);
			}
			return false; //the tree does not have a parent has no parent node no longer fits into QuadTree
		}
	}
	
	/**
	 * Returns a List of the all the values in the QuadTree
	 * @return
	 */
	public LinkedList<V> getAll()
	{
		return this.queryPossible(bounds);
	}
	
	/**
	 * Returns a List of the all the nodes in the QuadTree
	 * @return
	 */
	private LinkedList<Node<V>> getAllNodes()
	{
		LinkedList<Node<V>> list = new LinkedList<Node<V>>();
		queryNodes(bounds,list);
		return list;
	}
	

	@Override
	public Iterator<V> iterator() 
	{
		LinkedList<V> list = getAll();
		return list.iterator();
	}
	
	public void render(Graphics2D g2, Rectangle region)
	{
		if (this.bounds.contains(region))
		{
			g2.setStroke(new BasicStroke(3));
			g2.setColor(Color.black);
			g2.drawRect((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
			
			for (Node<V> node : leafs) {
				drawNode(node, g2);
			}
			
			if (divided)
			{
				northWest.render(g2,region);
				southWest.render(g2,region);
				southEast.render(g2,region);
				northEast.render(g2,region);
			}
		}
		
	}
	
	private void drawNode(Node<V> node, Graphics2D g2)
	{
		Rectangle bounds = node.bounds;
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.red);
		g2.drawRect((int)bounds.getX(), (int)bounds.getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
	}

	public void clear() 
	{
		synchronized (leafs) 
		{
			leafs.clear();
			
		}
		if (divided)
		{
			northEast.clear();
			northWest.clear();
			southEast.clear();
			southWest.clear();
		}
	}

	public Vector2 closetHit(Ray ray) 
	{
		DoubleBox box = new DoubleBox();
		box.num=Double.MAX_VALUE;
		return queryClosestHit(ray, box, null);		
	}

	public boolean isClearLine(Ray ray) 
	{
		if (ray.intersects(bounds) || bounds.contains(ray.origin()) && bounds.contains(ray.end()))
		{
			for (Node<V> node : leafs) {
				if (ray.intersects(node.bounds))
				{
					return false;
				}
				else if (node.bounds.contains(ray.origin()) && node.bounds.contains(ray.end()))
				{
					return false;
				}
			}
			
			if (divided)
			{
				if (!northEast.isClearLine(ray)) return false;
				if (!northWest.isClearLine(ray)) return false;
				if (!southEast.isClearLine(ray)) return false;
				if (!southWest.isClearLine(ray)) return false;
			}
		}
		return true;
	}
	
	
}
