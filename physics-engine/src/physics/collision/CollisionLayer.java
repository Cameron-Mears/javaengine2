package physics.collision;

import java.util.LinkedList;
import java.util.function.Function;

import engine.util.bst.BST;
import engine.util.quadtree.CollisionNode;
import engine.util.quadtree.CollisionQuadTree;
import engine.util.quadtree.QuadTreeNode;
import physics.body.PhysicsBody;
import physics.general.Vector2;

public class CollisionLayer
{
	private String name;	
	private CollisionQuadTree<HitBox> quadTree;
	private BST<Long, HitBox> members;	
	private Function<HitBox, Void> onCollision;
	private Rectangle size;
	private Vector2 origin;
	
	public CollisionLayer(String name, Rectangle size, Vector2 origin) 
	{
		this.name = name;
		members = new BST<Long, HitBox>();
		this.size = size;
		this.origin = origin;
		quadTree = new CollisionQuadTree<HitBox>(size, origin);
		
		onCollision = new Function<HitBox, Void>() {
			
			@Override
			public Void apply(HitBox t) 
			{
				LinkedList<QuadTreeNode<HitBox>> possibles = quadTree.queryRange(t.getBounds());
				for (QuadTreeNode<HitBox> hitbox : possibles) 
				{
					if (!hitbox.get().equals(t)) hitbox.get().testCollision(t);
				}
				return null;
			}
		};
		
	}

	public boolean addBody(PhysicsBody body)
	{
		return false;
	}
	
	public void addHitBox(HitBox box)
	{
		members.addNode(box.getOwner().getID().getID(), box);
		quadTree.insert(box.getNode());
		box.setCollisionLayer(this);
	}
	
	public void addHitBoxDirect(HitBox box) //direct insertion to quadtree will not check for collisions every tick
	{
		members.addNode(box.getOwner().getID().getID(), box);
		box.setCollisionLayer(this);
	}
	
	public void checkCollisions(HitBox... boxes)
	{
		for (HitBox hitBox : boxes) 
		{
			onCollision.apply(hitBox);
		}
	}
	
	public void resloveCollisions()
	{
		members.inOrderTraverse(onCollision);
	}
}
