package game.entities.item;

import java.util.ArrayList;

public class Recipe 
{
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Item> items;
	
	public static class Ingredient
	{
		private Item item;
		public Item getItem() 
		{
			return item;
		}

		public int getCount() {
			return count;
		}

		private int count;
		
		public Ingredient(Item item, int count)
		{
			this.item = item;
			this.count = count;
		}
	}
	
	public ArrayList<Item> getItems()
	{
		return items;
	}
	
	public ArrayList<Ingredient> getIngredients()
	{
		return ingredients;
	}
	
	public boolean testValadity(Item item)
	{
		return ingredients.contains(item);
	}
	
	public int getNItems()
	{
		return ingredients.size();
	}
}
