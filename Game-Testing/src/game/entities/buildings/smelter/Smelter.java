package game.entities.buildings.smelter;

import java.awt.Graphics2D;

import game.entities.buildings.Building;
import game.entities.buildings.Traverseable;
import game.entities.item.Item;
import game.entities.item.processing.ItemProcessor;
import game.entities.item.processing.ItemStream;
import physics.collision.Rectangle;

public class Smelter extends Building
{

	private ItemStream input;
	private ItemStream output;
	
	private ItemProcessor processor;
	
	public Smelter()
	{
		
	}
	
	@Override
	public void traverse() 
	{	
		if (input.hasNext())
		{
			Item item = input.peekNext();
			if (processor.testValadity(item))
			{
				item = input.next();
				processor.acceptNext(item);
				
			}
			else
			{
				
			}
		}
		for (Traverseable traverseable : children) 
		{
			traverseable.traverse();
		}
		
		
	}

	@Override
	public void render(Graphics2D g2) 
	{
				
	}

	@Override
	public Rectangle renderBoundingArea() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
