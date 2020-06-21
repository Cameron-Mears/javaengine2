package engine.core.tick;

import java.util.LinkedList;
import java.util.Queue;

/*
 * Class used for other objects which should only tick condintionaly
 */
public class TickSchedular 
{
	private Queue<Tickable> queue;
	
	private TickSchedular()
	{
		queue = new LinkedList<Tickable>();
	}
}
