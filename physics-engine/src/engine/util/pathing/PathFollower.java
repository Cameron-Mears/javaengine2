package engine.util.pathing;

import engine.core.Engine;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import graphics.layer.GraphicsLayerManager;
import physics.general.Vector2;

public class PathFollower implements Tickable
{
	private Path path;
	PathNode current;
	PathNode next;
	private Vector2 position;
	private double velocity;
	private EndOfPathListener listener;
	private double yDir, xDir;
	
	private boolean complete;
	
	public PathFollower(double velocity, EndOfPathListener listener)
	{
		this.velocity = velocity;
		this.listener = listener;
	}
	
	public void join(Path path, Vector2 position)
	{
		this.path = path;
		this.position = position;
		if (path.size() == 0)
		{
			complete = true;
			return;
		}
		current = path.head();
		if (current.next() == null)
		{
			complete = true;
			return;
		}
		next = current.next();
		int direction = (int)current.directionToNext();
		if (direction == 0) xDir = 1;
		if (direction == 180) xDir = -1;
		if (direction == 90) yDir = -1;
		if (direction == 270) yDir = 1;
	}
	
	@Override
	public void onTick(TickInfo info) 
	{
		if (!complete)
		{
			position.x += velocity * xDir * info.delta;
			position.y += velocity * yDir * info.delta;
			
			if (reachedNext())
			{
				setUpNext();
			}
		}
		else listener.onceAtEnd(this);
	}
	
	private void setUpNext()
	{
		current = next;
		if (current.hasNext())
		{
			
			next = current.next();
		}
		else
		{
			complete = true;
			return;
		}
		int direction = (int)current.directionToNext();
		if (direction == 0) xDir = 1;
		if (direction == 180) xDir = -1;
		if (direction == 90) yDir = -1;
		if (direction == 270) yDir = 1;
	}
	
	
	private boolean reachedNext()
	{
		Vector2 nodePos = next.getPosition();
		if (nodePos.y == position.y) //moving along x
		{
			if (xDir == 1) //moving right
			{
				if (position.x >= nodePos.x)
				{
					xDir = 0;
					position.x = nodePos.x; //snap to grid
					return true;
				}
			}
			else
			{
				if (position.x <= nodePos.x)
				{
					xDir = 0;
					position.x = nodePos.x;
					return true;
				}
			}
		}
		else
		{
			if (yDir == 1) //moving down
			{
				if (position.y >= nodePos.y)
				{
					yDir = 0;
					position.y = nodePos.y; //snap to grid
					return true;
				}
			}
			else
			{
				if (position.y <= nodePos.y)
				{
					yDir = 0;
					position.y = nodePos.y;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean finished()
	{
		return complete;
	}
}
