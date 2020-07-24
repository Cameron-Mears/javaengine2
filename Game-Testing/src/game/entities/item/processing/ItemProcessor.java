package game.entities.item.processing;

import java.util.ArrayList;

import game.entities.item.Item;
import game.entities.item.Recipe;
import game.entities.item.Recipe.Ingredient;

public class ItemProcessor 
{
	public static final int OK = 0;
	
	/**
	 * The item is not included in the Recipe
	 */
	public static final int ITEM_INCOMPATIBLE = -1;

	/**
	 * The itemCount for the item is full
	 */
	public static final int FULL = -2;
	/**
	 * There are not enough items to complete the desired operation
	 */
	public static final int NOT_ENOUGH_ITEMS = -3;
	/**
	 * If a number for add parts or remove parts is a negative number
	 */
	public static final int INVALID_NUMBER = -4;
	private Recipe recipe;
	
	/**
	 * if the method <code> hasAllItems() </code> 
	 */
	private boolean hasAllItems;
	private ArrayList<Integer> itemCount;
	
	public ItemProcessor(Recipe recipe)
	{
		this.recipe = recipe;
		this.hasAllItems = false;
		this.itemCount = new ArrayList<Integer>();
		
		for (Item item : recipe.getItems()) 
		{
			itemCount.add(item.getID(), 0); //declare the used indexes of the array list
		}
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	public int acceptNext(Item item)
	{
		int ret = addParts(item, 1);
		return ret == 1? OK:ret;
	}
	
	/**
	 * 
	 * @return if the ItemProcessor has all the items for the recipe
	 */
	public boolean hasAllItems()
	{
		if (hasAllItems) return true;
		for (Ingredient ingredient : recipe.getIngredients()) 
		{
			Item item = ingredient.getItem();
			int count = ingredient.getCount();
			if (itemCount.get(item.getID()) <= count)
			{
				hasAllItems = false;
				return false;
			}
		}
		hasAllItems = true;
		return true;
	}
	
	/**
	 * subtracts the cost of recipe from the <code>itemCount</code> list
	 * @return exit code the function 
	 */
	public int subtractRecipe()
	{
		if (hasAllItems())
		{
			for (Ingredient ingredient : recipe.getIngredients()) 
			{
				int ret = removeParts(ingredient.getItem(), ingredient.getCount());
				if (ret < 0) return ret;
			}
			return OK;
		}
		return NOT_ENOUGH_ITEMS;
	}
	
	/**
	 * Removes parts from the item processor
	 * 
	 * @param item the item to be removed
	 * @param amount the amount to be removed
	 * @return the amount removed, if the the requested amount is more than it currently holds the the amount it holds will be returned
	 * negative value return if an error has occurred
	 */
	public int removeParts(Item item, int amount)
	{
		if (recipe.testValadity(item))
		{
			if (amount < 0) return INVALID_NUMBER;
			hasAllItems = false; //parts have been removed this may no longer be true
			int count = itemCount.get(item.getID());
			int sum = amount - count;
			if (sum < 0)
			{
				int net = amount;
				itemCount.set(item.getID(), 0);
				return net;
			}
			itemCount.set(item.getID(), sum);
			return amount;
		} else return ITEM_INCOMPATIBLE;
	}
	
	/**
	 * Add parts to the item processor directly with quantity versus singularly with <code> acceptNext(Item item) </code>
	 * @param item  the item to be added
	 * @param amount the amount to be added
	 * @return the amount added to the <code> ItemProcessor </code>, may be less than <code> amount </code> if the sum of the current count and the amount is greater than the item's stack size
	 *  A negative value will be return if a error has occur, see static field declarations for error code descriptions
	 */
	public int addParts(Item item, int amount)
	{
		if (recipe.testValadity(item))
		{
			if (amount < 0) return INVALID_NUMBER;
			int count = itemCount.get(item.getID());
			int sum = amount + count;
			if (sum > item.getStackSize())
			{
				int net = sum - item.getStackSize();
				itemCount.set(item.getID(), item.getStackSize());
				return net;
			}
			itemCount.set(item.getID(), sum);
			return amount;
		} else return ITEM_INCOMPATIBLE;
	}

	public boolean testValadity(Item item) {
		// TODO Auto-generated method stub
		return false;
	}
}
