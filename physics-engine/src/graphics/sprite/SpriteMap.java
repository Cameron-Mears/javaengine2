package graphics.sprite;

import java.util.HashMap;

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
		return sprite.clone();
	}
	
}
