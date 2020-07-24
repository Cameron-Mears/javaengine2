package physics.collision;

import java.io.IOException;

import engine.core.Engine;
import engine.core.exceptions.EngineException;
import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import external.org.json.JSONException;
import external.org.json.JSONObject;
import physics.general.Vector2;

public class CollisionLayerManager 
{
	private static CollisionLayerManager instance;
	
	private HashTreeMap<String, CollisionLayer> layers;
	
	private TraverseFunction<CollisionLayer> traverse;
	
	public static CollisionLayerManager getInstance()
	{
		if (instance == null) instance = new CollisionLayerManager();
		
		return instance;
	}
	
	private CollisionLayerManager()
	{
		layers = new HashTreeMap<String, CollisionLayer>();
		
		JSONObject properties;
		try {
			properties = (JSONObject) Engine.getInstance().getProperty("collisionDefaultLayerBounds");
			Vector2 vector = new Vector2(properties.getDouble("origin_x"), properties.getDouble("origin_y"));
			Rectangle rect = new Rectangle(properties.getDouble("width"), properties.getDouble("height"));
			CollisionLayer defaultLayer = new CollisionLayer("default", rect, vector);
			layers.put("default", defaultLayer);
		} catch (JSONException | EngineException e) 
		{
			e.printStackTrace();
			System.out.println("[WARNING][ENGINE_INIT]: The defualt collison layer could not be cretaed");
		}
		
		traverse = new TraverseFunction<CollisionLayer>() {
			
			@Override
			public void apply(CollisionLayer layer) {
				layer.resloveCollisions();
			}
		};
	}
	
	public CollisionLayer getDefaultlayer()
	{
		return layers.get("default");
	}
	
	public void resloveCollisions(String name) throws EngineException
	{
		CollisionLayer layer = layers.get(name);
		if (layer == null) throw new EngineException("The Collision Layer: \"" + name + "\" does not exist");
		layer.resloveCollisions();
	}
	
	public void resloveAllCollisions()
	{
		layers.inOrderTraverse(traverse);
	}
}
