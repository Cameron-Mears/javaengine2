package game.entities.player.robot;

import java.awt.Graphics2D;

import engine.core.tick.TickInfo;
import game.entities.Entity;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;
import physics.body.MassData;
import physics.body.Material;
import physics.body.PhysicsBody;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.general.Transform;
import physics.general.Vector2;

public class Drone extends Entity
{
	private Sprite sprite;
	private Rectangle boundingBox;
	
	private int captureTurrets;
	private int captureLimit;
	
	private Rectangle discoveryBoundary;
	
	private RobotPlayer player;
	
	private Vector2 followTarget;
	
	private double velocity;
	
	
	public Drone() 
	{
		this.body = new PhysicsBody(new MassData(10), new Material(), new Transform());
		this.sprite = SpriteMap.getClonedSprite("Robot Drone");
		this.boundingBox = sprite.createNewBoundingBox(getPosition());
	}
	
	
	@Override
	public void onTick(TickInfo info) 
	{	
		
	}

	@Override
	public void render(Graphics2D g2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle renderBoundingArea() {
		return boundingBox;
	}

	@Override
	public HitBox getHitBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCollision(HitBox other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PhysicsBody getPhysicsBody() 
	{
		return body;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}
}
