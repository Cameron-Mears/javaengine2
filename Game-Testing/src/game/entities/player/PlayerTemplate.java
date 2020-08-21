package game.entities.player;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.core.input.InputHandler;
import engine.core.instance.InstanceID;
import engine.core.tick.TickInfo;
import engine.util.math.MathUtils;
import game.entities.Entity;
import game.enums.PlayerClass;
import game.weapons.Weapon;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;
import physics.body.MassData;
import physics.body.Material;
import physics.body.PhysicsBody;
import physics.collision.Rectangle;
import physics.general.Transform;
import physics.general.Vector2;

public abstract class PlayerTemplate extends Entity
{	
	protected double movementSpeed = 300;
	protected InputHandler input;
	protected Vector2 velocity;
	
	
	protected PlayerClass playerClass;
	
	protected ArrayList<Weapon> weapons;
	
	protected Sprite sprLeft;
	protected Sprite sprRight;
	protected Sprite sprUp;
	protected Sprite sprDown;
	protected Sprite sprCurrent;
	
	protected Rectangle bounds;
	protected InstanceID<Entity> qtNode;
	
	public abstract void actionSkill();
	
	public PlayerTemplate()
	{
		input = new InputHandler(true);
		
		body = new PhysicsBody(new MassData(5), new Material(), new Transform(50,50));
		
		sprUp = sprCurrent = SpriteMap.getClonedSprite("player_walk_up");
		sprDown = SpriteMap.getClonedSprite("player_walk_down");
		sprRight = SpriteMap.getClonedSprite("player_walk_right");
		sprLeft = SpriteMap.getClonedSprite("player_walk_left");
		bounds = sprCurrent.createNewBoundingBox(getPosition());
		velocity = body.getVelocity();
		modifiers.speed = 1;
		
	}
	
	
	public void move(TickInfo info)
	{
		int xDir = 0, yDir = 0;
		
		xDir = (input.isKeyDown(KeyEvent.VK_A)? -1:0) + (input.isKeyDown(KeyEvent.VK_D)? 1:0);
		yDir = (input.isKeyDown(KeyEvent.VK_S)? -1:0) + (input.isKeyDown(KeyEvent.VK_W)? 1:0);
		
		double speed = movementSpeed * modifiers.speed;
		
		double direction = MathUtils.direction(yDir, xDir);
		
		if (xDir != 0 || yDir != 0)
		{
			sprCurrent = sprRight;
			if (direction > Math.PI/4) sprCurrent = sprDown;
			if (direction > 3*Math.PI/4) sprCurrent = sprLeft;
			if (direction > 5*Math.PI/4) sprCurrent = sprUp;
		}
		
		if (xDir != 0 || yDir != 0)
		{
			velocity.setDirMag(direction, speed);
			sprCurrent.tick(info);
		}
		else velocity.set(0, 0);
		body.tick(info);
		
		
	}
	
	public ArrayList<Weapon> getWeapons()
	{
		return weapons;
	}
	
	public PlayerClass getPlayerClass()
	{
		return playerClass;
	}
	
}
