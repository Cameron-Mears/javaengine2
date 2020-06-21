package physics.collision;

import java.awt.Graphics2D;

import engine.util.quadtree.QuadTreeNode;

public class HitBox 
{
	private QuadTreeNode<HitBox> myNode;
	
	private Rectangle bounds;
	
	public QuadTreeNode<HitBox> getNode()
	{
		return myNode;
	}
	
	public Rectangle getBounds()
	{
		return null;
	}
	
	public void testCollision(HitBox other)
	{
	
	}
	
	public void drawHitBox(Graphics2D g2)
	{
		
	}
}
