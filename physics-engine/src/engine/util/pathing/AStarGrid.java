package engine.util.pathing;

import java.util.ArrayList;

public class AStarGrid 
{
	private ArrayList<ArrayList<ASNode>> grid;
	
	private int width, height;
	
	public AStarGrid(int width, int height, boolean allowDiagonal)
	{
		this.width = width;
		this.height = height;
		
		grid = new ArrayList<ArrayList<ASNode>>(height);
		for (ArrayList<ASNode> arrayList : grid) 
		{
			arrayList = new ArrayList<ASNode>(width);
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
	
	public ASNode at(int x, int y)
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
