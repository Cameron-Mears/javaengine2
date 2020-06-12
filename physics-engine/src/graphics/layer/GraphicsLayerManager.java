package graphics.layer;

import java.util.HashMap;

import engine.util.bst.BST;

public class GraphicsLayerManager 
{
	private BST<String,GraphicsLayer> graphicsLayers;
	
	
	public GraphicsLayer getLayer(String name)
	{
		return graphicsLayers.find(name);
	}
	
	
	
	
	public void render()
	{
		
	}
}
