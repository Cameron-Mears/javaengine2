package graphics.tilemap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import engine.util.parsers.image.ImageParser;
import external.org.json.JSONArray;
import external.org.json.JSONObject;

public class TileMapImageSet 
{
	private String name;
	private ArrayList<BufferedImage> imageList;
	private int cellWidth, cellHeight;

	public TileMapImageSet(JSONObject json) throws IOException
	{
		this.name = json.getString("name");
		imageList = new ArrayList<BufferedImage>(json.getInt("nTiles")+1);
		imageList.add(0, null);
		this.cellHeight = json.getInt("cellHeight");
		this.cellWidth = json.getInt("cellWidth");
		imageList.add(0, null); //tile id zero represents an empty cell
		
		JSONArray images = json.getJSONArray("images");
		
		for (int index = 0; index < images.length(); index++) 
		{
			JSONObject object = images.getJSONObject(index);
			int tileID = object.getInt("int_code");
			BufferedImage tile = ImageParser.parseImage(new File(object.getString("path")));
			imageList.add(tileID, tile);
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public BufferedImage getTile(int id)
	{
		return imageList.get(id);
	}
	
	public int getCellWidth() 
	{
		return cellWidth;
	}

	public int getCellHeight() 
	{
		return cellHeight;
	}
}
