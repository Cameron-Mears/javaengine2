package graphics.layer;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.function.Function;


import engine.util.bst.BST;

public class GraphicsLayerManager 
{
	private BST<String,GraphicsLayer> graphicsLayers;
	
	private Function<GraphicsLayer, Void> traverse;
	private Graphics2D g2;
	
	private static GraphicsLayerManager instance;
	
	public static synchronized GraphicsLayerManager getInstance()
	{
		if (instance == null)
		{
			instance =  new GraphicsLayerManager();
		}
		return instance;
	}
	
	
	
	private GraphicsLayerManager()
	{
		graphicsLayers = new BST<String, GraphicsLayer>();
		
		GraphicsLayer defaultlayer = new GraphicsLayer("default");
		graphicsLayers.addNode(defaultlayer.getName(), defaultlayer);
		
		traverse = new Function<GraphicsLayer, Void>() {

			@Override
			public Void apply(GraphicsLayer t) 
			{
				t.render(g2);
				return null;
			}
		};
	}
	
	public GraphicsLayer getLayer(String name)
	{
		return graphicsLayers.find(name);
	}
	
	
	
	public void render(Graphics2D g2)
	{
		this.g2 = g2;
		
		if (graphicsLayers.getRoot() != null)
		{
			graphicsLayers.inOrderTraverse(graphicsLayers.getRoot(), traverse);
		}
	}
}
