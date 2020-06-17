package game.entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import engine.core.input.InputEventListener;
import engine.core.instance.EngineInstance;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
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
	
	private Sprite player;
	private PhysicsBody body;
	
	public Player(Integer x)
	{
		super();
		player = SpriteMap.getClonedSprite("player");
		body = new PhysicsBody(new MassData(10), new Material(), new Shape(), new Transform());
	}
	
	
	@Override
	public void onTick(TickInfo info) 
	{
		body.applyForce(new Vector2(10,100));
		body.tick(info);
		player.tick(info);
	}


	@Override
	public void render(Graphics2D g2) 
	{	
		g2.drawImage(player.getCurrentFrame(), (int)body.getPosition().getX(), (int)body.getPosition().getY(), null);
	}


	@Override
	public void onKeyPress(KeyEvent e) 
	{
		
		
	}


	@Override
	public void onKeyRelease(KeyEvent e) {
		// TODO Auto-generated method stub
		
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
