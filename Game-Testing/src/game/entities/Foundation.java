package game.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.core.instance.EngineInstance;
import graphics.instance.IGraphics;
import physics.collision.Rectangle;
import physics.general.Transform;

public class Foundation extends EngineInstance implements IGraphics
{
	protected Transform tx;
	protected BufferedImage img;
	
	@Override
	public void render(Graphics2D g2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle renderBoundingArea() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
