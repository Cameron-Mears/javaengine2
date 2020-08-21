package game.loader;

import java.util.LinkedList;

import engine.core.Engine;
import external.org.json.JSONArray;
import external.org.json.JSONException;
import external.org.json.JSONObject;
import graphics.tilemap.TileMap;
import graphics.tilemap.TileMapAssetMap;
import graphics.tilemap.TileMapImageSet;

public class LevelLoader 
{
	public static void load(JSONObject obj)
	{
		JSONArray imagesets = obj.getJSONArray("imagesets");
		
		for (int index = 0; index < imagesets.length(); index ++)
		{
			String setName = imagesets.getString(index);
			try
			{
				TileMapImageSet set = new TileMapImageSet(Engine.getGameFiles().get(setName));
				TileMapAssetMap.getInstance().putTileMapImageSet(set);;
			}
			catch (Exception e)
			{
				Engine.printWarningMessage("Error loading ImageSet -> " + setName + " Reason -> " + e.toString(), LevelLoader.class);
			}
		}
		
		JSONArray tilemaps = obj.getJSONArray("tilemaps");
		
		for (int index = 0; index < imagesets.length(); index ++)
		{
			String filename = tilemaps.getString(index);
			try
			{	JSONObject tm = Engine.getGameFiles().get(filename);
				TileMap map = new TileMap(tm, TileMapAssetMap.getInstance().getImageSet(tm.getString("imageset")), tm.getString("name"));
				try
				{
					JSONArray bgTiles = tm.getJSONArray("bgtiles");
					if (bgTiles.length() > 0)
					{
						int[] tiles = new int[bgTiles.length()];
						for (int i = 0; i < bgTiles.length(); i ++)
						{
							int code = bgTiles.getInt(i);
							tiles[i] = code;
						}
						map.setBackgroundTiles(tiles);
					}
				}
				catch (JSONException e) {
					// TODO: handle exception
				}
				TileMapAssetMap.getInstance().putTileMap(map);
								
			}
			catch (Exception e)
			{
				Engine.printWarningMessage("Error loading TileMap -> " + filename + " Reason -> " + e.toString(), LevelLoader.class);
			}
		}
	}
}
