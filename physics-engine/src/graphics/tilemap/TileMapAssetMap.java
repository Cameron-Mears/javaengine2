package graphics.tilemap;

import java.util.HashMap;

public class TileMapAssetMap 
{
	private HashMap<String, TileMapImageSet> imageSets;
	private HashMap<String, TileMap> tileMaps;
	
	private TileMapAssetMap()
	{
		imageSets = new HashMap<String, TileMapImageSet>();
		tileMaps = new HashMap<String, TileMap>();
	}
	
	private static TileMapAssetMap instance;
	
	public static TileMapAssetMap getInstance()
	{
		if (instance == null) instance = new TileMapAssetMap();
		return instance;
	}

	public TileMapImageSet getImageSet(String name)
	{
		return imageSets.get(name);
	}
	
	public TileMap getTileMap(String name)
	{
		return tileMaps.get(name);
	}
	
	public void putTileMap(TileMap map)
	{
		tileMaps.put(map.getName(), map);
	}
	
	public void putTileMapImageSet(TileMapImageSet map)
	{
		imageSets.put(map.getName(), map);
	}
}
