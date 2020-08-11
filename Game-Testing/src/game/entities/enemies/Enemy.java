package game.entities.enemies;

import java.awt.Rectangle;

import engine.core.random.Rand;
import engine.core.tick.TickScheduler;
import engine.core.tick.Tickable;
import engine.util.TimeUtils;
import engine.util.pathing.EndOfPathListener;
import engine.util.pathing.PathFollower;
import game.Level;
import game.entities.Entity;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;
import graphics.sprite.Sprite;
import physics.body.PhysicsBody;
import physics.collision.HitBox;
import physics.general.Vector2;

public abstract class Enemy extends Entity implements EndOfPathListener
{
	
	private static Tickable periodicGenerator = (info)->{
		new BasicEnemy();
	};
	
	static
	{
		//TickScheduler.getInstance().addPeriodic(periodicGenerator, TimeUtils.secondsToNanos(0.2));
	}
	
	public Enemy(PhysicsBody body) 
	{
		
		super();
		this.body = body;
		pathFollower = new PathFollower(Rand.range(8, 8),this);
		Vector2 pos = body.getPosition();
		Vector2 pathHead = Level.getLevelPath().head().getPosition();
		pos.set(pathHead.x, pathHead.y);
		pathFollower.join(Level.getLevelPath(), pos);
	}

	protected HitBox hitbox;
	protected PhysicsBody body;
	protected Sprite sprite;
	
	protected PathFollower pathFollower;
	
	protected double health;
	
	public final void a()
	{
		Rectangle a = new Rectangle();
	}
	
	
	
	@Override
	public void onceAtEnd(PathFollower follower)
	{
		GraphicsLayer defaultLayer = GraphicsLayerManager.getInstance().getLayer("default");
		this.delete();
	}
}
