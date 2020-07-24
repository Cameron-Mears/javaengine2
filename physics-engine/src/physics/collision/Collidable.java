package physics.collision;

import engine.core.instance.EngineComponent;

public interface Collidable extends EngineComponent
{
	public HitBox getHitBox();
	
	public void onCollision(HitBox other);
}
