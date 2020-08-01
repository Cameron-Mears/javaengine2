package graphics.layer;

import java.awt.Graphics2D;
import java.util.HashMap;

import engine.core.exceptions.EngineException;
import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import graphics.Camera;
import graphics.instance.IGraphics;

public class GraphicsLayerManager 
{
	private HashTreeMap<Long,GraphicsLayer> graphicsLayers;
	private HashTreeMap<Long, Camera> cameras;
	private HashMap<String, Integer> cameramap;
	private HashMap<String, GraphicsLayerNode> layerMap;
	private TraverseFunction<GraphicsLayer> traverse;
	private TraverseFunction<Camera> cameraTraverse;
	private Graphics2D g2;
	
	private IGraphics renderer = null;
	
	private static GraphicsLayerManager instance;
	
	private static class GraphicsLayerNode
	{
		GraphicsLayer layer;
		long depth;
		
		public GraphicsLayerNode(GraphicsLayer layer, long depth)
		{
			this.layer = layer;
			this.depth = depth;
		}
	}
	
	public void setRenderer(IGraphics renderer)
	{
		this.renderer= renderer;
	}
	
	public HashTreeMap<Long, GraphicsLayer> getAllLayers()
	{
		return graphicsLayers;
	}
	
	public static GraphicsLayerManager getInstance()
	{
		if (instance == null)
		{
			instance =  new GraphicsLayerManager();
		}
		return instance;
	}
	
	public void addCamera(Camera camera, int depth)
	{
		
	}
	
	private GraphicsLayerManager()
	{
		graphicsLayers = new HashTreeMap<Long, GraphicsLayer>();
		cameras = new HashTreeMap<Long, Camera>();
		cameramap = new HashMap<String, Integer>();
		layerMap = new HashMap<String, GraphicsLayerNode>();
		GraphicsLayer defaultlayer = new GraphicsLayer("default");
		GraphicsLayerNode node = new GraphicsLayerNode(defaultlayer, 0L);
		layerMap.put(defaultlayer.getName(), node);
		graphicsLayers.put(0L, defaultlayer);
		
		traverse = new TraverseFunction<GraphicsLayer>() {

			@Override
			public void apply(GraphicsLayer t) 
			{
				t.render(g2);
			}
		};
	
		
		cameraTraverse = new TraverseFunction<Camera>() 
		{
			@Override
			public void apply(Camera camera)
			{				

			}
		};
	}
	
	/**
	 * 
	 * @param name the layer depth to change
	 * @param newDepth the depth to set the graphics
	 * @return the old depth of the layer
	 * @throws EngineException if the layer does not exist, or that graphicsDepth is in use
	 */
	public long setLayerDepth(String name, long newDepth) throws EngineException
	{
		GraphicsLayerNode node = layerMap.get(name);
		if (node == null) throw new EngineException("The Graphics Layer [\"" + name + "\"] does not exist");
		else
		{
			long currentDepth = node.depth;
			graphicsLayers.put(currentDepth, null);
			if (graphicsLayers.get(newDepth) != null) throw new EngineException("The GraphicsLayer depth " + Long.toString(newDepth) + " is already in use.");
			
			graphicsLayers.put(newDepth, node.layer);
			node.depth = newDepth;
			
			
		}
		return newDepth;
		
	}
	
	public GraphicsLayer getLayer(String name)
	{
		GraphicsLayerNode node = layerMap.get(name);
		if (node == null) return null;
		else return node.layer;
	}
	
	/**
	 * 
	 * @param layer
	 * @param depth
	 * @throws EngineException if a layer with the same name exists or a graphics layer is at that depth
	 */
	public void addLayer(GraphicsLayer layer, long depth) throws EngineException
	{
		synchronized (layer) 
		{
			String name = layer.getName();
			if (layerMap.get(name) != null) throw new EngineException("Graphicslayer [\"" + layer.getName() + "\"] already exists");
			if (graphicsLayers.get(depth) != null) throw new EngineException("A GraphicsLayer already exists at depth: " + Long.toString(depth));
			GraphicsLayerNode node = new GraphicsLayerNode(layer, depth);
			layerMap.put(name, node);
			graphicsLayers.put(depth, layer);
		}
		
	}
	
	public void render(Graphics2D g2)
	{
		if (renderer == null)
		{
			this.g2 = g2;
			graphicsLayers.inOrderTraverse(traverse);
		}
		else
		{
			renderer.render(g2);
		}
	}
}
