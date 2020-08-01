package graphics.ui;

import java.awt.image.BufferedImage;

import physics.collision.HitBox;

public class UIButton extends UIElement
{
	private BufferedImage buttonImage;
	private UIButtonActionListener listener;
	
	
	@Override
	public void onCollision(HitBox other) 
	{
		listener.onClick(this);
	}
	
}
