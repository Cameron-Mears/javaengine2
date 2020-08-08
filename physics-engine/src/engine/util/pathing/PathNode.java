package engine.util.pathing;

import engine.core.random.Rand;
import physics.general.Vector2;

public class PathNode
{
	private Vector2 position;
	private boolean isJunction = false;
	private PathNode[] paths; //if juncition splits to muptiple
	
	private PathNode[] previous; //if juncition splits to muptiple
	
	PathNode next;
	PathNode prev;
	private int juntionSplitIndex;
	private Mode mode;
	private boolean isMergeNode;
	private boolean[] pressimbleSplitNodes;
	private boolean[] pressimbleMergeNodes;
	private int nNextNodes;
	private int nPrevNodes;
	
	
	@Override
	public boolean equals(Object anObject)
	{
		if (anObject instanceof PathNode)
		{
			return ((PathNode) anObject).position.equals(this.position);
		}
		return false;
	}
	
	public void lock()
	{
		
	}
	
	@Override
	public String toString()
	{
		return position.toString();
	}
	
	public PathNode(Vector2 position, Mode mode)
	{
		this.pressimbleSplitNodes = new boolean[3];
		this.paths = new PathNode[3];
		this.previous = new PathNode[3];
		this.position = position;
		this.mode = mode;
	}
	
	public PathNode(Vector2 position, Mode mode, PathNode prev, PathNode ...nextNodes)
	{
		this(position, mode);
		setNodes(prev,nextNodes);
	}
	
	void addNext(PathNode node)
	{
		if (nNextNodes + 1 < 3)
		{
			paths[nNextNodes] = node;
			nNextNodes++;
			node.setPrev(this);
		}
	}
	
	void setPrevs(PathNode...prevs)
	{
		isJunction = prevs.length > 1;
		nPrevNodes =  prevs.length;
		for (int index = 0; index < 3; index++)
		{
			pressimbleSplitNodes[index] = true;
		}
		System.arraycopy(prevs, 0, paths, 0, prevs.length);
	}
	
	void clear()
	{
		for (int index = 0; index < paths.length; index++)
		{
			pressimbleSplitNodes[index] = true;
			paths[index] = null;
		}
	}
	
	void setAsMergeNode(PathNode node)
	{
		for (int index = 0; index < paths.length; index++) 
		{
			PathNode other = paths[index];
			if (other != null)
			{
				if (other.equals(node))
				{
					pressimbleSplitNodes[index] = false;
				}
			}
		}
	}
	
	PathNode[] getNextNodes()
	{
		return paths;
	}
	
	void setPrev(PathNode prev)
	{
		this.prev = prev;
	}
	
	void setNext(PathNode ... nextNodes)
	{
		isJunction = nextNodes.length > 1;
		nNextNodes =  nextNodes.length;
		for (int index = 0; index < 3; index++)
		{
			pressimbleSplitNodes[index] = true;
		}
		System.arraycopy(nextNodes, 0, paths, 0, nextNodes.length);
	}
	
	void setMode(Mode mode)
	{
		this.mode = mode;
	}
	
	public boolean isJunction()
	{
		return isJunction;
	}
	
	void setNodes(PathNode prev, PathNode ... nextNodes)
	{
		setPrev(prev);
		setNext(nextNodes);
	}
	
	public static enum Mode
	{
		SPLIT,
		RANDOM,
		MERGE
	}
	
	public Vector2 getPosition()
	{
		return position;
	}
	
	public PathNode prev()
	{
		return prev;
	}
	
	public boolean hasNext()
	{
		return next() != null;
	}
	
	public PathNode next()
	{
		if (isJunction)
		{
			if (mode == Mode.SPLIT)
			{	
				do
				{
					juntionSplitIndex = (++juntionSplitIndex > paths.length-1)? 0:juntionSplitIndex;
				} while (!isPressimbleNode(juntionSplitIndex));
				return paths[juntionSplitIndex];
			}
			else
			{
				int index = 0;
				do
				{
					index = Rand.randInt(0,paths.length);
				} while (!isPressimbleNode(index));
				return paths[index];
			}
			
		}
		
		return paths == null? null:paths[0];
	}
	
	private boolean isPressimbleNode(int index)
	{
		return pressimbleSplitNodes[index];
	}
	
	public void setSplitEven()
	{
		mode = Mode.SPLIT;
	}
	
	public void setSplitRandom()
	{
		mode = Mode.RANDOM;
	}

	public void setJunction(boolean b) 
	{
		this.isJunction = b;
		
	}

	public double directionToNext()
	{
		if (!hasNext()) return -1;
		return Math.toDegrees(position.angleTo(next().position));
	}
}
