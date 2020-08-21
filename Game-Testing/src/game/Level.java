package game;

import engine.core.instance.EngineInstance;
import engine.util.VarArgUtil;
import engine.util.pathing.Path;
import engine.util.pathing.PathNode.Mode;
import engine.util.pathing.TileMapPathGenerator;
import game.entities.Entity;
import game.entities.Player;
import game.entities.buildings.turrets.Turret;
import game.renderer.TileMapRenderer;
import game.weapons.bullet.Bullet;
import graphics.instance.IGraphics;
import graphics.tilemap.TileMap;
import graphics.tilemap.TileMapAssetMap;
import physics.collision.Rectangle;
import physics.collision.quadtree.CRQuadTree;
import physics.general.Vector2;

public class Level 
{
	private static Path levelPath;
	public static boolean shoot = false;
	public static Player player;
	public static Turret turret;
	public static Rectangle raytest;
	public final static CRQuadTree<EngineInstance> enemyTree;
	public final static CRQuadTree<IGraphics> turretTree;
	
	public  static CRQuadTree<Rectangle> wallTree;
	public static final Rectangle levelBounds;
	
	
	public static final CRQuadTree<Bullet> bulletTree;
	public static final CRQuadTree<Entity> playerTree;
	
	static
	{
		new TileMapRenderer();
		TileMap tm = TileMapAssetMap.getInstance().getTileMap("test");
		TileMapPathGenerator tmpg = new TileMapPathGenerator(tm, Mode.SPLIT, VarArgUtil.range(2, 7));
		
		
		long now = System.nanoTime();
		levelPath = tmpg.solve(new Vector2(63,5), new Vector2(0,13));
		levelPath.scale(32);
		
		enemyTree = new CRQuadTree<EngineInstance>(4, new Rectangle(32*64, 32*24));
		turretTree = new CRQuadTree<IGraphics>(10, new Rectangle(32*64, 32*24));
		wallTree = new CRQuadTree<>(10,new Rectangle(32*64, 32*24));
		levelBounds = new Rectangle(10000,10000);
		bulletTree = new CRQuadTree<Bullet>(10,levelBounds);
		playerTree = new CRQuadTree<Entity>(10,levelBounds);
		
	}
	
	public static Path getLevelPath()
	{
		return levelPath;
	}
}
