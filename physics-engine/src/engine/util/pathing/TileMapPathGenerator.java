package engine.util.pathing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import engine.core.exceptions.EngineException;
import engine.util.pathing.PathNode.Mode;
import graphics.tilemap.TileMap;
import physics.general.Vector2;

public class TileMapPathGenerator 
{
	private static EngineException NO_PATH = new EngineException("No path");
	
	private TileMap tm;
	private int[] tiles;
	private int[][] tileMapData;
	private PathNode[][] nodeLocations;
	private Queue<Vector2> nodeQueue;
	private Mode junctionMode;
	private HashMap<Integer, Boolean> validTiles;
	
	
	private static class Solver
	{
		public PathNode prev;
		public PathNode focus;
	}
	
	public TileMapPathGenerator(TileMap tm, Mode junctionMode, int ...tilesToUse)
	{
		this.junctionMode = junctionMode;
		this.tm = tm;
		this.tileMapData = tm.getData();
		this.nodeLocations = new PathNode[tileMapData.length][tileMapData[0].length];
		tiles = tilesToUse;
		validTiles = new HashMap<>();
		nodeQueue = new LinkedList<>();
		for (int i : tilesToUse) 
		{
			validTiles.put(i, true);
		}
	}
	
	public Path createPath(Vector2 start, Vector2 end)
	{
		int xs = (int)start.getX();
		int ys = (int)start.getY();
		int xe = (int)end.getX();
		int ye = (int)end.getY();
		if (!checkIndex(xs, ys) || !checkIndex(xe, ye)) throw new IndexOutOfBoundsException();
		Path path = new Path();
		
		Solver solver = new Solver();
		
		int tileCode = tileMapData[ys][xs];
		int x = xs;
		int y = ys;
		if (isValidTile(at(x,y)))
		{
			PathNode pathHead = new PathNode(new Vector2(x,y), junctionMode);
			solver.focus = pathHead;
			path.append(pathHead);
		}
		exploreNeighbours(x, y, solver);
		do
		{
			if (nodeQueue.size() == 0) //only one neighbour (previous node), check to make sure it is not the start
			{
				
				if (end.equals(solver.focus.getPosition()))
				{
					System.out.println("very cool");
					break;
				}
				throw new EngineException("No path");				
				
			}
			
			if (nodeQueue.size() > 1) junctionNode(path, end, solver);
			else normalNode(path,solver);
			x = (int)solver.focus.getPosition().getX();
			y = (int)solver.focus.getPosition().getY();
			tileCode = at(x,y);
			exploreNeighbours(x, y, solver);
		} while (isValidTile(tileCode) && checkForExisting(x, y) != null);
		
		return path;
	}
	
	
	public Path solve(Vector2 start, Vector2 end)
	{
		int x, y;
		
		x = (int) start.x;
		y = (int) start.y;
		System.out.println(x);
		System.out.println(y);
		if (!checkIndex(x, y) || !checkIndex((int)end.x, (int)end.y))
		{
			throw new EngineException("Start or end point out of bounds");
		}
		if (!isValidTile(at(x,y))) throw new EngineException("Path starts on a invalid tile code");
		Solver solver = new Solver();
		Path path = new Path();
		PathNode head = new PathNode(start, junctionMode);
		putNode(head);
		path.append(head);
		solver.prev = head;
		do
		{
			LinkedList<Vector2> neighbours = queryEmptyNeigbours(x, y);
			if (neighbours.size() > 1)
			{
				PathNode junction = solver.prev;
				junction.setJunction(true);
				
			}
			else
			{
				if (neighbours.size() == 0) //no free neighbour
				{
					PathNode pn = nodeAt(x,y);
					if (pn != null)
					{
						LinkedList<PathNode> junctions = getJunctionNeighbours(x, y);
						
					}
					else throw new EngineException("No path.");
				}
				Vector2 pos = neighbours.poll();
				PathNode pn = new PathNode(pos, junctionMode);
				solver.prev.setNext(pn);
				pn.setPrev(solver.prev);
				solver.prev = pn;
				putNode(pn);
				path.append(pn);
			}
			
			x = (int)solver.prev.getPosition().x;
			y = (int)solver.prev.getPosition().y;
		} while (!end.equals(x,y));
		
		return path;
	}
	
	private LinkedList<PathNode> getJunctionNeighbours(int x, int y)
	{
		LinkedList<PathNode> ret = new LinkedList<>();
		
		if (nodeAt(x+1, y) != null) ret.add(nodeAt(x+1, y));
		if (nodeAt(x-1, y) != null) ret.add(nodeAt(x-1, y));
		if (nodeAt(x, y+1) != null) ret.add(nodeAt(x, y+1));
		if (nodeAt(x, y-1) != null) ret.add(nodeAt(x, y-1));
		
		for (PathNode pathNode : ret) 
		{
			if (!pathNode.isJunction())
			{
				ret.remove(pathNode);
			}
		}
		return ret;
	}
	
	private void putNode(PathNode pn)
	{
		int x = (int)pn.getPosition().x, y = (int)pn.getPosition().y;
		nodeLocations[y][x] = pn;
	}
	
	
	private void normalNode(Path path, Solver solver)
	{
		while (!nodeQueue.isEmpty())
		{
			Vector2 pos = nodeQueue.poll();
			
			if (solver.prev != null)
			{
				if (pos.equals(solver.focus.getPosition())) continue;
			}
			int x = (int)pos.getX();
			int y = (int)pos.getY();
			
			PathNode pn = checkForExisting(x, y);
			if (pn != null)
			{
				if (pn.isJunction())
				{
					path.flip();
					pn.addNext(path.head());
					path.tail().setMode(Mode.MERGE);
				}
			}
			
			pn = new PathNode(pos,junctionMode);
			solver.focus.setNext(pn);
			pn.setPrev(solver.prev);
			solver.prev = solver.focus;
			solver.focus = pn;
			path.append(pn);
			nodeLocations[y][x] = pn;
		}
	}
	
	private void junctionNode(Path path, Vector2 end, Solver solver)
	{
		solver.focus.setJunction(true);
		LinkedList<PathNode> junctionNodes = new LinkedList<>();
		while (!nodeQueue.isEmpty())
		{
			Vector2 pos = nodeQueue.poll();
			
			if (solver.prev != null)
				if (pos.equals(solver.prev.getPosition())) continue;
			
			PathNode pn = new PathNode(pos,junctionMode);
			pn.setPrev(solver.focus);
			junctionNodes.add(pn);
		}
		LinkedList<PathNode> passedNodes = new LinkedList<>();
		int index = 0;
		for (index = 0; index < junctionNodes.size(); index++)
		{
			PathNode pathNode = junctionNodes.get(index);
			Vector2 pos = pathNode.getPosition();
			int x = (int) pos.getX();
			int y = (int) pos.getY();
			Path pFromJunction = null;
			if (pos.equals(14,2))
			{
				toString();
			}
			if (solver.prev != null)
			{
				if (pos.equals(solver.prev.getPosition()) || checkForExisting(x,y) != null) continue;
			}
			try
			{
				 
				 nodeLocations[y][x] = pathNode;
				 pFromJunction = createPath(pathNode.getPosition(), end);
			}
			catch (EngineException e)
			{
				pFromJunction = null;

			}
			if (pFromJunction == null)  continue;
			pathNode.setNext(pFromJunction.getPathNodes().peek());
			passedNodes.add(pathNode);
		}
		PathNode[] arr = new PathNode[passedNodes.size()];
		for (index = 0; index < passedNodes.size(); index ++)
		{
			arr[index] = passedNodes.get(index);
		}
		if (arr.length != 0) solver.focus.setNext(arr);
	}
	
	private boolean isJunctionCanidate(int x, int y)
	{
		return queryEmptyNeigbours(x, y).size() > 1;
	}
	
	private LinkedList<Vector2> queryEmptyNeigbours(int x, int y)
	{
		LinkedList<Vector2> ret = new LinkedList<>();
		if (isValidNeighbour(x+1, y)) ret.add(new Vector2(x+1,y));
		if (isValidNeighbour(x-1, y)) ret.add(new Vector2(x-1,y));
		if (isValidNeighbour(x, y+1)) ret.add(new Vector2(x,y+1));
		if (isValidNeighbour(x, y-1)) ret.add(new Vector2(x,y-1));
		return ret;
	}
	
	private boolean isValidNeighbour(int x, int y)
	{
		if (checkIndex(x, y))
		{
			return isValidTile(at(x,y)) && (nodeAt(x,y) == null);
		}
		return false;
	}
	
	private PathNode nodeAt(int x, int y)
	{
		return nodeLocations[y][x];
	}
	
	private PathNode checkForExisting(int x, int y)
	{
		return nodeLocations[y][x];
	}
	
	private void exploreNeighbours(int x, int y, Solver solver)
	{
		checkNeighbour(x-1, y, solver);
		checkNeighbour(x+1, y, solver);
		checkNeighbour(x, y+1, solver);
		checkNeighbour(x, y-1, solver);
	}
	
	private void checkNeighbour(int x, int y, Solver solver)
	{
		if (checkIndex(x, y))
		{
			if (isValidTile(at(x, y)))
			{
				if (solver.prev != null)
				{
					if (solver.prev.getPosition().equals(x, y)) return;
				}
				nodeQueue.add(new Vector2(x,y));
			}
		}
	}
	
	private int at(int x, int y)
	{
		return tileMapData[y][x];
	}
	
	private boolean isValidTile(int tileCode)
	{
		return validTiles.get(tileCode) == null ? false:true;
	}
	
	private boolean checkIndex(int x, int y)
	{
		if (y < 0 || x < 0) return false;
		if (y > tileMapData.length-1) return false;
		if (x > tileMapData[0].length-1) return false;
		return true;
	}
}
