package game.inventory;

import game.entities.item.Item;

/**
 * 
 * @author Cameron
 * Represents an entry inside of an inventory
 */
public class Entry
{
	private int count;
	private Item item;
	
	public Entry(Item item)
	{
		this.item = item;
		this.count = 0;
	}
	
	public Entry(Item item, int count)
	{
		this.item = item;
		this.count = count;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public Item getItem()
	{
		return item;
	}
	
	public int putItems(int count)
	{
		if (this.count + count > item.getStackSize())
		{
			int net = item.getStackSize() - this.count;
			this.count = item.getStackSize();
			return net;
		}
		else
		{
			this.count += count;
			return count;
		}
	}
}