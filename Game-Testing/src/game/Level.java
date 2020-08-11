package game;

import engine.util.VarArgUtil;
import engine.util.pathing.Path;
import engine.util.pathing.TileMapPathGenerator;
import engine.util.pathing.PathNode.Mode;
import graphics.tilemap.TileMap;
import graphics.tilemap.TileMapAssetMap;
import physics.general.Vector2;

public class Level 
{
	private static Path levelPath;
	
	static
	{
		TileMap tm = TileMapAssetMap.getInstance().getTileMap("background");
		int[] test =  VarArgUtil.range(2, 6);
		for (int i = 0; i < test.length; i++) {
			
		}
		TileMapPathGenerator tmpg = new TileMapPathGenerator(tm, Mode.SPLIT, VarArgUtil.range(2, 7));
		
		
		long now = System.nanoTime();
		levelPath = tmpg.solve(new Vector2(63,5), new Vector2(0,13));
	}
	
	public static Path getLevelPath()
	{
		return levelPath;
	}
}
