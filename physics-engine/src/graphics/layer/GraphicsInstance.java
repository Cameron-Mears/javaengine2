package graphics.layer;

import java.awt.Graphics2D;
import java.util.LinkedList;

import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import graphics.Camera;
import graphics.instance.IGraphics;
import physics.collision.Rectangle;

public class GraphicsInstance 
{
	private long renderPriority;
	private Graphics2D g2;
	private Camera camera;
	private TraverseFunction<IGraphics> renderFunction;
	private HashTreeMap<Long, IGraphics> renderInterfaces;
	private LinkedList<HashTreeMap<Long,GraphicsInstance>> gLayers;
	
	private boolean isCreatedFromGraphicsLayer;
	
	public GraphicsInstance(long depth)
	{
		this.renderFunction = (graphics)->{
			Rectangle bounds = graphics.renderBoundingArea();
			if (camera != null)
			{
				if (bounds != null)
				{
					if (camera.getBounds() != null && camera.getBounds().contains(bounds))
					{
						graphics.render(g2);
						return;
					}
					return;
				}
				graphics.render(g2);
				return;
			}
			graphics.render(g2);
		};
		this.renderPriority = depth;
		this.renderInterfaces = new HashTreeMap<Long, IGraphics>();
		this.isCreatedFromGraphicsLayer = false;
		this.gLayers = new LinkedList<HashTreeMap<Long,GraphicsInstance>>();
	}
	
	GraphicsInstance(long depth, boolean createdFromGraphicsLayer)
	{
		this(depth);
		this.isCreatedFromGraphicsLayer = createdFromGraphicsLayer;
	}
	
	public void addInterface(long depth, IGraphics interFace)
	{
		renderInterfaces.put(depth, interFace);
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
		this.g2 = g2;
		this.camera = camera;
		renderInterfaces.inOrderTraverse(renderFunction);
	}
	
	
	public long getRenderPriority()
	{
		return renderPriority;
	}

	public void remove(long requestedDepth) {
		renderInterfaces.put(requestedDepth, null);
		
	}
	
	
}
