package engine.core.console;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.JFrame;

import graphics.viewer.Window;

public class Console extends JFrame implements KeyListener
{
	private static final long serialVersionUID = 1L;
	private LinkedList<String> history;
	private int index;
	private StringBuilder currentInput;
	private Window window;
	
	public Console()
	{
		
	}
	

	@Override
	public void keyTyped(KeyEvent e) {
		
		char key = e.getKeyChar();
		if (!e.isActionKey())
		{
			if (e.isShiftDown())
			{
				currentInput.append(Character.toUpperCase(key));
			}
		}
		else
		{
			if (e.getKeyCode() == KeyEvent.VK_UP)
			{
				if (index + 1 >= history.size()) index = history.size()-1;
				String pastEntry = history.get(index);
			}
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void render()
	{
		
	}
	
}
