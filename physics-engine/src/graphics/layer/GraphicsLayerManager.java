package graphics.layer;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.function.Function;

import engine.core.exceptions.EngineException;
import engine.util.bst.BST;
import graphics.Camera;

public class GraphicsLayerManager 
{
	private BST<Long,GraphicsLayer> graphicsLayers;
	private BST<Long, Camera> cameras;
	private HashMap<String, Integer> cameramap;
	private HashMap<String, GraphicsLayerNode> layerMap;
	private Function<GraphicsLayer, Void> traverse;
	private Function<Camera, Void> cameraTraverse;
	private Graphics2D g2;
	
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
	
	
	public static synchronized GraphicsLayerManager getInstance()
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
		graphicsLayers = new BST<Long, GraphicsLayer>();
		cameras = new BST<Long, Camera>();
		cameramap = new HashMap<String, Integer>();
		layerMap = new HashMap<String, GraphicsLayerNode>();
		GraphicsLayer defaultlayer = new GraphicsLayer("default");
		GraphicsLayerNode node = new GraphicsLayerNode(defaultlayer, 0L);
		layerMap.put(defaultlayer.getName(), node);
		graphicsLayers.addNode(0L, defaultlayer);
		
		traverse = new Function<GraphicsLayer, Void>() {

			@Override
			public Void apply(GraphicsLayer t) 
			{
				t.render(g2);
				return null;
			}
		};
	
		
		cameraTraverse = new Function<Camera, Void>() 
		{
			@Override
			public Void apply(Camera camera)
			{				
				return null;
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
			graphicsLayers.remove(currentDepth);
			if (graphicsLayers.find(newDepth) != null) throw new EngineException("The GraphicsLayer depth " + Long.toString(newDepth) + " is already in use.");
			
			graphicsLayers.addNode(newDepth, node.layer);
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
		String name = layer.getName();
		if (layerMap.get(name) != null) throw new EngineException("Graphicslayer [\"" + layer.getName() + "\"] already exists");
		if (graphicsLayers.find(depth) != null) throw new EngineException("A GraphicsLayer already exists at depth: " + Long.toString(depth));
		GraphicsLayerNode node = new GraphicsLayerNode(layer, depth);
		layerMap.put(name, node);
		graphicsLayers.addNode(depth, layer);
		
	}
	
	public void render(Graphics2D g2)
	{
		this.g2 = g2;
		graphicsLayers.inOrderTraverse(traverse);
	}
}
