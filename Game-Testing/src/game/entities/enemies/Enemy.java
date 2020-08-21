package game.entities.enemies;

import engine.core.instance.EngineInstance;
import engine.core.instance.InstanceID;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import engine.util.pathing.EndOfPathListener;
import engine.util.pathing.PathFollower;
import game.Level;
import game.entities.Entity;
import graphics.sprite.Sprite;
import physics.body.PhysicsBody;
import physics.collision.HitBox;
import physics.collision.quadtree.CRQuadTree;
import physics.general.Vector2;

public abstract class Enemy extends Entity implements EndOfPathListener
{
	
	protected InstanceID<CRQuadTree.Node<EngineInstance>> crqtNode;
	protected HitBox hitbox;
	protected PhysicsBody body;
	protected Sprite sprite;
	private long createdNumber;
	protected PathFollower pathFollower;
	
	protected double health;
	protected double velocity;
	
	protected abstract void tick(TickInfo info);
	
	static
	{
		//TickScheduler.getInstance().addPeriodic(periodicGenerator, TimeUtils.secondsToNanos(0.2));
	}
	
	public Enemy(double vel, double hp, long createdNumber)
	{
		this.velocity = vel;
		this.health= hp;
		this.createdNumber = createdNumber;
	}
	
	public void init(PhysicsBody body, HitBox hitbox)
	{
		this.body = body;
		pathFollower = new PathFollower(velocity,this);
		Vector2 pos = body.getPosition();
		Vector2 pathHead = Level.getLevelPath().head().getPosition();
		pos.set(pathHead.x, pathHead.y);
		pathFollower.join(Level.getLevelPath(), pos);
		crqtNode = Level.enemyTree.put(hitbox.getBounds(), (EngineInstance) this);
	}
	
	public double getHealth()
	{
		return health;
	}
	
	public long getCreatedNumber() 
	{
		return createdNumber;
	}
	
	@Override
	public void onTick(TickInfo info)
	{
		if (crqtNode != null) Level.enemyTree.updateEntry(crqtNode);
		tick(info);
	}
	
	@Override
	protected void beforeDelete()
	{
		if (crqtNode != null)Level.enemyTree.remove(crqtNode, true);
		crqtNode = null;
	}
	
	
	@Override
	public void onceAtEnd(PathFollower follower)
	{
		this.delete();
	}
}
