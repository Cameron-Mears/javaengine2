package game.entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import engine.core.Engine;
import engine.core.input.InputHandler;
import engine.core.instance.EngineInstance;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import external.org.json.JSONObject;
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

public class Player extends EngineInstance implements Tickable, IGraphics, CollisionEventListener
{
	
	private Sprite playerSprite;
	private PhysicsBody body;
	private InputHandler input;
	private HitBox hitbox;
	private CollisionLayer layer;
	private Camera camera;
	
	public Player(int x)
	{
		super();
		input = new InputHandler();
		playerSprite = SpriteMap.getClonedSprite("player");
		body = new PhysicsBody(new MassData(1), new Material(), new Shape(), new Transform(100,100));
		hitbox = playerSprite.getHitBox(body.getPosition(), this);
		hitbox.addCollisionEventListener(this);
		layer = CollisionLayerManager.getInstance().getDefaultlayer();
		layer.addHitBox(hitbox);
	}
	
	public Player(JSONObject json)
	{
		this(json.getInt("test"));
	}
	
	
	@Override
	public void onTick(TickInfo info) 
	{
		if (input.isKeyDown(KeyEvent.VK_UP)) body.applyForce(0,-100);
		if (input.isKeyDown(KeyEvent.VK_DOWN)) body.applyForce(0,100);
		if (input.isKeyDown(KeyEvent.VK_LEFT)) body.applyForce(-100,0);
		if (input.isKeyDown(KeyEvent.VK_RIGHT)) body.applyForce(100,0);
		
		double friction_x = (Math.abs(body.getVelocity().getX()) > 0)? 40 * -Math.signum(body.getVelocity().getX()):0;
		double friction_y = (Math.abs(body.getVelocity().getY()) > 0)? 40 * -Math.signum(body.getVelocity().getY()):0;
		
		body.applyForce(friction_x, friction_y);
		body.tick(info);
		playerSprite.tick(info);
		layer.checkCollisions(hitbox);
	}


	@Override
	public void render(Graphics2D g2) 
	{	
		TileMap tm = TileMapAssetMap.getInstance().getTileMap("background");
		tm.render(g2);
		g2.drawImage(playerSprite.getCurrentFrame(), (int)body.getPosition().getX(), (int)body.getPosition().getY(), null);
		hitbox.drawHitBox(g2);
	}


	@Override
	public void onCollision(CollisionEvent event) 
	{
		Engine.printDebugMessage("hello", this);	
	}

	@Override
	public Rectangle renderBoundingArea() 
	{
		return hitbox.getBounds();
	}
	

	
}
