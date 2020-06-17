package engine.util.parsers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import engine.core.JSON_CONSTANTS;
import engine.util.parsers.image.ImageParser;
import external.org.json.JSONArray;
import external.org.json.JSONException;
import external.org.json.JSONObject;
import external.org.json.JSONTokener;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;

public class AssetMapParser 
{
	public static void parseAssetMap(File assetMap) throws JSONException, IOException
	{
		JSONArray assets = new JSONArray(new JSONTokener(new FileInputStream(assetMap)));
		
		for (int index = 0; index < assets.length(); index++) 
		{
			JSONObject asset = assets.getJSONObject(index);
			
			switch (asset.getString("type")) 
			{
				case "sprite":					
					AssetMapParser.parseSpritefromJSON(asset);
					break;
					
					
				case "tilemap":
				
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
			BufferedImage img = ImageParser.parseImage(new File(images.getString(frame)));
			frames.add(frame, img);
		}
		
		Sprite spr = new Sprite(fps, frames, asset.getString(JSON_CONSTANTS.OBJECT_NAME));
		SpriteMap.put(asset.getString(JSON_CONSTANTS.OBJECT_NAME), spr);
		
		return true;
	}
	
	
	
	
}
