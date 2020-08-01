package game.entities.item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import engine.core.Engine;
import engine.util.parsers.image.ImageParser;
import external.org.json.JSONArray;
import external.org.json.JSONObject;

public class Item 
{
	public static final int WIDTH = 32, HEIGHT = 32;
	
	private int id;
	private int stackSize;
	private BufferedImage img;
	private String name;
	private ArrayList<Recipe> recipes;
	
	private Item(String name, int id, int stackSize, BufferedImage img)
	{
		this.id = id;
		this.stackSize = stackSize;
		this.name = name;
		this.img = img;
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
	
	public BufferedImage getImage()
	{
		return img;
	}
	
	public String getName()
	{
		return name;
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
			int stackSize = item.getInt("stacksize");
			String name = item.getString("name");
			String imgPath = item.getString("img");
			BufferedImage img = null;
			if (!imgPath.equals("IMG_PATH"))
			{
				try
				{
					img = ImageParser.parseImage(new File(Engine.USER_DIR+"\\"+imgPath));	
				} catch (Exception e) {e.printStackTrace();}
			}
			
			Item temp = new Item(name, id, stackSize, img);
			itemMap.put(name, temp);
		}
	}
	
	public static Item getItem(String name)
	{
		return itemMap.get(name);
	}
	
}
