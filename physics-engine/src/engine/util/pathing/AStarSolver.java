package engine.util.pathing;

import java.util.ArrayList;
import java.util.HashMap;

public class AStarSolver
{
	private AStarGrid grid;
	
	private PathNode start, end;
	
	private ArrayList<PathNode> openlist;
	private ArrayList<PathNode> closedList;
	
	private HashMap<PathNode, Path> pathcache; 
	
	AStarSolver(AStarGrid grid, PathNode start, PathNode end)
	{
		this.pathcache = new HashMap<PathNode, Path>();
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
		this.openlist = new ArrayList<PathNode>();
		this.closedList = new ArrayList<PathNode>();
		
		pathcache.put(start, path);
		return null;
	}

	
	void checkPaths()
	{
		
	}
}
