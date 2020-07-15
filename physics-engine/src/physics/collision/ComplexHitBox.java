package physics.collision;

import java.awt.image.BufferedImage;

import engine.core.instance.EngineInstance;
import graphics.transform.Matrix;

//a pixel perfect bounding box
public class ComplexHitBox extends HitBox
{

	private Matrix a;
	
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
	
	public Rectangle getOverlap(Rectangle other)
	{
		return null;
	}

}
