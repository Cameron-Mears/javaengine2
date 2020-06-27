package physics.collision;

import java.io.IOException;
import java.util.function.Function;

import engine.core.Engine;
import engine.core.exceptions.EngineException;
import engine.util.bst.BST;
import external.org.json.JSONException;
import external.org.json.JSONObject;
import physics.general.Vector2;

public class CollisionLayerManager 
{
	private static CollisionLayerManager instance;
	
	private BST<String, CollisionLayer> layers;
	
	private Function<CollisionLayer, Void> traverse;
	
	public static CollisionLayerManager getInstance()
	{
		if (instance == null) instance = new CollisionLayerManager();
		
		return instance;
	}
	
	private CollisionLayerManager()
	{
		layers = new BST<String, CollisionLayer>();
		
		JSONObject properties;
		try {
			properties = (JSONObject) Engine.getInstance().getProperty("collisionDefaultLayerBounds");
			Vector2 vector = new Vector2(properties.getDouble("origin_x"), properties.getDouble("origin_y"));
			Rectangle rect = new Rectangle(properties.getDouble("width"), properties.getDouble("height"));
			CollisionLayer defaultLayer = new CollisionLayer("default", rect, vector);
			layers.addNode("default", defaultLayer);
		} catch (JSONException | IOException | EngineException e) 
		{
			e.printStackTrace();
			System.out.println("[WARNING][ENGINE_INIT]: The defualt collison layer could not be cretaed");
		}
		
		traverse = new Function<CollisionLayer, Void>() {
			
			@Override
			public Void apply(CollisionLayer layer) {
				layer.resloveCollisions();
				return null;
			}
		};
	}
	
	public CollisionLayer getDefaultlayer()
	{
		return layers.find("default");
	}
	
	public void resloveCollisions(String name) throws EngineException
	{
		CollisionLayer layer = layers.find(name);
		if (layer == null) throw new EngineException("The Collision Layer: \"" + name + "\" does not exist");
		layer.resloveCollisions();
	}
	
	public void resloveAllCollisions()
	{
		layers.inOrderTraverse(traverse);
	}
}
