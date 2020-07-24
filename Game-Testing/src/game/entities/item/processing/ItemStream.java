package game.entities.item.processing;

import java.util.LinkedList;

import game.entities.item.Item;

public class ItemStream 
{
	/**
	 * 
	 * <p> represents the origin of the item, for instance the input of a smelter cannot connected to the input of another smelter
	 * outputs should only to inputs</p>
	 * @author Cameron
	 *
	 */
	public static enum ORIGIN
	{
		INPUT,
		OUTPUT
	}
	
	private ORIGIN origin;
	private int capacity;
	private double rate;
	private int held;
	private LinkedList<Item> path;
	
	
	
	
	public boolean hasNext()
	{
		return held > 0;
	}
	
	public Item next()
	{
		held--;		
		return null;
	}
	
	public Item peekNext()
	{
		return null;
	}

}
