package physics.collision;

import java.util.LinkedList;

import engine.core.Engine;
import engine.core.instance.EngineInstance;
import engine.core.tick.Tickable;
import engine.util.quadtree.CollisionQuadTree;
import engine.util.quadtree.QuadTreeNode;
import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import physics.body.PhysicsBody;
import physics.general.Vector2;

public class CollisionLayer
{
	private String name;	
	private CollisionQuadTree<HitBox> quadTree;
	private HashTreeMap<Long, Collidable> members;	
	private TraverseFunction<Collidable> onCollision;
	private Rectangle size;
	private Vector2 origin;
	
	public CollisionLayer(String name, Rectangle size, Vector2 origin) 
	{
		this.name = name;
		members = new HashTreeMap<Long, Collidable>();
		this.size = size;
		this.origin = origin;
		quadTree = new CollisionQuadTree<HitBox>(size, origin);
		
		onCollision = new TraverseFunction<Collidable>() {
			
			@Override
			public void apply(Collidable collidabe) 
			{
				HitBox t = collidabe.getHitBox();
				LinkedList<QuadTreeNode<HitBox>> possibles = quadTree.queryRange(t.getBounds());
				for (QuadTreeNode<HitBox> hitbox : possibles) 
				{
					if (!hitbox.get().equals(t)) hitbox.get().testCollision(t);
				}
			}
		};
		
	}

	public boolean addBody(PhysicsBody body)
	{
		return false;
	}
	
	public void addCollidable(EngineInstance collidable)
	{
		Collidable collideInterface = (Collidable) collidable.getComponent("Collidable");
		members.put(collidable.getID().getID(), collideInterface);
		HitBox box = collideInterface.getHitBox();
		box.setCollisionLayer(this);
		quadTree.insert(box.getNode());
		box.setCollisionLayer(this);
	}
	
	public void addHitBoxDirect(HitBox box) //direct insertion to quadtree will not check for collisions every tick
	{
		quadTree.insert(box.myNode);
		box.setCollisionLayer(this);
	}
	
	public void checkCollisions(Collidable... boxes)
	{
		for (Collidable hitBox : boxes) 
		{
			onCollision.apply(hitBox);
		}
	}
	
	public void resloveCollisions()
	{
		members.inOrderTraverse(onCollision);
	}

	public String getName() {
		
		return name;
	}
}
