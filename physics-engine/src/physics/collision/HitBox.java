package physics.collision;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.LinkedList;

import engine.core.exceptions.EngineException;
import engine.core.instance.EngineInstance;
import engine.core.tick.TickHandler;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import engine.util.quadtree.CollisionNode;
import engine.util.quadtree.QuadTreeNode;
import external.org.json.JSONException;
import graphics.instance.InvalidInstanceException;
import physics.general.Vector2;

public class HitBox implements Tickable
{
	
	public static enum TYPE
	{
		RECT,
		CIRCLE,
		PIXEL_PERFECT
	}
	
	protected LinkedList<CollisionEventListener> listeners;
	protected EngineInstance owner;
	protected long id;

	protected CollisionNode<HitBox> myNode;
	protected CollisionLayer myLayer;
	protected Vector2 lastPosition;
	protected Rectangle bounds;
	protected TYPE type;
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof HitBox)
		{
			return ((HitBox) other).getOwner().equals(owner);
		}
		return false;
	}
	
	public HitBox(Rectangle bounds, EngineInstance owner)
	{

		this.bounds = bounds;
		this.listeners = new LinkedList<CollisionEventListener>();
		if (owner == null) this.id = (long)(Math.random() * Long.MAX_VALUE);
		else this.id = owner.getID().getID();
		this.myNode = new CollisionNode<HitBox>(bounds.getPosition(), this, this);
		this.owner = owner;
		this.lastPosition = bounds.getPosition().clone();
		
	}
	
	
	public CollisionNode<HitBox> getNode()
	{
		return myNode;
	}
	
	public Rectangle getBounds()
	{
		return bounds;
	}
	
	public void setCollisionLayer(CollisionLayer layer)
	{
		this.myLayer = layer;
	}
	
	public EngineInstance getOwner()
	{
		return owner;
	}
	
	public void addCollisionEventListener(CollisionEventListener e)
	{
		listeners.add(e);
	}
	
	public void testCollision(HitBox other)
	{
		boolean collision = other.getBounds().contains(bounds);
		if (!lastPosition.equals(bounds.getPosition()))
		{
			try {
				//this will remove it from the quadtree at the end of the engine tick and it will be reinserted
				TickHandler.getInstance().queueTickable(this);
			} catch (JSONException | InvalidInstanceException | EngineException | IOException e) {
				e.printStackTrace();
			}
		}
		if (collision)
		{
			CollisionEvent event = new CollisionEvent(owner, other.getOwner());
			for (CollisionEventListener collisionEventListener : listeners) 
			{
				collisionEventListener.onCollision(event);
			}
		}
	}
	
	public void drawHitBox(Graphics2D g2)
	{
		g2.setStroke(new BasicStroke(4));
		g2.setColor(Color.black);
		g2.drawRect((int)bounds.getPosition().getX(), (int)bounds.getPosition().getY(), (int)bounds.getWidth(), (int)bounds.getHeight());
	}

	@Override
	public void onTick(TickInfo info) 
	{
		myNode.removeFromQuadTree();
		myNode.unChecked();
		myLayer.addHitBoxDirect(this);
	}
}
