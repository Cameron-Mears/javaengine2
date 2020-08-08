package engine.core.tick;

import java.util.LinkedList;
import java.util.Queue;

import engine.core.exceptions.EngineException;
import engine.core.instance.InstanceID;
import engine.core.instance.InstanceMap;
import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;

/*
 * Class used for other objects which should only tick condintionaly
 */
public class TickScheduler 
{
	private static TickScheduler instance;
	public static synchronized TickScheduler getInstance()
	{
		if (instance == null) instance = new TickScheduler();
		return instance;
	}
	
	private InstanceMap<Node> nodeMap;
	private HashTreeMap<InstanceID<Node>, Node> nodes;
	private TraverseFunction<Node> traverseFunction;
	
	private TickInfo myInfo;
	
	private TickInfo info;
	
	public static class Node
	{
		InstanceID<Node> id;
		Tickable tickable;
		double period;
		double timeSum;
		
		long scheduledTime;
		
		boolean isPaused;
		boolean isPeriodic;
		
		Node(Tickable tickable, double period, TickScheduler ts, boolean isPeriodic)
		{
			this.tickable = tickable;
			this.period = period;
			this.scheduledTime = System.nanoTime();
			this.isPeriodic = isPeriodic;
			this.id = ts.nodeMap.newInstanceID(this);
			ts.nodes.put(id, this);
		}
	}
	
	private TickScheduler()
	{
		info = new TickInfo();
		info.groupName = "TickScheduler";
		
		nodeMap = new InstanceMap<>();
		nodes = new HashTreeMap<>();
		traverseFunction = traverseFunction();
	}
	
	void update(TickInfo info)
	{
		myInfo = info;
		nodes.inOrderTraverse(traverseFunction);
	}
	
	TraverseFunction<Node> traverseFunction()
	{
		return new TraverseFunction<Node>() 
		{			
			@Override
			public void apply(Node node) 
			{
				if (node.isPaused) return;
				node.timeSum += myInfo.deltaNS;
				if (node.timeSum >= node.period)
				{
					if (node.isPeriodic)
					{
						node.timeSum -= node.period;
					}
					else
					{
						nodes.put(node.id, null);
					}
					info.deltaNS = System.nanoTime() - node.scheduledTime;
					node.scheduledTime = System.nanoTime();
					info.delta = ((double)info.deltaNS)/1E9D;
					node.tickable.onTick(info);
				}
			}
		};
	}
	
	public void pause(InstanceID<Node> nodeID)
	{
		Node node = nodeMap.getInstanceFromID(nodeID);
		if (node == null) throw new EngineException("nodeID is invalid, or the tickaable as alreay run");
		node.isPaused = true;
	}
	
	public void resume(InstanceID<Node> nodeID)
	{
		Node node = nodeMap.getInstanceFromID(nodeID);
		if (node == null) throw new EngineException("nodeID is invalid, or the tickaable as alreay run");
		node.isPaused = false;
	}
	
	public InstanceID<Node> schedule(Tickable t, long delta)
	{
		if (t == null || delta < 0) throw new IllegalArgumentException();
		Node node = new Node(t, delta, this, false);
		return node.id;
	}
	
	public InstanceID<Node> addPeriodic(Tickable t, long interval)
	{
		if (t == null || interval < 0) throw new IllegalArgumentException();
		Node node = new Node(t, interval, this, true);
		return node.id;	
	}
	
	
}
