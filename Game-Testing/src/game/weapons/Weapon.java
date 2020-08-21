package game.weapons;

import java.awt.Graphics2D;
import java.util.LinkedList;

import engine.core.instance.EngineInstance;
import engine.core.tick.TickInfo;
import game.enums.WeaponType;
import graphics.instance.IGraphics;
import graphics.sprite.Sprite;
import graphics.transform.Matrix;
import physics.general.Vector2;

public abstract class Weapon extends EngineInstance
{
	protected Sprite sprite;
	protected LinkedList<WeaponModifier> mods;
	
	protected Vector2 shootPoint;
	
	protected double bulletVel;
	protected WeaponType type;
	protected double range;
	
	protected double rotation;
	
	
	public abstract void shoot(TickInfo info, Vector2 location);
	
	public abstract void reload();
	
	
	public Vector2 getBulletShootOrigin(double rotation)
	{
		Matrix matrix = new Matrix(rotation);
		return matrix.mulvec(shootPoint);
	}

	public abstract void render(Graphics2D g2, double x, double y);
	
	
	
	
}	
