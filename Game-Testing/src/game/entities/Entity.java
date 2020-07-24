package game.entities;

import engine.core.instance.EngineInstance;
import engine.core.tick.Tickable;
import graphics.instance.IGraphics;
import physics.body.PhysicsBody;
import physics.collision.Collidable;
import physics.general.Vector2;

public abstract class Entity extends EngineInstance implements Tickable, IGraphics, Collidable
{
	public abstract PhysicsBody getPhysicsBody();
	public abstract Vector2 getPosition();
}
