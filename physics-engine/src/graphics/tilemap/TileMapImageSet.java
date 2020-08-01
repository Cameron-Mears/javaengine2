package graphics.tilemap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import engine.core.Engine;
import engine.util.parsers.image.ImageParser;
import external.org.json.JSONArray;
import external.org.json.JSONObject;

public class TileMapImageSet 
{
	private String name;
	private ArrayList<BufferedImage> imageList;
	private int cellWidth, cellHeight;

	/**
	 * Loads in a tilemapiamge set from an json file
	 * @param json
	 * @throws IOException
	 */
	public TileMapImageSet(JSONObject json) throws IOException
	{
		this.name = json.getString("name");
		imageList = new ArrayList<BufferedImage>(json.getInt("nTiles")+1); //number of images in the in the image set (+1 for null image index 0 (empty cell))
		imageList.add(0, null); //tile_id 0 represents a empty cell
		this.cellHeight = json.getInt("cellHeight"); //width of the images in the tilemap
		this.cellWidth = json.getInt("cellWidth"); //height of the images in the tilemap
		
		JSONArray images = json.getJSONArray("images"); //each index the array contains a json object one of which contains the code for the tile, the other contains the path to the image
		
		for (int index = 0; index < images.length(); index++) 
		{
			JSONObject object = images.getJSONObject(index);
			int tileID = object.getInt("int_code");
			BufferedImage tile = ImageParser.parseImage(new File(Engine.USER_DIR+"\\"+object.getString("path"))); //load the image
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
