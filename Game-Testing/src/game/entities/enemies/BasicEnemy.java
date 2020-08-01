package game.entities.enemies;

import java.awt.Graphics2D;

import engine.core.tick.TickHandler;
import engine.core.tick.TickInfo;
import external.org.json.JSONObject;
import game.entities.Player;
import graphics.layer.GraphicsLayerManager;
import graphics.sprite.SpriteMap;
import physics.body.MassData;
import physics.body.Material;
import physics.body.PhysicsBody;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.general.Transform;
import physics.general.Vector2;

public class BasicEnemy extends Enemy
{
	private Player player;
	private int tickLife = 100;
	private long depth;
	
	public BasicEnemy(double x, double y, Player player)
	{
		super(new PhysicsBody(new MassData(1), new Material(), new Transform(x,y)));
		this.player = player;
		depth = GraphicsLayerManager.getInstance().getLayer("default").addGraphics(this, 0);
		TickHandler.getInstance().addTickable("default", this);
		sprite = SpriteMap.getClonedSprite("enemy");
		hitbox = new HitBox(new Rectangle(32, 32, getPosition()), this);
	}

	@Override
	public void onTick(TickInfo info, Object t) 
	{
		tickLife --;
		if (tickLife == 0)
		{
			GraphicsLayerManager.getInstance().getLayer("default").removeIGraphics(this, depth);
		}
		Vector2 position = body.getPosition();
		double direcitonToPlayer = position.angleTo(player.getPosition());
		body.getVelocity().setDirMag(direcitonToPlayer, 100);
		body.tick(info);
		
	}

	@Override
	public void render(Graphics2D g2) {
		
		g2.drawImage(sprite.getCurrentFrame(), (int)body.getPosition().getX(), (int)body.getPosition().getY(), null);
	}

	@Override
	public Rectangle renderBoundingArea() {
		return null;
	}

	@Override
	public PhysicsBody getPhysicsBody() {
		return body;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
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

}
