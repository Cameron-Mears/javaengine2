package graphics.layer;

import java.awt.Graphics2D;
import java.util.LinkedList;

import engine.util.tree.HashTreeMap;
import graphics.Camera;
import graphics.instance.IGraphics;
import physics.collision.Rectangle;

public class GraphicsInstance 
{
	private long renderPriority;
	private LinkedList<IGraphics> renderInterfaces;
	private LinkedList<HashTreeMap<Long,GraphicsInstance>> gLayers;
	
	private boolean isCreatedFromGraphicsLayer;
	
	public GraphicsInstance(long depth)
	{
		
		this.renderPriority = depth;
		this.renderInterfaces = new LinkedList<IGraphics>();
		this.isCreatedFromGraphicsLayer = false;
		this.gLayers = new LinkedList<HashTreeMap<Long,GraphicsInstance>>();
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
		for (HashTreeMap<Long, GraphicsInstance> tree : gLayers) 
		{
			tree.put(renderPriority, null);
		}
	}
	
	public void assignedToLayer(HashTreeMap<Long, GraphicsInstance> g)
	{
		gLayers.add(g);
	}
	
	public void render(Graphics2D g2, Camera camera)
	{
		for (IGraphics iGraphics : renderInterfaces)
		{
			Rectangle bounds = iGraphics.renderBoundingArea();
			if (camera != null)
			{
				if (bounds != null)
				{
					if (camera.getBounds() != null && camera.getBounds().contains(bounds))
					{
						iGraphics.render(g2);
						continue;
					}
					continue;
				}
				iGraphics.render(g2);
				continue;
			}
			iGraphics.render(g2);
		}
	}
	
	
	public long getRenderPriority()
	{
		return renderPriority;
	}
	
	
}
