package graphics.tilemap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import engine.core.Engine;
import engine.util.parsers.image.ImageParser;
import external.org.json.JSONArray;
import external.org.json.JSONObject;

public class TileMapImageSet 
{
	private String name;
	private ArrayList<ImgNode> imageList;
	private int cellWidth, cellHeight;
	private int nTiles;

	/**
	 * Loads in a tilemapiamge set from an json file
	 * @param json
	 * @throws IOException
	 */
	
	public static class ImgNode
	{
		public final BufferedImage img;
		public final int code;
		
		ImgNode(BufferedImage img, int code)
		{
			this.code = code;
			this.img = img;
		}
	}
	
	public TileMapImageSet(JSONObject json) throws IOException
	{
		this.name = json.getString("name");
		nTiles = json.getInt("nTiles")+1;
		this.cellHeight = json.getInt("cellHeight"); //width of the images in the tilemap
		this.cellWidth = json.getInt("cellWidth"); //height of the images in the tilemap
		
		JSONArray images = json.getJSONArray("images"); //each index the array contains a json object one of which contains the code for the tile, the other contains the path to the image
		imageList = new ArrayList<ImgNode>(images.length()+1); //number of images in the in the image set (+1 for null image index 0 (empty cell))
		imageList.add(0, null); //tile_id 0 represents a empty cell
		imageList.ensureCapacity(images.length()+1);
		for (int index = 0; index < images.length(); index++) 
		{
			JSONObject object = images.getJSONObject(index);
			int tileID = object.getInt("int_code");
			BufferedImage tile = ImageParser.parseImage(new File(Engine.USER_DIR+"\\"+object.getString("path"))); //load the image
			ImgNode node = new ImgNode(tile,tileID);
			imageList.add(null);
			imageList.add(tileID, node);
		}
		nTiles = imageList.size();
	}
	
	public String getName()
	{
		return name;
	}
	
	
	public Iterator<ImgNode> iterator()
	{
		return imageList.iterator();
	}
	
	public ImgNode getNode(int index)
	{
		return imageList.get(index);
	}
	
	public BufferedImage getTile(int id)
	{
		if (id < 0 && id >= imageList.size()) return null;
		ImgNode node = imageList.get(id);
		return node == null? null:node.img;
	}
	
	public int nTiles()
	{
		return nTiles;
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
