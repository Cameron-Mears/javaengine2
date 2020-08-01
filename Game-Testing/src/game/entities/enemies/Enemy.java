package game.entities.enemies;

import java.awt.Rectangle;

import game.entities.Entity;
import graphics.sprite.Sprite;
import physics.body.PhysicsBody;
import physics.collision.HitBox;

public abstract class Enemy extends Entity
{
	public Enemy(PhysicsBody body) {
		super();
	}

	protected HitBox hitbox;
	protected PhysicsBody body;
	protected Sprite sprite;
	
	public final void a()
	{
		Rectangle a = new Rectangle();
	}
}
