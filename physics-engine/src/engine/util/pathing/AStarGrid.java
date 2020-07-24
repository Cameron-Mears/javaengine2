package engine.util.pathing;

import java.util.ArrayList;

public class AStarGrid 
{
	private ArrayList<ArrayList<Node>> grid;
	
	private int width, height;
	
	public AStarGrid(int width, int height, boolean allowDiagonal)
	{
		this.width = width;
		this.height = height;
		
		grid = new ArrayList<ArrayList<Node>>(height);
		for (ArrayList<Node> arrayList : grid) 
		{
			arrayList = new ArrayList<Node>(width);
		}
	}
	
	public AStarSolver createSolver()
	{
		return null;
	}
	
	public boolean allowDiagonal()
	{
		return false;
	}
	
	public Node at(int x, int y)
	{
		return grid.get(y).get(x);
	}

	public int width() 
	{
		return width;
	}
	
	public int height() 
	{
		return height;
	}
	
}
