package game.entities;

import java.util.HashMap;
import java.util.LinkedList;

import engine.core.instance.EngineInstance;
import engine.core.tick.TickHandler;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import engine.core.tick.TickableGroup;
import engine.util.quadtree.ConcurrentQuadTreeNode;
import game.entities.modifiers.EntityModifiers;
import game.enums.Elements;
import graphics.instance.IGraphics;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;
import physics.body.PhysicsBody;
import physics.collision.Collidable;
import physics.collision.CollisionLayer;
import physics.collision.CollisionLayerManager;
import physics.collision.HitBox;
import physics.collision.quadtree.CRQuadTree;
import physics.general.Vector2;

public abstract class Entity extends EngineInstance implements Tickable, IGraphics, Collidable
{
	public abstract PhysicsBody getPhysicsBody();
	public abstract Vector2 getPosition();
	
	protected PhysicsBody body;
	protected HitBox hitbox;
	
	protected EntityModifiers modifiers;
	
	protected int debuffCount;
	
	protected HashMap<Elements, Boolean> underEffects;
	
	
	public Entity() 
	{
		this.modifiers = new EntityModifiers();
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
}
