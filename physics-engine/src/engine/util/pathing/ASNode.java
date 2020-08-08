package engine.util.pathing;

import java.util.LinkedList;

import physics.general.Vector2;

public class ASNode 
{
	private Vector2 position;
	private ASNode parent;
	private LinkedList<ASNode> neighbours;
	private double h = 0, g = 0;
	
	private boolean isTraverseable;
	
	public ASNode(Vector2 position, AStarGrid grid)
	{
		this.position = position;
		this.neighbours = new LinkedList<ASNode>();
	}
	
	public double getH()
	{
		return h;
	}
	
	public double getF()
	{
		return g+h;
	}
	
	public void getNeighbours()
	{
	
	}
	
	private void queryNeighbours(AStarGrid grid)
	{
		for (int offset = -1; offset < 1; offset++) 
		{
			int x = (int) position.getX();
			int y = (int) position.getY();
			ASNode node = null;
			if (grid.allowDiagonal())
			{
				for (int dOffset = -1; dOffset < 1; dOffset++) 
				{
					node = getNeighbour(x+offset, y+dOffset, grid);
				}
			}
			else
			{
				if (offset == 0)
				{
					node = getNeighbour(x, y-1, grid);
					node = getNeighbour(x, y+1, grid);
				}
				node = getNeighbour(x+offset, y, grid);
			}
			
			if (node != null)
			{
				node.setParent(this);
				neighbours.add(node);
			}
		}
	}
	
	private void setParent(ASNode node) 
	{
		parent = node;		
	}
	
	public ASNode parent()
	{
		return parent;
	}

	private ASNode getNeighbour(int x, int y, AStarGrid grid)
	{
		if (x > 0 && x < grid.width() && y > 0 && y < grid.height())
		{
			return grid.at(x, y);
		}
		return null;
	}
}
