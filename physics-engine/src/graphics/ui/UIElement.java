package graphics.ui;

import engine.core.instance.InstanceID;
import physics.collision.Collidable;
import physics.collision.HitBox;

public class UIElement implements Collidable
{
	private InstanceID<UIElement> id;

	final void setID(InstanceID<UIElement> id)
	{
		this.id = id;
	}
	
	public UIElement() 
	{
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public HitBox getHitBox() {
		
		return null;
	}

	@Override
	public void onCollision(HitBox other) {
		// TODO Auto-generated method stub
		
	}
	
}
