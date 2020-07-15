package graphics.viewer;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.swing.JFrame;

import engine.core.Engine;
import engine.core.exceptions.EngineException;
import external.org.json.JSONException;

public class Window extends JFrame
{
	private static final long serialVersionUID = 7882469958177323612L;
	private static Window instance;
	private BufferStrategy bs;
	private Graphics2D g2;
	private boolean fullscreen;
	
	/**
	 * 
	 * @return the default game window
	 * @throws JSONException
	 * @throws IOException
	 * @throws EngineException
	 */
	public static Window getInstance() throws JSONException, IOException, EngineException
	{
		if (instance == null)
		{
			instance = init();
		}
		return instance;
	}
	
	public static Window init() throws JSONException, IOException, EngineException
	{
		return new Window();
	}
	
	public static void refresh()
	{
		
	}
	
	public Window(int width, int height, int x, int y, boolean fullScreen, String title)
	{
		this.setTitle(title);
		this.setSize(width, height);
		this.setLocation(x, y);
		this.setUndecorated(fullScreen);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(true);
	}
	
	private Window() throws JSONException, IOException, EngineException
	{
		int width = (int) Engine.getInstance().getProperty("window_width");
		int height = (int) Engine.getInstance().getProperty("window_height");
		
		fullscreen = (boolean) Engine.getInstance().getProperty("window_fullscreen");
		
		this.setTitle((String) Engine.getInstance().getProperty("title"));
		
		this.setLocation(100, 100);
		this.setResizable(false);
		
		if (fullscreen)
		{
			this.setUndecorated(true);
			this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		}
		else
		{
			this.setSize(width, height);
		}
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	
	
	public void showGraphics()
	{
		bs.show();
	}
	
	public Graphics2D createGraphics()
	{
		if (g2 != null) g2.dispose();
		
		if (bs == null)
		{
			bs = this.getBufferStrategy();
			if (bs == null)
			{
				this.createBufferStrategy(2);
				bs = getBufferStrategy();
				return createGraphics();
			}
		}
		this.g2 = (Graphics2D) bs.getDrawGraphics();
		g2.translate(0, 32);
		return g2;
	}
}
