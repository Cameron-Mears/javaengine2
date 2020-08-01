package engine.core;

public class Asset 
{
	public static enum TYPE
	{
		TILEMAP,
		SPRITE,
		IMAGE,
		SOUND,
		TILEMAP_IMAGE_SET
	}
	
	protected final String name;
	protected final TYPE type;
	
	public Asset(String name, TYPE type)
	{
		this.type = type;
		this.name = name;
	}
}
