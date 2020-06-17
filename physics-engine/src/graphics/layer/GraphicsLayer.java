package graphics.layer;

import java.awt.Graphics2D;
import java.util.function.Function;

import engine.util.bst.BST;
import graphics.instance.IGraphics;

public class GraphicsLayer 
{
	
	private String name;
	
	private BST<Long, Object> members;
	private BST<Long,GraphicsInstance> renderOrder;
	private Graphics2D g2;
	private Function<GraphicsInstance, Void> traverse;

	public GraphicsLayer(String name) 
	{
		this.name = name;
		renderOrder = new BST<Long, GraphicsInstance>();
		members = new BST<Long, Object>();
		traverse = new Function<GraphicsInstance, Void>() 
		{

			@Override
			public Void apply(GraphicsInstance graphics) 
			{		
				
				graphics.render(g2);
				return null;
			}
		};
	}
	
	public String getName()
	{
		return name;
	}
	
	public void render(Graphics2D g2)
	{
		this.g2 = g2;
		if (renderOrder.getRoot() != null)
		{
			renderOrder.inOrderTraverse(renderOrder.getRoot(), traverse);
		}
	}

	
	//returns the depth assigned to the graphics instance if that depth is already taken
	public long addIGrpahics(IGraphics instance, long depth) 
	{
		GraphicsInstance g = renderOrder.find(depth);
		
		if (g != null)
		{
			if (g.isCreatedFromGraphicsLayer())
			{
				g.addInterface(instance);
				return depth;
			}
			else
			{
				depth++;
				return addIGrpahics(instance, depth);
			}
		}
		
		GraphicsInstance gInstance = new GraphicsInstance(depth, true);
		gInstance.addInterface(instance);
		gInstance.assignedToLayer(renderOrder);
		renderOrder.addNode(depth, gInstance);
		return depth;
	}
	
	public boolean removeIGraphics(IGraphics instance, int depth)
	{
		return false;
	}
	
	public boolean addGraphicsInstance(GraphicsInstance g)
	{
		return false;
	}
	
	
	
}
