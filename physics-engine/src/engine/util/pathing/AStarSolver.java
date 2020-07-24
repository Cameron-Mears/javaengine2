package engine.util.pathing;

import java.util.ArrayList;
import java.util.HashMap;

public class AStarSolver
{
	private AStarGrid grid;
	
	private Node start, end;
	
	private ArrayList<Node> openlist;
	private ArrayList<Node> closedList;
	
	private HashMap<Node, Path> pathcache; 
	
	AStarSolver(AStarGrid grid, Node start, Node end)
	{
		this.pathcache = new HashMap<Node, Path>();
		this.start = start;
		this.end = end;
	}
	
	public void clearCaches(AStarGrid grid)
	{
		pathcache.clear();
		this.grid = grid;
	}
	
	public Path slove()
	{
		Path path = pathcache.get(start);
		if (path != null) return path;
		
		path = new Path();
		if (start == null || end == null) return path;
		if (start.equals(end))
		{
			path.append(end);
			return path;
		}
		this.openlist = new ArrayList<Node>();
		this.closedList = new ArrayList<Node>();
		
		pathcache.put(start, path);
		return null;
	}

	
	void checkPaths()
	{
		
	}
}
