package game.weapons.bullet;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import engine.core.tick.TickInfo;
import graphics.sprite.Sprite;
import graphics.util.GraphicsUtils;
import physics.general.Vector2;

public class LaserImageBuilder 
{
	private Sprite laser;
	int frame = 0;
	int nframes;
	
	double deltaF;
	double deltaFSum = 0;
	
	public LaserImageBuilder(Sprite laser)
	{
		this.laser = laser;
		this.nframes = laser.getFrameCount();
		 deltaF = 1d/laser.getCurrentFPS();
	}
	
	public void draw(Graphics2D g2, Vector2 start, Vector2 end, double delta)
	{
		//GraphicsUtils.drawLine(g2, start, end);
		
		if (start == null || end == null) throw new NullPointerException();
		double distance = start.distanceTo(end);
		double theta = start.angleTo(end);
		
		double w = laser.getWidth();
		double h = laser.getHeight();
		double x = start.x;
		double y = start.y;
		
		double dx = w * Math.cos(theta);
		
		double dy = h * Math.sin(theta);
		AffineTransform current = g2.getTransform();
		
		deltaFSum += delta;
		boolean changeFrame = false;
		if (deltaFSum >= deltaF)
		{
			changeFrame = true;
			deltaFSum = 0;
		}
		
		g2.rotate(Math.PI-theta, start.x, start.y);
		
		for (int i = 0; i < distance; i += w) 
		{
			g2.setTransform(current);
			g2.rotate(Math.PI-theta, x+laser.getWidth()/2, y);
			g2.drawImage(laser.getFrame(frame), (int)x+laser.getWidth()/2, (int)y,null);
			if (changeFrame)
			{frame ++;
				if (frame >= nframes-1) frame = 0;
				
			}
			x += dx;
			y -= dy;
			
		}
		if (changeFrame) frame ++;
		g2.setTransform(current);
		
	}
}
