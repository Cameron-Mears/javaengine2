package graphics.layer;

import java.awt.Graphics2D;
import java.util.function.Function;

import engine.core.Engine;
import engine.core.exceptions.EngineException;
import engine.core.instance.EngineInstance;
import engine.core.instance.InstanceID;
import engine.util.EngineRemovable;
import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import graphics.Camera;
import graphics.instance.IGraphics;
import physics.collision.Rectangle;

public class GraphicsLayer implements EngineRemovable
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
	
	private HashTreeMap<Long, GraphicsNode> members;
	private HashTreeMap<Long,GraphicsInstance> renderOrder;
	private Graphics2D g2;
	private TraverseFunction<GraphicsInstance> traverse;
	private RENDER_QUERY_MODE mode;

	private static class GraphicsNode
	{
		InstanceID<EngineInstance> id;
		long requestedDepth;
		long putAtDepth;
	}
	
	@Override
	public boolean equals(Object other)
	{
		return (other instanceof GraphicsLayer)?  ((GraphicsLayer) other).getName().equals(name):false;
	}
	
	public GraphicsLayer(String name) 
	{
		this.name = name;
		this.mode = RENDER_QUERY_MODE.BST;
		renderOrder = new HashTreeMap<Long, GraphicsInstance>();
		members = new HashTreeMap<>();
		traverse = new TraverseFunction<GraphicsInstance>() 
		{

			@Override
			public void apply(GraphicsInstance graphics) 
			{						
				graphics.render(g2, camera);
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
	 * @param eInstance the EngineInstance to add, must implement ten IGraphics EngineComponent
	 * @param depth the desired depth for the instance, if a user create GraphicsInstance exists at that depth, a different depth will be assigned
	 * @return the assigned depth for the IGraphics instance
	 */
	public long addGraphics(EngineInstance eInstance, long depth) 
	{
		if (eInstance.getComponent("IGraphics") == null) throw new EngineException("Instance : " + eInstance.getClass().getName() + " does not implement IGrpahics or is not defined in the EngineInstances.json file");
		
		GraphicsNode node = new GraphicsNode();
		node.id = eInstance.getID();
		node.requestedDepth = depth;
		members.put(node.id.getID(), node);
		System.out.print("In -> ");
		System.out.println(node.id);
		IGraphics instance = (IGraphics) eInstance;
		eInstance.addedToRemovableStruct(this);
		
		Rectangle nullCheck = instance.renderBoundingArea();
		if (nullCheck == null)
		{
			Engine.printWarningMessage("Rendering Area of: " + instance.getClass().getName() + " is null this instance will always be rendered, many of instances may lead to preformace issues", this);
		}
		
		GraphicsInstance g = renderOrder.get(depth);
		if (g != null)
		{
			if (g.isCreatedFromGraphicsLayer())
			{
				node.putAtDepth = depth;
				g.addInterface(node.requestedDepth, instance); //stuff can be overridden
				return depth;
			}
			else
			{
				boolean shouldBreak;
				do 
				{
					depth++;
					g = renderOrder.get(depth);
					shouldBreak = true;
					if (g != null)
					{
						shouldBreak = g.isCreatedFromGraphicsLayer();
					}
					
				} while (!shouldBreak);
				
				if (g == null)
				{
					GraphicsInstance gInstance = new GraphicsInstance(depth, true);
					node.putAtDepth = depth;
					gInstance.addInterface(node.requestedDepth,instance);
					gInstance.assignedToLayer(renderOrder);
					renderOrder.put(depth, gInstance);
					return depth;
				}
				else
				{
					node.putAtDepth = depth;
					g.addInterface(depth, instance);
					return depth;
				}
				
			}
		}
		node.putAtDepth = depth;
		GraphicsInstance gInstance = new GraphicsInstance(depth, true);
		gInstance.addInterface(node.requestedDepth,instance);
		gInstance.assignedToLayer(renderOrder);
		renderOrder.put(depth, gInstance);
		return depth;
	}
	
	
	public boolean addGraphicsInstance(GraphicsInstance g)
	{
		return false;
	}

	@Override
	public void remove(InstanceID<EngineInstance> id) 
	{
		GraphicsNode node = members.get(id.getID());
		if (node == null)
		{
			System.out.print("Error -> ");
			System.out.println(id);
		}
		GraphicsInstance gi = renderOrder.get(node.putAtDepth);
		gi.remove(node.requestedDepth);
		members.put(id.getID(), null);
		System.out.print("Out -> ");
		System.out.println(id);
	}

	public boolean contains(InstanceID<EngineInstance> id) {
		return members.get(id.getID()) != null;
	}
	
	
	
}
