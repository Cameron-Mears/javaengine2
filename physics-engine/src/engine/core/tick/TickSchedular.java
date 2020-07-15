package engine.core.tick;

import java.util.LinkedList;
import java.util.Queue;

/*
 * Class used for other objects which should only tick condintionaly
 */
public class TickSchedular 
{
	private static class Node
	{
		Tickable tickable;
		double delta;
		
		Node(Tickable tickable, double delta)
		{
			this.tickable = tickable;
			this.delta = delta;
		}
	}
	
	private TickSchedular()
	{
		
	}
	
	public Node schedule(Tickable t, long delta)
	{
		return null;
	}
	
	public Node addPeriodic(Tickable t, long interval)
	{
		return null;
	}
	
	
}
