package graphics.layer;

import java.awt.Graphics2D;
import java.util.LinkedList;

import engine.util.bst.BST;
import graphics.instance.IGraphics;

public class GraphicsInstance 
{
	private long renderPriority;
	private LinkedList<IGraphics> renderInterfaces;
	private LinkedList<BST<Long,GraphicsInstance>> gLayers;
	
	private boolean isCreatedFromGraphicsLayer;
	
	public GraphicsInstance(long depth)
	{
		
		this.renderPriority = depth;
		this.renderInterfaces = new LinkedList<IGraphics>();
		this.isCreatedFromGraphicsLayer = false;
		this.gLayers = new LinkedList<BST<Long,GraphicsInstance>>();
	}
	
	GraphicsInstance(long depth, boolean createdFromGraphicsLayer)
	{
		this(depth);
		this.isCreatedFromGraphicsLayer = createdFromGraphicsLayer;
	}
	
	public void addInterface(IGraphics interFace)
	{
		renderInterfaces.add(interFace);
		this.isCreatedFromGraphicsLayer = false;
	}
	
		
	
	boolean isCreatedFromGraphicsLayer()
	{
		return isCreatedFromGraphicsLayer;		
	}
	
	public void removeFromAllLayers()
	{
		for (BST<Long, GraphicsInstance> bst : gLayers) 
		{
			bst.deleteRec(bst.getRoot(), this.renderPriority);
		}
	}
	
	public void assignedToLayer(BST<Long, GraphicsInstance> g)
	{
		
		gLayers.add(g);
	}
	
	public void render(Graphics2D g2)
	{
		for (IGraphics iGraphics : renderInterfaces)
		{
			iGraphics.render(g2);
		}
	}
	
	
	public long getRenderPriority()
	{
		return renderPriority;
	}
	
	
}
