package game.entities.health;

import java.awt.Color;
import java.awt.Graphics2D;

import graphics.util.GraphicsUtils;
import physics.collision.Rectangle;

public class Health 
{
	private double health;
	private double max;
	
	private boolean isImmune;
	
	public Health(double amount)
	{
		this(amount,amount);
	}
	
	public Health(double amount, double max)
	{
		this.max = max;
		this.health = amount;
	}
	
	public double add(double amount)
	{
		if (isImmune) return 0;
		double sum = amount + health;
		if (sum <= 0)
		{
			health = 0;
			return sum - amount;
		}
		if (sum >= max)
		{
			health = max;
			return sum - max;
		}
		health = sum;
		return amount;
	}
	
	public double get()
	{
		return health;
	}
	
	public void render(Graphics2D g2, Rectangle size, int padding)
	{
		g2.setColor(Color.white);
		GraphicsUtils.fillRect(g2, size);
		g2.setColor(Color.black);
		g2.fillRect((int)size.getX()+padding, (int)size.getY()+padding, (int)size.getWidth()-padding*2, (int)size.getHeight()-padding*2);
		g2.setColor(Color.red);
		g2.fillRect((int)size.getX()+padding, (int)size.getY()+padding, (int)(size.getWidth() * (health/max))-padding*2, (int)size.getHeight()-padding*2);
	}
}
  