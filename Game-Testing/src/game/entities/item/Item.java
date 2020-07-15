package game.entities.item;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import engine.core.Engine;
import external.org.json.JSONArray;
import external.org.json.JSONObject;

public class Item 
{
	private int id;
	private int stackSize;
	private BufferedImage img;
	private String name;
	private ArrayList<Recipe> recipes;
	
	private Item()
	{
		
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Item)) return false;
		return ((Item)o).getID() == id;
	}
	
	public int getID()
	{
		return id;
	}
	
	public int getStackSize()
	{
		return stackSize;
	}
	
	private static HashMap<String, Item> itemMap;

	//Item Loader
	static
	{
		JSONObject itemList = Engine.getGameFiles().get("item_list.json");
		JSONArray items = itemList.getJSONArray("items");
		int nItems = items.length();
		itemMap = new HashMap<String, Item>(nItems);
		
		for (int index = 0; index < nItems; index ++)
		{
			JSONObject item = items.getJSONObject(index);
			int id = item.getInt("id");
			String name = item.getString("name");
			String imgPath = item.getString("img");
		}
	}
	
}
