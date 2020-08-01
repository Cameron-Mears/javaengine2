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
import engine.core.instance.InstanceID;
import engine.core.instance.InstanceMap;
import engine.core.tick.TickHandler;
import engine.core.tick.TickInfo;
import engine.core.tick.Tickable;
import external.org.json.JSONException;
import graphics.instance.InvalidInstanceException;
import graphics.viewer.Window;
import physics.general.Transform;
import physics.general.Vector2;

public class InputHandler implements MouseListener, KeyListener, MouseWheelListener, MouseMotionListener, Tickable
{	

	private static InstanceMap<InputHandler> map = new InstanceMap<InputHandler>();
	
	private InstanceID<InputHandler> id;
	
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
	
	private LinkedList<InputEventListener> listeners;
	
	private Transform transform;
	
	private boolean isQueued = false; //stops this from getting queued multiple times
	private int mouseX;
	private int mouseY;
	
	
	@Override
	public boolean equals(Object o)
	{
		return (o instanceof InputHandler)? ((InputHandler)o).id.equals(id): false;
	}
	
	public InputHandler(boolean enable)
	{
		listeners = new LinkedList<InputEventListener>();
		id = map.newInstanceID();
		if (enable) enable();
		
		/*
		 * Populate hashmaps with all keys and mouse buttons
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
	
	
	public void setTransform(Transform tx)
	{
		this.transform = tx;
	}
	
	public void addListener(InputEventListener listener)
	{
		listeners.add(listener); //listener can access input from methods
	}
	
	public boolean isKeyDown(int key)
	{
		return keyIsDown.get(key);
	}
	
	public boolean wasKeyPressed(int key)
	{
		return keyWasPressed.get(key);
	}
	
	public boolean wasKeyReleased(int key)
	{
		return keyWasReleased.get(key);
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
		Vector2 pos = new Vector2(mouseX, mouseY);
		if (transform != null)
		{
			pos.add(transform.getPosition());
		}
		return pos;
	}
	
	
	public void disable()
	{
		Window.getInstance().removeKeyListener(this);
		Window.getInstance().removeMouseListener(this);
		Window.getInstance().removeMouseMotionListener(this);
		Window.getInstance().removeMouseWheelListener(this);
	}
	
	public void enable()
	{
		Window.getInstance().addKeyListener(this);
		Window.getInstance().addMouseListener(this);
		Window.getInstance().addMouseMotionListener(this);
		Window.getInstance().addMouseWheelListener(this);
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
		if (!isQueued) 
		{
				TickHandler.getInstance().queueTickable(this);
				isQueued = true;
		}
		
		informListeners();
	}

	private void informListeners() {
		for (InputEventListener inputEventListener : listeners) {
			inputEventListener.newInput();
		}
		
	}


	@Override
	public void keyReleased(KeyEvent e) 
	{
		int key = e.getKeyCode();
		keyIsDown.put(key, false);
		keyWasReleased.put(key, true);
		keyReleases.add(key);
		
			TickHandler.getInstance().queueTickable(this);
		
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
		TickHandler.getInstance().queueTickable(this);
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
		TickHandler.getInstance().queueTickable(this);
		informListeners();
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
		mouseX = e.getX();
		mouseY = e.getY();
		
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
			Integer key = keyPresses.poll();
			keyWasPressed.put(key,false);
		}
		
		while (!keyReleases.isEmpty())
		{
			Integer key = keyReleases.poll();
			keyWasReleased.put(key,false);
		}
		while (!mousePresses.isEmpty())
		{
			Integer key = mousePresses.poll();
			mouseWasPressed.put(key,false);
		}
		
		while (!keyReleases.isEmpty())
		{
			Integer key = mouseReleases.poll();
			mouseWasReleased.put(key,false);
		}
		isQueued = false;
		
		
	}

}
