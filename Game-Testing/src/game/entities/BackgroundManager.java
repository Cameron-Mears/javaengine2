package game.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import engine.core.Engine;
import engine.core.instance.EngineInstance;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import external.org.json.JSONObject;
import graphics.instance.IGraphics;

public class BackgroundManager extends EngineInstance implements IGraphics, Tickable
{	
	public BackgroundManager(JSONObject json) 
	{
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void render(Graphics2D g2) {
		g2.setColor(Color.white);
		try {
			
			g2.fillRect(0, 0, (int) Engine.getInstance().getProperty("window_width"), (int) Engine.getInstance().getProperty("window_height"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void onTick(TickInfo info) {
		// TODO Auto-generated method stub
		
	}

}
