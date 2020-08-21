package game.entities.buildings.turrets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import engine.core.tick.TickInfo;
import engine.util.VarArgUtil;
import game.Level;
import game.entities.Player;
import graphics.util.GraphicsUtils;
import physics.collision.Circle;
import physics.collision.Rectangle;
import physics.collision.quadtree.CRQuadTree;
import physics.collision.ray.Ray;
import physics.general.Vector2;

public class BaseTurret extends Turret
{

	private double interval = 1;
	private double sum = 0;
	Ray ray;
	Vector2 raypos;
	LinkedList<Vector2> hits;
	private double rotvel = 2;
	
	private CRQuadTree<Rectangle> qt;
	boolean put;
	
	public BaseTurret(Vector2 pos) {
		super(pos, 350, "turret_level_1", TARGET_MODE.CLOSEST, VarArgUtil.range(0, 1));
		// TODO Auto-generated constructor stub
		raypos = position.duplicate();
		raypos.add(16,16);
		qt = new CRQuadTree<Rectangle>(2, new Rectangle(1000, 1000));
	}

	@Override
	public void onTick(TickInfo info) 
	{	
		/*
		sum += info.delta;
		target = aquireTarget();
		if (target != null)
		{
			rotation = position.angleTo(target.getPosition());
			if (sum > interval)
			{
				target.delete();
				sum = 0;
			}
		}
		*/
		
		if (!put && Level.raytest != null)
		{
			qt.put(Level.raytest, Level.raytest);
			put = true;
		}
		Player p = Level.player;
		if (p != null)
		{
			
			ray = new Ray(raypos, -position.angleTo(p.getPosition()), range);
			if (position.distanceToSq(p.getPosition()) > range*range)
			{
				Level.shoot = false;
			}
			else if (Level.raytest != null)
			{
				Level.shoot =Level.wallTree.isClearLine(ray);
			}
			if (!Level.shoot) rotation += rotvel * info.delta;
			else
			{
				rotation = position.angleTo(p.getPosition());
			}
			
		}
		
		if (health.get() == 0)
		{
			delete();
		}
		
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
		if (!Level.shoot)
		{
			if (ray != null)
			{
				g2.setColor(new Color(1f, 0f, 0f, 0.2f));
				g2.setStroke(new BasicStroke(6));
				GraphicsUtils.drawLine(g2, ray.origin(), ray.end());
			}
		}
		g2.setColor(new Color(1f, 0f, 0f, 0.2f));
		g2.setStroke(new BasicStroke(6));
		if (ray != null)GraphicsUtils.drawLine(g2, ray.origin(), ray.end());
		GraphicsUtils.drawCircle(g2, new Circle(position, range));
		//GraphicsUtils.drawRect(g2,rangeBoundingBox);
		if (hits == null) return;
		Vector2 closet = hits.peek();
		if (hits.size() > 0)
		{
			for (Vector2 hit : hits) 
			{
				if (position.distanceTo(hit) < position.distanceTo(closet)) closet = hit;
			}
			hits.clear();
		}
		if (closet != null)
		{
			g2.setColor(Color.black);
			g2.setStroke(new BasicStroke(3));
			GraphicsUtils.drawLine(g2, raypos, closet);
		}
		
		Vector2 pos = getPosition().duplicate();
		pos.addY(renderBounds.getHeight());
		Rectangle h = new Rectangle(40, 30, pos);
		health.render(g2, h, 3);
	}	
	
	
}
