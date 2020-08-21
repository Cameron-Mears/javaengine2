package game.renderer;

import java.awt.Graphics2D;

import engine.core.Engine;
import engine.core.instance.EngineInstance;
import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import game.loader.LevelLoader;
import graphics.Camera;
import graphics.instance.IGraphics;
import graphics.layer.GraphicsLayerManager;
import graphics.tilemap.TileMap;
import graphics.tilemap.TileMapAssetMap;
import physics.collision.Rectangle;

public class TileMapRenderer extends EngineInstance implements IGraphics
{

	private HashTreeMap<Long, TileMap> tilemaps;
	private TraverseFunction<TileMap> func;
	
	private Camera camera;
	private Graphics2D g2;
	
	
	public TileMapRenderer() {
		GraphicsLayerManager.getInstance().getLayer("default").addGraphics(this, Long.MIN_VALUE);
		LevelLoader.load(Engine.getGameFiles().get("Level1.json"));
		TileMap bg = TileMapAssetMap.getInstance().getTileMap("level1testmap");
		tilemaps = new HashTreeMap<Long, TileMap>();
		tilemaps.put(0L, bg);
	}
	
	@Override
	public void render(Graphics2D g2) 
	{
		camera = Renderer.getInstance().getActiveCamera();
		this.g2 = g2;
		tilemaps.inOrderTraverse(traverse());
	}

	@Override
	public Rectangle renderBoundingArea() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public TraverseFunction<TileMap> traverse()
	{
		if (func != null) return func;
		
		func = new TraverseFunction<TileMap>() {
			
			@Override
			public void apply(TileMap v) {
				v.render(g2, camera.getBounds());
		
			}
		};
		
		return func;
	}
	
}
