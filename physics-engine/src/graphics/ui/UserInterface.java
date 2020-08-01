package graphics.ui;

import java.util.LinkedList;

import engine.core.input.InputEventListener;
import engine.core.input.InputHandler;
import engine.core.instance.InstanceMap;
import engine.util.quadtree.ConcurrentQuadTree;
import engine.util.quadtree.ConcurrentQuadTreeNode;
import physics.collision.Rectangle;

public class UserInterface implements InputEventListener
{
	private ConcurrentQuadTree tree;
	private InputHandler input;
	private InstanceMap<UIElement> elementMap;
	private Rectangle size;
	public UserInterface(Rectangle size)
	{
		this.input = new InputHandler(false);
		this.elementMap = new InstanceMap<UIElement>();
		this.tree = new ConcurrentQuadTree(size,4);
		this.size = size;
	}
	
	public void addElement(UIElement elem)
	{
		if (elem == null) throw new NullPointerException();
		elem.setID(elementMap.newInstanceID());
		ConcurrentQuadTreeNode<UIElement> node = new ConcurrentQuadTreeNode<UIElement>(elem.getHitBox().getBounds(), elem);
		tree.put(node);
	}
	
	public void removeElement(UIElement elem)
	{
		
	}
	
	private void parseInput()
	{
		if (input.mouseButtonReleased(1)) //left click
		{
			LinkedList<ConcurrentQuadTreeNode<?>> elems = tree.query(new Rectangle(1, 1, input.getMousePosition()));
			if (elems.size() > 0)
			{
				for (ConcurrentQuadTreeNode<?> concurrentQuadTreeNode : elems) {
					
					UIElement elem = (UIElement) concurrentQuadTreeNode.getValue();
				}
			}
		}
	}

	@Override
	public void newInput() 
	{
		parseInput();		
	}
	
	
}
