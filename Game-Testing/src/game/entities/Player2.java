package game.entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import engine.core.Engine;
import engine.core.input.InputEventListener;
import engine.core.input.InputHandler;
import engine.core.instance.EngineInstance;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import external.org.json.JSONObject;
import graphics.instance.IGraphics;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;
import physics.body.MassData;
import physics.body.Material;
import physics.body.PhysicsBody;
import physics.collision.CollisionLayerManager;
import physics.collision.HitBox;
import physics.collision.Shape;
import physics.general.Transform;
import physics.general.Vector2;

public class Player2 extends EngineInstance implements Tickable, IGraphics
{
	
	private Sprite playerSprite;
	private PhysicsBody body;
	private InputHandler input;
	private HitBox hitbox;
	
	public Player2(int x)
	{
		super();
		input = new InputHandler();
		playerSprite = SpriteMap.getClonedSprite("player");
		body = new PhysicsBody(new MassData(1), new Material(), new Shape(), new Transform(23,2));
		hitbox = playerSprite.getHitBox(body.getPosition(), this);
		CollisionLayerManager.getInstance().getDefaultlayer().addHitBox(hitbox);
	}
	
	public Player2(JSONObject json)
	{
		this(json.getInt("test"));
	}
	
	
	@Override
	public void onTick(TickInfo info) 
	{
		if (input.isKeyDown('W')) body.applyForce(0,-100);
		if (input.isKeyDown('S')) body.applyForce(0,100);
		if (input.isKeyDown('A')) body.applyForce(-100,0);
		if (input.isKeyDown('D')) body.applyForce(100,0);
		
		double friction_x = (Math.abs(body.getVelocity().getX()) > 0)? 40 * -Math.signum(body.getVelocity().getX()):0;
		double friction_y = (Math.abs(body.getVelocity().getY()) > 0)? 40 * -Math.signum(body.getVelocity().getY()):0;
		
		body.applyForce(friction_x, friction_y);
		body.tick(info);
		playerSprite.tick(info);
	}


	@Override
	public void render(Graphics2D g2) 
	{	
		g2.drawImage(playerSprite.getCurrentFrame(), (int)body.getPosition().getX(), (int)body.getPosition().getY(), null);
		hitbox.drawHitBox(g2);
	}
	
}
