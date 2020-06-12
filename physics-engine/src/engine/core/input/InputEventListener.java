package engine.core.input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public interface InputEventListener 
{
	public void onKeyPress(KeyEvent e);
	
	public void onKeyRelease(KeyEvent e);
	
	public void onKeyDown(KeyEvent e);
	
	public void onMousePressed(MouseEvent e);
	
	public void onMouseReleased(MouseEvent e);
	
	public void onMouseDown(MouseEvent e);

	public void onMouseScroll(MouseWheelEvent e);
}
