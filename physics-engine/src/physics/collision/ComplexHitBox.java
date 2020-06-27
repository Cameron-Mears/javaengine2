package physics.collision;

import java.awt.image.BufferedImage;

import engine.core.instance.EngineInstance;

//a pixel perfect bounding box
public class ComplexHitBox extends HitBox
{

	private boolean[][][] pixels; //keeps track of pixels that are considered collision pixels 3d array to keep track of multiple sprite frames
	
	public ComplexHitBox(Rectangle bounds, EngineInstance owner, BufferedImage...frames ) //outer bounds determine if it is worth trying pixel perfect collision
	{
		super(bounds, owner);
		
	}
	
	@Override
	public void testCollision(HitBox other)
	{
		if (bounds.contains(other.getBounds()))
		{
			//get overlap	
		}
	}
	
	public int[] getOverlap(Rectangle other)
	{
		return null;
	}

}
