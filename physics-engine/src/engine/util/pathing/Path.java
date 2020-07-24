package engine.util.pathing;

import java.util.Iterator;
import java.util.LinkedList;

public class Path implements Iterable<Node>
{
	private LinkedList<Node> path;
	
	public Path()
	{
		path = new LinkedList<Node>();
	}
	
	@Override
	public Iterator<Node> iterator() 
	{
		return path.iterator();
	}
	
	public void append(Node node)
	{
		path.add(node);
	}
}
