package graphics.layer;

import java.awt.Graphics2D;
import java.util.function.Function;

import engine.core.Engine;
import engine.util.bst.BST;
import graphics.Camera;
import graphics.instance.IGraphics;
import physics.collision.Rectangle;

public class GraphicsLayer 
{
	
	private String name;
	private Camera camera;
	
	/**
	 * Query modes:
	 * 
	 * QUAD_TREE: Rendering will find instances in the camera frame and render them in order from the results from the quad tree query,
	 * this mode will have more overhead as each instances will need to be moved around in the quad tree if they move
	 * 
	 * 
	 * 
	 * BST: all instances in the graphics layer will be checked if in frame will be rendering from lowest to highest depth, will have very low overhead
	 * with insertion and deletion as no instances need to moved around
	 * 
	 * DEPTH_QUAD_TREE: Extends QUAD_TREE process, the query from the quad tree will be rendered in order of graphics depth, this method has
	 * the most overhead.
	 * 
	 * The default mode is BST.
	 */
	public static enum RENDER_QUERY_MODE
	{
		QUAD_TREE,
		BST,
		DEPTH_QUAD_TREE
	}
	
	private BST<Long, Object> members;
	private BST<Long,GraphicsInstance> renderOrder;
	private Graphics2D g2;
	private Function<GraphicsInstance, Void> traverse;
	private RENDER_QUERY_MODE mode;

	public GraphicsLayer(String name) 
	{
		this.name = name;
		this.mode = RENDER_QUERY_MODE.BST;
		renderOrder = new BST<Long, GraphicsInstance>();
		members = new BST<Long, Object>();
		traverse = new Function<GraphicsInstance, Void>() 
		{

			@Override
			public Void apply(GraphicsInstance graphics) 
			{						
				graphics.render(g2, camera);
				return null;
			}
		};
	}
	
	/**
	 * 
	 * @param name the name of the tree
	 *
	 * @param mode the query mode for the graphics layer
	 */
	public GraphicsLayer(String name, RENDER_QUERY_MODE mode)
	{
		this(name);
		this.mode = mode;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setCamera(Camera camera)
	{
		this.camera = camera;
	}
	
	public void render(Camera camera, Graphics2D g2)
	{
		Camera current = this.camera;
		this.camera = camera;
		render(g2);
		this.camera = current;
	}
	
	public void render(Graphics2D g2)
	{
		this.g2 = g2;
		
		
		switch (mode) 
		{
			case BST:
				renderOrder.inOrderTraverse(traverse);
				break;
			case QUAD_TREE:
				break;
			case DEPTH_QUAD_TREE:
				break;
		
		default:
			throw new IllegalArgumentException("Unexpected value: " + mode);
		}
	}

	
	/**
	 * 
	 * @param instance the IGraphics instance to add
	 * @param depth the desired depth for the instance, if a user create GraphicsInstance exists at that depth, a different depth will be assigned
	 * @return the assigned depth for the IGraphics instance
	 */
	public long addIGrpahics(IGraphics instance, long depth) 
	{
		Rectangle nullCheck = instance.renderBoundingArea();
		if (nullCheck == null)
		{
			Engine.printWarningMessage("Rendering Area of: " + instance.getClass().getName() + " is null this instance will always be rendered, many of instances may lead to preformace issues", this);
		}
		
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
