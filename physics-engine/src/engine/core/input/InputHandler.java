package engine.core.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import engine.core.exceptions.EngineException;
import engine.core.tick.TickHandler;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import external.org.json.JSONException;
import graphics.instance.InvalidInstanceException;
import graphics.viewer.Window;
import physics.general.Vector2;

public class InputHandler implements MouseListener, KeyListener, MouseWheelListener, MouseMotionListener, Tickable
{	

	private HashMap<Integer, Boolean> keyWasPressed;
	private HashMap<Integer, Boolean> keyWasReleased;
	private HashMap<Integer, Boolean> keyIsDown;
	
	private Queue<Integer> keyPresses;
	private Queue<Integer> keyReleases;	
	
	private HashMap<Integer, Boolean> mouseWasPressed;
	private HashMap<Integer, Boolean> mouseWasReleased;
	private HashMap<Integer, Boolean> mouseIsDown;
	
	private Queue<Integer> mousePresses;
	private Queue<Integer> mouseReleases;
	
	
	private boolean isQueued = false; //stops this from getting queued multiple times if
	
	
	public InputHandler()
	{
		try {
			Window.getInstance().addKeyListener(this);
			Window.getInstance().addMouseListener(this);
			Window.getInstance().addMouseMotionListener(this);
			Window.getInstance().addMouseWheelListener(this);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * Populate hashmaps with
		 */
		
		
		Field[] fields = KeyEvent.class.getDeclaredFields();
		int n = 0;

		keyWasPressed = new HashMap<Integer, Boolean>(fields.length);
		keyWasReleased = new HashMap<Integer, Boolean>(fields.length);
		keyIsDown = new HashMap<Integer, Boolean>(fields.length);
		
		keyPresses = new LinkedList<Integer>();
		keyReleases = new LinkedList<Integer>();	
		
		for (Field f : fields) 
		{
			if (Modifier.isStatic(f.getModifiers()) && !Modifier.isPrivate(f.getModifiers()))
		    {
		    	try 
		        {
			    	Class<?> type = f.getType();
			    	if (type.equals(int.class))
			    	{
				        n = f.getInt(null);
				        keyWasPressed.put(n,false);
				        keyIsDown.put(n, false);
				        keyWasReleased.put(n,false);
			    	} 
			    	
		        } catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
			    
		    } 
		}
		
		
		fields = MouseEvent.class.getDeclaredFields();
		n = 0;
		mouseWasPressed = new HashMap<Integer, Boolean>(fields.length);
		mouseWasReleased = new HashMap<Integer, Boolean>(fields.length);
		mouseIsDown = new HashMap<Integer, Boolean>(fields.length);
		
		mousePresses = new LinkedList<Integer>();
		mouseReleases = new LinkedList<Integer>();
		for (Field f : fields) 
		{
		    if (Modifier.isStatic(f.getModifiers()) && !Modifier.isPrivate(f.getModifiers())) 
		    {
		    	try 
		        {
			    	Class<?> type = f.getType();
			    	if (type.equals(int.class))
			    	{
			    		n = f.getInt(null);
			    		mouseWasPressed.put(n,false);
				        mouseIsDown.put(n, false);
				        mouseWasReleased.put(n,false);
			    	}
		        
				} catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
		    } 
		}
	}
	
	
	public boolean isKeyDown(char key)
	{
		return keyIsDown.get((int)key);
	}
	
	public boolean wasKeyPressed(char key)
	{
		return keyWasPressed.get((int)key);
	}
	
	public boolean wasKeyReleased(char key)
	{
		return keyWasReleased.get((int)key);
	}
	
	public boolean isMouseButtonDown(int button)
	{
		return mouseIsDown.get(button);
	}
	
	public boolean mouseButtonPressed(int button)
	{
	return mouseWasPressed.get(button);
	}
	
	public boolean mouseButtonReleased(int button)
	{
		return mouseWasReleased.get(button);
	}
	
	public Vector2 getMousePosition()
	{
		return null;
	}
	
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode();
		long when = e.getWhen();
		if (!(boolean)keyIsDown.get(key)) keyWasPressed.put(key,true);
		keyIsDown.put(key, true);
		keyPresses.add(key);
		try {
			if (!isQueued) 
			{
				TickHandler.getInstance().queueTickable(this);
				isQueued = true;
			}
		} catch (JSONException | InvalidInstanceException | EngineException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		int key = e.getKeyCode();
		keyIsDown.put(key, false);
		keyWasReleased.put(key, true);
		keyReleases.add(key);
		
		try {
			TickHandler.getInstance().queueTickable(this);
		} catch (JSONException | InvalidInstanceException | EngineException | IOException e1) {
		
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		int button = e.getButton();
		
		if (!mouseIsDown.get(button))
		{
			mouseIsDown.put(button,true);
			mouseWasPressed.put(button, true);
			mousePresses.add(button);
		}
		try {
			TickHandler.getInstance().queueTickable(this);
		} catch (JSONException | InvalidInstanceException | EngineException | IOException e1) { e1.printStackTrace(); }
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		int button = e.getButton();
		
		if (!mouseIsDown.get(button))
		{
			mouseIsDown.put(button,false);
			mouseWasReleased.put(button, true);
			mouseReleases.add(button);
		}
		try {
			TickHandler.getInstance().queueTickable(this);
		} catch (JSONException | InvalidInstanceException | EngineException | IOException e1) { e1.printStackTrace(); }
	}
		
	

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		e.getClickCount();
		
	}

	@Override
	public void onTick(TickInfo info) 
	{
		while (!keyPresses.isEmpty())
		{
			int key = keyPresses.poll();
			keyWasPressed.put(key,false);
		}
		
		while (!keyReleases.isEmpty())
		{
			int key = keyReleases.poll();
			keyWasReleased.put(key,false);
		}
		while (!mousePresses.isEmpty())
		{
			int key = mousePresses.poll();
			mouseWasPressed.put(key,false);
		}
		
		while (!keyReleases.isEmpty())
		{
			int key = mouseReleases.poll();
			mouseWasReleased.put(key,false);
		}
		isQueued = false;
		
		
	}

}
