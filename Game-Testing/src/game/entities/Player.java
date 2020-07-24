package game.entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import engine.core.Engine;
import engine.core.input.InputHandler;
import engine.core.instance.EngineInstance;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import engine.util.json.JSONSerializable;
import external.org.json.JSONObject;
import game.entities.enemies.BasicEnemy;
import game.entities.enemies.Enemy;
import graphics.Camera;
import graphics.instance.IGraphics;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;
import graphics.tilemap.TileMap;
import graphics.tilemap.TileMapAssetMap;
import physics.body.MassData;
import physics.body.Material;
import physics.body.PhysicsBody;
import physics.collision.CollisionEvent;
import physics.collision.CollisionEventListener;
import physics.collision.CollisionLayer;
import physics.collision.CollisionLayerManager;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.collision.Shape;
import physics.general.Transform;
import physics.general.Vector2;

public class Player extends Entity implements JSONSerializable
{
	
	private Sprite playerSprite;
	private PhysicsBody body;
	private InputHandler input;
	private HitBox hitbox;
	private CollisionLayer layer;
	private Camera camera;
	private BasicEnemy enemy;
	
	public Player(int x)
	{
		super();
		input = new InputHandler();
		playerSprite = SpriteMap.getClonedSprite("player");
		body = new PhysicsBody(new MassData(1), new Material(), new Transform(100,100));
		hitbox = playerSprite.getHitBox(body.getPosition(), this);
		layer = CollisionLayerManager.getInstance().getDefaultlayer();
		layer.addCollidable(this);
	}
	
	public Player(JSONObject json)
	{
		this(json.getInt("test"));
	}
	
	
	@Override
	public void onTick(TickInfo info) 
	{
		//enemy = new BasicEnemy((Math.random() * 300) + 100, (Math.random() * 300) + 100, this);
		Vector2 velocity = body.getVelocity();
		int yDirection = 0;
		if (input.isKeyDown(KeyEvent.VK_W)) --yDirection;
		if (input.isKeyDown(KeyEvent.VK_S)) ++yDirection;
		velocity.setY(300 * yDirection);
		int xDirection = 0;
		if (input.isKeyDown(KeyEvent.VK_D)) ++xDirection;
		if (input.isKeyDown(KeyEvent.VK_A)) --xDirection;
		
		velocity.setX(300 * xDirection);
		
		double friction_x = (Math.abs(body.getVelocity().getX()) > 0)? 40 * -Math.signum(body.getVelocity().getX()):0;
		double friction_y = (Math.abs(body.getVelocity().getY()) > 0)? 40 * -Math.signum(body.getVelocity().getY()):0;
		
		body.applyForce(friction_x, friction_y);
		body.tick(info);
		playerSprite.tick(info);
	}


	@Override
	public void render(Graphics2D g2) 
	{	
		TileMap tm = TileMapAssetMap.getInstance().getTileMap("background");
		tm.render(g2, hitbox.getBounds());
		g2.drawImage(playerSprite.getCurrentFrame(), (int)body.getPosition().getX(), (int)body.getPosition().getY(), null);
		hitbox.drawHitBox(g2);
	}

	@Override
	public Rectangle renderBoundingArea() 
	{
		return hitbox.getBounds();
	}

	@Override
	public JSONObject serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PhysicsBody getPhysicsBody() {
		
		return body;
	}

	@Override
	public Vector2 getPosition() 
	{
		return body.getPosition();
	}

	@Override
	public HitBox getHitBox() 
	{		
		return hitbox;
	}

	@Override
	public void onCollision(HitBox other) 
	{
		System.out.println("as");
		
	}
	

	
}
