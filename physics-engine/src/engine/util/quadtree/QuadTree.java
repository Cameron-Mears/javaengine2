package engine.util.quadtree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;

import engine.core.Engine;
import graphics.instance.IGraphics;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class QuadTree<Value> implements IGraphics
{
	
	private static int LEAF_CAPCITY = 200;
	
	
	private ArrayList<QuadTreeNode<Value>> leafs;
	
	private QuadTree<Value> ur;
	private QuadTree<Value> ul;
	private QuadTree<Value> lr;
	private QuadTree<Value> ll;
	
	
	public static int levels = 0;
	
	private Rectangle boundary;
	private Vector2 origin;
	
	boolean isDivided;
	
	public QuadTree(Rectangle boundary, Vector2 origin)
	{
		this.boundary = boundary;
		this.origin = origin;
		this.leafs = new ArrayList<QuadTreeNode<Value>>(LEAF_CAPCITY);
		this.isDivided = false;
	}
	
	
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
		
		this.ur = new QuadTree<Value>(new Rectangle(width, height, ur), ur);
		this.ul = new QuadTree<Value>(new Rectangle(width, height, ul), ul);
		this.lr = new QuadTree<Value>(new Rectangle(width, height, lr), lr);
		this.ll = new QuadTree<Value>(new Rectangle(width, height, ll), ll);
	
		this.isDivided = true;
		levels++;
	}
	
	public Rectangle getBoundary()
	{
		return boundary;
	}
	
	public boolean insert(QuadTreeNode<Value> treeNode)
	{
		if (!boundary.contains(treeNode.getPosition()))
		{
			return false;
		}
		
		if (leafs.size() + 1 > LEAF_CAPCITY || isDivided)
		{
			int duplicates = nDuplicates(treeNode);
			if (duplicates >= LEAF_CAPCITY) //all leafs are duplicates so we just add it anyways subdividing causes recursion error
			{
				leafs.add(treeNode);
				return true;
			}
			
			double rightBoundary = origin.getX() + boundary.getWidth()/2; //determine boundaries of the subtrees
			double bottomBoundary = origin.getY() + boundary.getHeight()/2;
			if (!isDivided)
			{
				subdivide();
				for (QuadTreeNode<Value> node : leafs) //check which subtree into insert nodes in top after subdivide
				{
					boolean isRight = node.getPosition().getX() > rightBoundary;
					boolean isBottom = node.getPosition().getY() > bottomBoundary;
					
					
					
					if (isRight)
					{
						if (isBottom)
						{
							lr.insert(node);
						} else ur.insert(node);
					}
					else
					{
						if (isBottom)
						{
							ll.insert(node);
						} else ul.insert(node);
					}
				}
				leafs.clear();
			}
			
			boolean isRight = treeNode.getPosition().getX() > rightBoundary;
			boolean isBottom = treeNode.getPosition().getY() > bottomBoundary;
			
			if (isRight)
			{
				if (isBottom)
				{
					lr.insert(treeNode);
				} else ur.insert(treeNode);
			}
			else
			{
				if (isBottom)
				{
					ll.insert(treeNode);;
				} else ul.insert(treeNode);
			}
			
		}
		else
		{
			leafs.add(treeNode);
		}
		return true;
	}
	
	private int nDuplicates(QuadTreeNode<Value> node)
	{
		int n = 0;
		for (QuadTreeNode<Value> quadTreeNode : leafs) 
		{
			if (quadTreeNode.equals(node)) n++;
		}
		
		return n;
	}
	
	public ArrayList<QuadTreeNode<Value>> getLeafs()
	{
		return leafs;
	}
	
	public LinkedList<QuadTreeNode<Value>> queryRange(Rectangle rect)
	{
		LinkedList<QuadTreeNode<Value>> result = new LinkedList<QuadTreeNode<Value>>();
		
		queryRange(rect, result);		
		
		return result;
	}
	
	private LinkedList<QuadTreeNode<Value>> queryRange(Rectangle rect, LinkedList<QuadTreeNode<Value>> result)
	{
		
		if (!boundary.contains(rect)) return result;
		
		
		if (isDivided)
		{
			ur.queryRange(rect, result);
			ul.queryRange(rect, result);
			lr.queryRange(rect, result);
			ll.queryRange(rect, result);
		}
		else
		{
			for (QuadTreeNode<Value> quadTreeNode : leafs) 
			{
				result.add(quadTreeNode);
			}
		}
		
		return result;
	}


	@Override
	public void render(Graphics2D g2) 
	{
		g2.setColor(Color.blue);
		try {
			g2.fillRect(0, 0, (int)Engine.getInstance().getProperty("window_width"), (int)Engine.getInstance().getProperty("window_height"));
		} catch (Exception e) {}
		
		this.drawBoundries(g2);
	}
	
	public void drawBoundries(Graphics2D g2)
	{
		g2.setColor(Color.black);
		g2.setStroke(new BasicStroke(5));
		g2.drawRect((int)origin.getX(), (int)origin.getY(), (int)boundary.getWidth(), (int)boundary.getHeight());
		g2.drawRect(23, 43, 100, 200);
		if (!isDivided)g2.drawString(Integer.toString(leafs.size()), (int)(origin.getX() + boundary.getWidth()/2), (int)(origin.getY() + boundary.getHeight()/2));
		
		if (isDivided)
		{
			ur.drawBoundries(g2);
			ul.drawBoundries(g2);
			ll.drawBoundries(g2);
			lr.drawBoundries(g2);
			
		}
	}
	

}
