package engine.util.pathing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Iterator;
import java.util.LinkedList;

public class Path implements Iterable<PathNode>, Iterator<PathNode>
{
	private LinkedList<PathNode> path;
	private PathNode current;
	private boolean goesToEnd;
	
	void setReachesEnd(boolean b)
	{
		goesToEnd = b;
	}
	
	boolean reachesEnd()
	{
		return goesToEnd;
	}
	public Path()
	{
		path = new LinkedList<PathNode>();
	}
	
	@Override
	public String toString()
	{
		return "[size=" + Integer.toString(path.size()) + "][head=" + head().toString();
	}
	
	@Override
	public Iterator<PathNode> iterator() 
	{
		current = path.peek();
		return this;
	}
	
	public void joinFollower(PathFollower follower)
	{
		follower.join(this, path.peek().getPosition());
	}
	
	public void append(PathNode node)
	{
		path.add(node);
	}
	
	public int size()
	{
		return path.size();
	}

	@Override
	public boolean hasNext() 
	{
		return current != null;
	}
	
	public void scale(double factor)
	{
		for (PathNode pathNode : path) 
		{
			pathNode.getPosition().x *= 32;
			pathNode.getPosition().y *= 32;
		}
	}

	@Override
	public PathNode next() 
	{
		PathNode temp = current;
		current = current.next();
		return temp;
	}
	
	LinkedList<PathNode> getPathNodes()
	{
		return path;
	}
	
	public void flip()
	{
		if (path.size() <= 1) return;
		LinkedList<PathNode> newOrder = new LinkedList<>();
		PathNode prev = path.pop();
		if (prev == null) return;
		prev.clear();
		while (!path.isEmpty())
		{
			PathNode tempNode = path.pop();
			prev.setPrev(tempNode);
			tempNode.setNext(tempNode);
			newOrder.add(prev);
			prev = tempNode;
		}
		path = newOrder;
	}

	public void drawPath(Color c, Stroke s, Graphics2D g2)
	{
		PathNode pn = path.peek();
		g2.setColor(c);
		g2.setStroke(s);
		Iterator<PathNode> iter = this.iterator();
		if (iter.hasNext()) iter.next();
		while (iter.hasNext())
		{
			PathNode cn = iter.next();
			if (cn.isJunction()) g2.setColor(Color.black);
			g2.drawLine((int)pn.getPosition().getX()*32, (int)pn.getPosition().getY() * 32, (int)cn.getPosition().getX() * 32, (int)cn.getPosition().getY() * 32);
			pn = cn;
		}
	}
	
	public PathNode head()
	{
		return path.peek();
	}
	
	public PathNode tail()
	{
		return path.peekLast();
	}
}
