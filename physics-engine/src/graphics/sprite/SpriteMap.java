package graphics.sprite;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import engine.core.exceptions.EngineException;

public class SpriteMap 
{
	
	static 
	{
		sprites = new HashMap<String, Sprite>();
	}
	
	private static HashMap<String, Sprite> sprites;
	
	public static void put(String name, Sprite sprite)
	{
		sprites.put(name, sprite);
	}
	
	public static Sprite getClonedSprite(String name)
	{
		Sprite sprite = sprites.get(name);
		return (sprite != null)? sprite.clone() : null;
	}

	public static BufferedImage getSpriteImage(String name, int frameIndex) 
	{
		Sprite sprite = sprites.get(name);
		if (sprite == null) throw new EngineException("No such Sprite -> " + name);
		return sprite.getFrame(frameIndex);	
	}
	
}
