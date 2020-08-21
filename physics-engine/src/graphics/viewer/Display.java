package graphics.viewer;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.swing.JFrame;

import engine.core.Engine;
import engine.core.exceptions.EngineException;
import external.org.json.JSONException;
import physics.collision.Rectangle;

public class Display extends JFrame
{
	private static final long serialVersionUID = 7882469958177323612L;
	private static Display instance;
	public static int WIDTH;
	public static int HEIGHT;
	private BufferStrategy bs;
	private Graphics2D g2;
	private Canvas canvas;
	private Rectangle bounds;
	private boolean fullscreen;
	
	/**
	 * 
	 * @return the default game window
	 * @throws JSONException
	 * @throws IOException
	 * @throws EngineException
	 */
	public static Display getInstance()
	{
		if (instance == null)
		{
			instance = init();
		}
		return instance;
	}
	
	public static Display init()
	{
		return new Display();
	}
	
	public static void refresh()
	{
		
	}
	
	public Display(int width, int height, int x, int y, boolean fullScreen, String title)
	{
		this.setTitle(title);
		this.setSize(width, height);
		this.setLocation(x, y);
		this.setUndecorated(fullScreen);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(true);
	}
	
	private Display()
	{
		
		int width = (int) Engine.getInstance().getProperty("window_width");
		int height = (int) Engine.getInstance().getProperty("window_height");
		WIDTH = width;
		HEIGHT = height;
		fullscreen = (boolean) Engine.getInstance().getProperty("window_fullscreen");
		
		this.setTitle((String) Engine.getInstance().getProperty("title"));
		
		this.setLocation(100, 100);
		this.setResizable(true);
		
		if (fullscreen)
		{
			this.setUndecorated(true);
			this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		}
		else
		{
			this.setSize(width, height);
		}
		
		this.setVisible(true);
		
		this.setFocusTraversalKeysEnabled(false);
		
		this.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent we) {
	           System.exit(0);
	         }
	     }
	);
	
	}
	
	public Rectangle getRect()
	{
		if (bounds == null) bounds = new Rectangle(getWidth(), getHeight());
		return bounds;
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
				this.createBufferStrategy(3);
				bs = getBufferStrategy();
				return createGraphics();
			}
		}
		this.g2 = (Graphics2D) bs.getDrawGraphics();
		if (!fullscreen)g2.translate(0, 31);
		return g2;
		 
		
	}
}
