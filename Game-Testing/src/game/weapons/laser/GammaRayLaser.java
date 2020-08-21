package game.weapons.laser;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import engine.core.tick.TickInfo;
import game.weapons.Weapon;
import graphics.sprite.SpriteMap;
import graphics.util.GraphicsUtils;
import physics.collision.ray.Ray;
import physics.general.Vector2;

public class GammaRayLaser extends Weapon
{
	private Ray laser;

	public GammaRayLaser() {
		sprite = SpriteMap.getClonedSprite("gamma_ray_rifle");
	}
	
	@Override
	public void render(Graphics2D g2, double x, double y)
	{
		AffineTransform tx = g2.getTransform();
		g2.rotate(rotation, x + sprite.getWidth()/2, y + sprite.getHeight()/2);
		if (shootPoint != null)
		{
			Vector2 drawStart = super.getBulletShootOrigin(rotation);
			g2.drawImage(sprite.getCurrentFrame(), (int)x, (int) y, null);
			drawStart.add(x,y);
			GraphicsUtils.drawLine(g2, drawStart, shootPoint);
			g2.setTransform(tx);
		}
	}


	@Override
	public void shoot(TickInfo info, Vector2 location) 
	{
		this.shootPoint = location;
		rotation = shootPoint.angleTo(location);
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	
}
