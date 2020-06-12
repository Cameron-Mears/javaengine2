package game.entities;

import java.awt.Graphics2D;

import engine.core.Engine;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import graphics.instance.EngineInstance;
import graphics.instance.IGraphics;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;
import physics.body.MassData;
import physics.body.Material;
import physics.body.PhysicsBody;
import physics.collision.Shape;
import physics.general.Transform;
import physics.general.Vector2;

public class Player extends EngineInstance implements Tickable, IGraphics
{
	
	private Sprite player;
	private PhysicsBody body;
	public Player(Integer x)
	{
		super();
		//player = SpriteMap.getClonedSprite("player");
		body = new PhysicsBody(new MassData(1), new Material(), new Shape(), new Transform());
		System.out.println(x);
	}
	
	
	@Override
	public void onTick(TickInfo info) 
	{
		body.applyForce(new Vector2(1,0));
		body.tick(info);
		Engine.printDebugMessage(body.getVelocity().getX(), this);		
	}


	@Override
	public void render(Graphics2D g2) 
	{	
		g2.drawImage(player.getCurrentFrame(), (int)body.getPosition().getX(), (int)body.getPosition().getY(), null);
	}
	

	
}
