package game.entities;

import engine.core.instance.EngineInstance;
import engine.core.tick.TickHandler;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import engine.core.tick.TickableGroup;
import engine.util.quadtree.ConcurrentQuadTreeNode;
import graphics.instance.IGraphics;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;
import physics.body.PhysicsBody;
import physics.collision.Collidable;
import physics.collision.CollisionLayer;
import physics.collision.CollisionLayerManager;
import physics.collision.HitBox;
import physics.general.Vector2;

public abstract class Entity extends EngineInstance implements Tickable, IGraphics, Collidable
{
	public abstract PhysicsBody getPhysicsBody();
	public abstract Vector2 getPosition();
	public abstract void onTick(TickInfo tf, Object somethinmgelse);
	
	protected ConcurrentQuadTreeNode<?> quadTreeNode;
	protected PhysicsBody body;
	protected HitBox hitbox;
	public Entity() 
	{
		
	}
	
	public void addToLayers(String collisionLayer, String graphicsLayer, String tickableGroup)
	{
		if (collisionLayer != null)
		{
			CollisionLayer cl = CollisionLayerManager.getInstance().getLayer(collisionLayer);
			if (cl != null)
			{
				if (getHitBox() == null)
				{
					
				}
				else cl.addCollidable(this);
			}
		}
		
		if (graphicsLayer != null)
		{
			GraphicsLayer gl = GraphicsLayerManager.getInstance().getLayer(graphicsLayer);
			if (gl != null)
			{
				gl.addGraphics(this, getRenderPriority());
			}
		}
		
		if (tickableGroup != null)
		{
			TickableGroup tg = TickHandler.getInstance().getGroup(tickableGroup);
			if (tg != null) tg.addTickable(this);
		}
		
	}
	
	@Override
	public final void onTick(TickInfo info)
	{
		if (body != null)
		{
			if (body.hasMoved())
			{
				quadTreeNode.update();
				//update quadtree position
			}
		}
		onTick(info, this);
	}
}
