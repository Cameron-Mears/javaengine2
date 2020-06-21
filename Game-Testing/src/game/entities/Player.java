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
import physics.collision.Shape;
import physics.general.Transform;
import physics.general.Vector2;

public class Player extends EngineInstance implements Tickable, IGraphics, InputEventListener
{
	
	private Sprite playerSprite;
	private PhysicsBody body;
	private InputHandler input;
	
	public Player(int x)
	{
		super();
		input = new InputHandler();
		playerSprite = SpriteMap.getClonedSprite("player");
		body = new PhysicsBody(new MassData(1), new Material(), new Shape(), new Transform());
	}
	
	public Player(JSONObject json)
	{
		this(json.getInt("test"));
	}
	
	
	@Override
	public void onTick(TickInfo info) 
	{
		if (input.wasKeyPressed('W'))
		{
			body.applyForce(new Vector2(10,10000));
			Engine.printDebugMessage(body.getVelocity().getX(), this);
			
		}
		body.tick(info);
		playerSprite.tick(info);
	}


	@Override
	public void render(Graphics2D g2) 
	{	
		g2.drawImage(playerSprite.getCurrentFrame(), (int)body.getPosition().getX(), (int)body.getPosition().getY(), null);
	}


	@Override
	public void onKeyPress(KeyEvent e) 
	{
				
	}


	@Override
	public void onKeyRelease(KeyEvent e) 
	{
				
	}


	@Override
	public void onKeyDown(KeyEvent e) 
	{
		if (e.getKeyCode() == 'w')
		{
			body.applyForce(new Vector2(1000,100));
		}
		
	}


	@Override
	public void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onMouseDown(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onMouseScroll(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	
}
