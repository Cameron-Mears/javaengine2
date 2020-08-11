package game.entities.buildings.turrets;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import engine.core.tick.TickInfo;
import game.entities.Entity;
import game.entities.enemies.Enemy;
import graphics.sprite.SpriteMap;
import physics.body.MassData;
import physics.body.PhysicsBody;
import physics.collision.Circle;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.collision.Shape;
import physics.general.Transform;
import physics.general.Vector2;

public class Turret extends Entity
{
	private Vector2 position;
	private BufferedImage base;
	private BufferedImage top;
	private HitBox hitbox;
	private Circle rangeCircle;
	private Rectangle rangeBoundingBox;
	
	private double range;
	
	private Enemy target;
	
	
	public Enemy getNearest()
	{
		return null;
	}
	
	public Turret(Vector2 pos) 
	{
		this.rangeCircle = new Circle(position, range);
		this.position = pos;
		this.body = new PhysicsBody(new MassData(1), null, new Transform());
		base = SpriteMap.getSpriteImage("turret_level_1", 0);
		top = SpriteMap.getSpriteImage("turret_level_1", 1);
		this.hitbox = new HitBox(new Rectangle(base.getWidth(), base.getHeight(), position), this);
		double w= base.getWidth();
		double h = base.getHeight();
		this.rangeBoundingBox = new Rectangle(pos.x - w/2, pos.y - h/2, w, h);
	}
	
	@Override
	public void render(Graphics2D g2) 
	{
		AffineTransform current = g2.getTransform();
		
		AffineTransform trans = new AffineTransform();
		double rotation = body.getTansform().getRotation();
		trans.rotate(rotation, position.x+16, position.y+16);
		g2.drawImage(base, (int) position.x, (int)position.y, null);
		
		g2.transform(trans);
		
		g2.drawImage(top, (int) position.x, (int)position.y, null);
		g2.setTransform(current);
		
	}

	@Override
	public Rectangle renderBoundingArea() 
	{
		return hitbox.getBounds();
	}

	@Override
	public HitBox getHitBox() {
		// TODO Auto-generated method stub
		return hitbox;
	}

	@Override
	public void onCollision(HitBox other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PhysicsBody getPhysicsBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2 getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onTick(TickInfo tf, Object somethinmgelse) {
		
		body.getTansform().addRotation(Math.toRadians(1));
		
	}
	
	@Override
	public int getRenderPriority()
	{
		return 4;
	}
	
}