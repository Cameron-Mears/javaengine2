package engine.util.parsers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import engine.core.JSON_CONSTANTS;
import engine.core.exceptions.EngineException;
import engine.util.parsers.image.ImageParser;
import external.org.json.JSONArray;
import external.org.json.JSONException;
import external.org.json.JSONObject;
import external.org.json.JSONTokener;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;
import graphics.tilemap.TileMap;
import graphics.tilemap.TileMapAssetMap;
import graphics.tilemap.TileMapImageSet;

public class AssetMapParser 
{
	private static final String USER_DIR = System.getProperty("user.dir")+"\\";
	
	public static void parseAssetMap(File assetMap) throws JSONException, IOException, EngineException
	{
		JSONArray assets = new JSONArray(new JSONTokener(new FileInputStream(assetMap)));
		TileMapAssetMap  tileMapAssets = TileMapAssetMap.getInstance();
		
		for (int index = 0; index < assets.length(); index++) 
		{
			JSONObject asset = assets.getJSONObject(index);
			
			switch (asset.getString("type")) 
			{
				case "sprite":					
					AssetMapParser.parseSpritefromJSON(asset);
					break;
					
					
				case "tilemap":
					tileMapAssets.putTileMap(new TileMap(asset, tileMapAssets.getImageSet(asset.getString("name")), asset.getString("name")));
					break;
				case "tilemapimageset":
					tileMapAssets.putTileMapImageSet(parserImageSet(asset));
					break;
				case "hitbox":
				case "sound":
					break;
				default:
					throw new IllegalArgumentException("Unexpected value: " + asset.getString("type"));
			}
		}
	}
	
	
	private static boolean parseSpritefromJSON(JSONObject asset) throws JSONException, IOException
	{
		int fps = asset.getInt("fps");
		int nFrames = asset.getInt("numFrames");
		ArrayList<BufferedImage> frames = new ArrayList<BufferedImage>(nFrames);
		
		JSONArray images = asset.getJSONArray(JSON_CONSTANTS.FILE_PATH);
		
		for (int frame = 0; frame < nFrames; frame++) 
		{
			BufferedImage img = ImageParser.parseImage(new File(USER_DIR + images.getString(frame)));
			img.setAccelerationPriority(1);
			frames.add(frame, img);
		}
		
		Sprite spr = new Sprite(fps, frames, asset.getString(JSON_CONSTANTS.OBJECT_NAME));
		SpriteMap.put(asset.getString(JSON_CONSTANTS.OBJECT_NAME), spr);
		
		return true;
	}
	
	private static TileMapImageSet parserImageSet(JSONObject asset) throws IOException
	{
		TileMapImageSet set = new TileMapImageSet(asset);
		return set;
		
	}
	
	
	
}
