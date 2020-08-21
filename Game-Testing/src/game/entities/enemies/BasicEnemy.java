package game.entities.enemies;

import java.awt.Graphics2D;

import engine.core.tick.TickHandler;
import engine.core.tick.TickInfo;
import graphics.layer.GraphicsLayerManager;
import graphics.sprite.SpriteMap;
import physics.body.MassData;
import physics.body.Material;
import physics.body.PhysicsBody;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.general.Transform;
import physics.general.Vector2;

public class BasicEnemy extends Enemy
{
	private long depth;
	
	public BasicEnemy(long n)
	{
		super(500, 10, n);
		body = new PhysicsBody(new MassData(1), new Material(), new Transform());
		sprite = SpriteMap.getClonedSprite("enemy");
		hitbox = new HitBox(new Rectangle(32, 32, getPosition()), this);
		super.init(body, hitbox);
		
		TickHandler.getInstance().addTickable("default", this);
	}

	@Override
	public void tick(TickInfo info) 
	{
		pathFollower.onTick(info);
		sprite.tick(info);
	}

	@Override
	public void render(Graphics2D g2) {
	
		g2.drawImage(sprite.getCurrentFrame(), (int)body.getPosition().getX(), (int)body.getPosition().getY(), null);
	}

	@Override
	public Rectangle renderBoundingArea() {
		return hitbox.getBounds();
	}

	@Override
	public PhysicsBody getPhysicsBody() {
		return body;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
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
