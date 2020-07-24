package game.entities.enemies;

import engine.core.tick.Tickable;
import game.entities.Entity;
import graphics.instance.IGraphics;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;
import physics.body.PhysicsBody;
import physics.collision.HitBox;

public abstract class Enemy extends Entity
{
	protected HitBox hitbox;
	protected PhysicsBody body;
	protected Sprite sprite;
}
