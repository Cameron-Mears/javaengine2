package game.entities.buildings.turrets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import engine.core.exceptions.EngineException;
import engine.core.instance.EngineInstance;
import game.Level;
import game.entities.Entity;
import game.entities.enemies.Enemy;
import game.entities.health.Health;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;
import graphics.sprite.SpriteMap;
import physics.body.MassData;
import physics.body.PhysicsBody;
import physics.collision.Circle;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.general.Transform;
import physics.general.Vector2;

public abstract class Turret extends Entity
{
	protected Vector2 position;
	protected Vector2 center;
	protected BufferedImage base;
	protected BufferedImage top;
	protected Rectangle rangeBoundingBox;
	protected Rectangle renderBounds;
	protected Circle rangeCircle;
	
	protected Health health;
	
	private TARGET_MODE mode;
	
	private double rangeSq;
	
	
	protected int[] layerOrder;
	protected ArrayList<BufferedImage> imageSet;
	protected Enemy target;
	protected double range;
	protected double rotation;
	
	protected double sprCenterXOff;
	protected double sprCenterYOff;
	
	public static enum TARGET_MODE
	{
		CLOSEST,
		FIRST,
		LARGEST
	}
	
	public Turret(Vector2 pos, double range, String spritename, TARGET_MODE mode, int ...imgLayerOrder)
	{
		this.mode = mode;
		if (imgLayerOrder.length < 1) throw new EngineException("Invalid layer order size 0");
		this.position = pos;
		this.range = range;
		this.rangeSq = range*range;
		imageSet = new ArrayList<BufferedImage>();
		int sw, sh; //sprite width and height
		for (int i : imgLayerOrder) 
		{
			BufferedImage img = SpriteMap.getSpriteImage(spritename, i);
			imageSet.add(i, img);
		}
		BufferedImage img = imageSet.get(imgLayerOrder[0]);
		sw = img.getWidth();
		sh = img.getHeight();
		sprCenterXOff = sw/2;
		sprCenterYOff = sh/2;
		this.center = new Vector2(pos.x + sprCenterXOff, pos.y + sprCenterYOff);
		this.rangeBoundingBox = new Rectangle(pos.x - range + sprCenterXOff,pos.y - range + sprCenterYOff, range*2, range*2);
		this.renderBounds = new Rectangle(sw, sh, position);
		this.layerOrder = imgLayerOrder;
		GraphicsLayer gl = GraphicsLayerManager.getInstance().getLayer("turrets");
		gl.add(this);
		addToLayers(null, null, "default");
		this.rangeCircle = new Circle(center, range);
		
		this.health = new Health(1000);
	}
	
	public Turret(Vector2 pos) 
	{
		
		this.position = pos;
		this.body = new PhysicsBody(new MassData(1), null, new Transform(pos));
		base = SpriteMap.getSpriteImage("turret_level_1", 0);
		top = SpriteMap.getSpriteImage("turret_level_1", 1);
		this.hitbox = new HitBox(new Rectangle(base.getWidth(), base.getHeight(), position), this);
		
		double w = 700;
		double h = 700;
		this.rangeBoundingBox = new Rectangle(pos.x - w/2+16, pos.y - h/2+16, w, h);
		GraphicsLayer gl = GraphicsLayerManager.getInstance().getLayer("turrets");
		gl.add(this);
		addToLayers(null, null, "default");
		
	}
	
	protected Enemy aquireTarget()
	{
		Enemy targ = null;
		switch (mode) {
		case CLOSEST:
			targ = getNearest();
			break;
			
		case FIRST:
			targ = updateFirst();
			break;
		
		case LARGEST:
			targ = getHighestHealth();
			break;

		default:
			break;
		}
		return targ;
	}
	
	public void setTargetMode(TARGET_MODE mode)
	{
		if (this.mode == TARGET_MODE.FIRST && mode != TARGET_MODE.FIRST)
		{
			return;
		}
		this.mode = mode;
		target = aquireTarget();
	}
	
	public Enemy getNearest()
	{
		LinkedList<EngineInstance> enemies = Level.enemyTree.queryCollisions(rangeBoundingBox);
		double closest = Double.MAX_VALUE;
		Enemy ret = null;
		for (EngineInstance engineInstance : enemies) 
		{
			Enemy enemy = (Enemy) engineInstance;
			double dis = center.distanceToSq(enemy.getPosition());
			if (dis < rangeSq)
			{
				if (dis < closest)
				{
					ret = enemy;
					closest = dis;
				}
			}
		}
		return ret;
	}
	
	
	public Enemy getHighestHealth()
	{
		LinkedList<EngineInstance> enemies = Level.enemyTree.queryCollisions(rangeBoundingBox);
		double highest = Double.MIN_VALUE;
		Enemy ret = null;
		for (EngineInstance engineInstance : enemies) 
		{
			Enemy enemy = (Enemy) engineInstance;
			double dis = center.distanceToSq(enemy.getPosition());
			if (dis < rangeSq)
			{
				if (enemy.getHealth() > highest)
				{
					ret = enemy;
					highest = enemy.getHealth();
				}
			}
		}
		return ret;	
	}
	
	public Enemy updateFirst()
	{
		if (target != null)
		{
			if (target.isAlive())
			{
				double dis = center.distanceToSq(target.getPosition());
				if (dis > rangeSq) target = null;
				else return target;
			}
		}
		return getFirst();
		
	}
	public Enemy getFirst()
	{
		LinkedList<EngineInstance> enemies = Level.enemyTree.queryCollisions(rangeBoundingBox);
		long lowest = Long.MAX_VALUE;
		Enemy ret = null;
		for (EngineInstance engineInstance : enemies) 
		{
			Enemy enemy = (Enemy) engineInstance;
			double dis = center.distanceToSq(enemy.getPosition());
			if (dis < rangeSq)
			{
				if (enemy.getCreatedNumber() < lowest)
				{
					ret = enemy;
					lowest = enemy.getCreatedNumber();
				}
			}
		}
		return ret;	
	}
	
	
	@Override
	protected void beforeDelete() {
		
	}
	
	@Override
	public void render(Graphics2D g2) 
	{
		AffineTransform current = g2.getTransform();
		g2.setColor(Color.black);
		//GraphicsUtils.drawCircle(g2, this.rangeCircle);
		//GraphicsUtils.drawRect(g2, rangeBoundingBox);
		AffineTransform trans = new AffineTransform();
		trans.rotate((Math.PI/2)-rotation, center.x, center.y);
		g2.drawImage(imageSet.get(0), (int) position.x, (int)position.y, null);
		
		g2.transform(trans);
		
		g2.drawImage(imageSet.get(1), (int) position.x, (int)position.y, null);
		g2.setTransform(current);
		
		//if (target != null)GraphicsUtils.drawLine(g2, position, target.getPosition());
		
		Vector2 pPos = Level.player.getPosition();
		health.render(g2, renderBounds, 10);
		//GraphicsUtils.drawRect(g2,rangeBoundingBox);
		
	}

	@Override
	public Rectangle renderBoundingArea() 
	{
		return renderBounds;
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

	@Override
	public PhysicsBody getPhysicsBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2 getPosition() 
	{
		return position;
	}
	
	@Override
	public int getRenderPriority()
	{
		return 4;
	}
	
	public Health getHealth()
	{
		return health;
	}
	
}