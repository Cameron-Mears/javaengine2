package game.weapons.bullet;

import java.util.LinkedList;

import engine.core.instance.EngineInstance;
import engine.core.tick.Tickable;
import game.entities.Entity;
import game.enums.WeaponType;
import graphics.instance.IGraphics;
import physics.collision.Rectangle;
import physics.collision.quadtree.CRQuadTree;
import physics.general.Transform;
import physics.general.Vector2;

public abstract class Bullet extends EngineInstance implements Tickable, IGraphics
{
	private BulletEventListener eventListener;
	
	protected int penetrationPower;
	
	protected Vector2 range; //length and direction
	protected Vector2 origin;
	protected Transform tranform;
	
	protected Rectangle bounds;
	
	protected WeaponType type;
	
	public Bullet()
	{
		
	}
	
	public void setListener(BulletEventListener listener)
	{
		this.eventListener = listener;
	}
	
	public LinkedList<Entity> getHitScanHits(CRQuadTree<Entity> qt, Entity ... toIngore)
	{
		
		return null;
	}
	
}
