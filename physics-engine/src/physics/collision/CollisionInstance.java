package physics.collision;

import java.util.LinkedList;

import engine.core.instance.EngineInstance;

/**
 * 
 * @author Cameron
 * Provides Collision Utility without having to use EngineInstance and The Collidable interface
 */
public class CollisionInstance extends EngineInstance implements Collidable
{

	private HitBox hitbox;
	private LinkedList<CollisionEventListener> listeners;
	
	public CollisionInstance(HitBox hit, CollisionEventListener listener) 
	{
		super();
		this.hitbox = hitbox;
	}

	@Override
	public HitBox getHitBox() {
		return hitbox;
	}

	@Override
	public void onCollision(HitBox other) 
	{
		CollisionEvent event = new CollisionEvent(this, other.getOwner());
		
		for (CollisionEventListener collisionEventListener : listeners) {
			collisionEventListener.onCollision(event);
		}
		
	}	
}
