package engine.util.quadtree;

import java.util.LinkedList;
import java.util.Queue;

import physics.collision.Rectangle;
import physics.general.Vector2;

public class CollisionQuadTree<Value> extends QuadTree<Value>
{

	/*
	 * Extension of quadtree that contains Rectangles instead of points
	 */
	
	private int n = 0;
	public CollisionQuadTree(Rectangle boundary, Vector2 origin) 
	{
		super(boundary, origin);
		boundary.setPosition(origin);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void subdivide()
	{
		double width = boundary.getWidth()/2;
		double height = boundary.getHeight()/2;
		
		double x1 = origin.getX();
		double x2 = x1 + width;
		
		double y1 = origin.getY();
		double y2 = y1 + height;
		
		Vector2 ur = new Vector2(x2,y1);
		Vector2 ul = new Vector2(x1,y1);
		Vector2 lr = new Vector2(x2,y2);
		Vector2 ll = new Vector2(x1,y2);
		
		this.ur = new CollisionQuadTree<Value>(new Rectangle(width, height, ur), ur);
		this.ul = new CollisionQuadTree<Value>(new Rectangle(width, height, ul), ul);
		this.lr = new CollisionQuadTree<Value>(new Rectangle(width, height, lr), lr);
		this.ll = new CollisionQuadTree<Value>(new Rectangle(width, height, ll), ll);
	
		this.isDivided = true;
	}
	
	@Override
	public boolean insert(QuadTreeNode<Value> collisionNode)
	{
		CollisionQuadTree<Value> ur = (CollisionQuadTree<Value>) this.ur;
		CollisionQuadTree<Value> ul = (CollisionQuadTree<Value>) this.ul;
		CollisionQuadTree<Value> ll = (CollisionQuadTree<Value>) this.ll;
		CollisionQuadTree<Value> lr = (CollisionQuadTree<Value>) this.lr;
		
		CollisionNode<Value> node = null;
		if (collisionNode instanceof CollisionNode)
		{
			node = (CollisionNode<Value>) collisionNode;
			
		}
		
		if (node == null) return false;
		
		if (leafs.size() + 1 > LEAF_CAPCITY)
		{
			if (!isDivided)
			{
				subdivide();
				ur = (CollisionQuadTree<Value>) this.ur;
				ul = (CollisionQuadTree<Value>) this.ul;
				ll = (CollisionQuadTree<Value>) this.ll;
				lr = (CollisionQuadTree<Value>) this.lr;
				/*
				 * test if the node can fit in a child tree and if it does insert it into the subtree
				 */
				
				CollisionNode<Value> tempNode = null;
				Queue<QuadTreeNode<Value>> leafQueue = leafs;
				for (int index = 0; index < leafs.size(); index ++) 
				{	
					QuadTreeNode<Value> temp = leafQueue.poll();
					
					tempNode = (CollisionNode<Value>) temp; //this will always be true as we check at the beginning
					if (!tempNode.wasCheckAgainstChild())
					{
						if (ur.getBoundary().fullyContains(tempNode.getBounds()))
						{
							ur.insert(tempNode);
						}
						else if (ul.getBoundary().fullyContains(tempNode.getBounds()))
						{
							ul.insert(tempNode);
						}
						else if (ll.getBoundary().fullyContains(tempNode.getBounds()))
						{
							ll.insert(tempNode);
						}
						else if (lr.getBoundary().fullyContains(tempNode.getBounds()))
						{
							lr.insert(tempNode);
						}
						else
						{
							tempNode.checked(); //Confirm that the node was unsuccesfully inserted into subtree so that this is not rechecked with every insertion
							leafs.add(tempNode);
							continue;
						}
					}
					//last case is that this node can only fit in the current tree so re-add to the leaves
					else leafs.add(tempNode);
				}
			
				
			}
			//check if it can fit into a subNode
			if (ur.getBoundary().fullyContains(node.getBounds())) ((CollisionQuadTree<Value>) ur).insert(node);
			else if (ul.getBoundary().fullyContains(node.getBounds())) ((CollisionQuadTree<Value>) ul).insert(node);
			else if (ll.getBoundary().fullyContains(node.getBounds())) ((CollisionQuadTree<Value>) ll).insert(node);
			else if (lr.getBoundary().fullyContains(node.getBounds())) ((CollisionQuadTree<Value>) lr).insert(node);
			
			//Otherwise this rectangle can only fit in this node so we force insert it
			else
			{
				leafs.add(node);
				node.checked();
			}
			
		}
		else
		{
			leafs.add(node);
		}
		
		return true;
		
	}
	
	@Override
	public LinkedList<QuadTreeNode<Value>> queryRange(Rectangle rect)
	{
		LinkedList<QuadTreeNode<Value>> result = new LinkedList<QuadTreeNode<Value>>();
		if (rect == null) return result;
		if (this.boundary.contains(rect))
		{
			for(QuadTreeNode<Value> node : this.leafs)
			{
				result.add(node);
				
			}
			
			if (isDivided)
			{
				((CollisionQuadTree<Value>) ur).queryRange(rect, result);
				((CollisionQuadTree<Value>) ul).queryRange(rect, result);
				((CollisionQuadTree<Value>) lr).queryRange(rect, result);
				((CollisionQuadTree<Value>) ll).queryRange(rect, result);
			}
		}
		
		return result;
	}
	
	@Override
	protected LinkedList<QuadTreeNode<Value>> queryRange(Rectangle rect, LinkedList<QuadTreeNode<Value>> result)
	{
		if (this.boundary.contains(rect))
		{
			for(QuadTreeNode<Value> node : this.leafs)
			{
				result.add(node);
			}
			
			if (isDivided)
			{
				((CollisionQuadTree<Value>) ur).queryRange(rect, result);
				((CollisionQuadTree<Value>) ul).queryRange(rect, result);
				((CollisionQuadTree<Value>) lr).queryRange(rect, result);
				((CollisionQuadTree<Value>) ll).queryRange(rect, result);
			}
		}
		
		return result;
	}
	
}
