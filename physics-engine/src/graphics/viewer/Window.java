package graphics.viewer;

import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;

import engine.core.Engine;
import external.org.json.JSONException;

public class Window extends JFrame
{
	private static final long serialVersionUID = 7882469958177323612L;
	private static Window instance;
	
	public static Window getInstance()
	{
		return instance;
	}
	
	public static Window init() throws JSONException, IOException
	{
		return new Window();
	}
	
	public static void refresh()
	{
		
	}
	
	private Window() throws JSONException, IOException
	{
		int width = (int) Engine.getInstance().getProperty("window_width");
		int height = (int) Engine.getInstance().getProperty("window_height");
		
		boolean fullscreen = (boolean) Engine.getInstance().getProperty("window_fullscreen");
		
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
}
