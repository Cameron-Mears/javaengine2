package physics.collision;

import java.util.LinkedList;
import java.util.function.Function;

import engine.util.bst.BST;
import engine.util.quadtree.QuadTree;
import engine.util.quadtree.QuadTreeNode;
import physics.body.PhysicsBody;
import physics.general.Vector2;

public class CollisionLayer
{
	private String name;	
	private QuadTree<HitBox> quadTree;	
	private BST<Long, HitBox> members;	
	private Function<HitBox, Void> onCollision;
	private Function<HitBox, Void> toQuadTree;
	private Rectangle size;
	private Vector2 origin;
	
	public CollisionLayer(String name, Rectangle size, Vector2 origin) 
	{
		this.name = name;
		members = new BST<Long, HitBox>();
		
		toQuadTree = new Function<HitBox, Void>() {

			@Override
			public Void apply(HitBox t) {
				
				quadTree.insert(t.getNode());
				return null;
			}
			
			
		};
		
		onCollision = new Function<HitBox, Void>() {
			
			@Override
			public Void apply(HitBox t) 
			{
				LinkedList<QuadTreeNode<HitBox>> possibles = quadTree.queryRange(t.getBounds());
				for (QuadTreeNode<HitBox> hitbox : possibles) 
				{
					hitbox.get().testCollision(t);
				}
				return null;
			}
		};
		
	}

	public boolean addBody(PhysicsBody body)
	{
		return false;
	}
	
	public void resloveCollisions()
	{
		quadTree = new QuadTree<HitBox>(this.size, this.origin);
		members.inOrderTraverse(toQuadTree);
		members.inOrderTraverse(onCollision);
	}
}
