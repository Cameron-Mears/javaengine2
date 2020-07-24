package engine.core.instance;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import engine.core.Engine;
import engine.core.GameFiles;
import engine.core.JSON_CONSTANTS;
import engine.core.exceptions.EngineException;
import engine.util.tree.HashTreeMap;
import external.org.json.JSONArray;
import external.org.json.JSONObject;
import graphics.instance.IGraphics;
import graphics.instance.InvalidInstanceException;
import graphics.layer.GraphicsLayerManager;

public abstract class EngineInstance 
{
	protected InstanceID id;
	
	protected LinkedList<HashTreeMap<Long,Object>> listmembers;
	
	protected LinkedList<EngineComponent> components;
	
	/**
	 * Provides a list of Intefaces that a EngineInstance Uses
	 */
	public static final HashMap<Class<?>, HashMap<String,Class<?>>> componentMap;
	
	static
	{
		JSONObject obj = Engine.getGameFiles().get("EngineInstances.json");
		JSONArray array = obj.getJSONArray("engineInstances");
		componentMap = new HashMap<Class<?>, HashMap<String,Class<?>>>();
		for (int index = 0; index < array.length(); index ++)
		{
			String clazz = array.getString(index);
			Class<?> rootClass = null;
			try
			{
				rootClass = Class.forName(clazz);
			} catch (Exception e) {}
			
			if (rootClass == null) continue;
			HashMap<String, Class<?>> components = new HashMap<String, Class<?>>();
			if (!EngineInstance.class.isAssignableFrom(rootClass)) continue;
			Class<?> superIterator = rootClass;
			do
			{
				
				Class<?>[] interfaces = superIterator.getInterfaces();
				for (Class<?> interFace : interfaces) 
				{
					if (EngineComponent.class.isAssignableFrom(interFace))
					{
						components.put(interFace.getSimpleName(), interFace);
					}
				}
				superIterator = superIterator.getSuperclass();
				
			}  while (EngineInstance.class.isAssignableFrom(superIterator));
			
			componentMap.put(rootClass, components);
		}
	}
	
	public EngineComponent getComponent(String name)
	{
		try
		{
			return (EngineComponent) componentMap.get(this.getClass()).get(name).cast(this);
		}
		catch (Exception e) {
			System.out.println("sd");
		}
		return null;
	}
	
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof EngineInstance)
		{
			return id.getID() == ((EngineInstance)other).getID().getID();
		}
		return false;
	}
	
	public EngineInstance()
	{
		this.id = InstanceMap.newInstanceID(this);		
	}
	
	public final InstanceID getID()
	{
		return id;
	}	
	
	public boolean addedToTree(HashTreeMap<Long,EngineInstance> tree)
	{
		return false;
	}
	
	public final boolean delete()
	{
		for (HashTreeMap<Long, Object> tree : listmembers) 
		{
			tree.put(id.getID(), null);
		}
		return true;
	}
	
	
	//optional method for class to implement to create an instance from a json file
	public void configureFromJSON(JSONObject object)
	{
		return;
	}
	
	public static final EngineInstance instanceFromJSON(JSONObject object) throws EngineException, InvalidInstanceException
	{	
		
		String className = object.getString(JSON_CONSTANTS.OBJECT_CLASS);
		
		
		Class<?> type;
		try 
		{
			type = Class.forName(className);
		} 
		catch (ClassNotFoundException e) 
		{
			throw new EngineException(e.getMessage());
		}
		
		
		//checks if the class type provided in the JSON file is a subclass of engine instance
		boolean isEngineInstance = EngineInstance.class.isAssignableFrom(type);
		EngineInstance instance;
		if (isEngineInstance)
		{
			Constructor<?> constructor;
			try 
			{
						
				
				Class<?> cls = JSONObject.class;
				JSONObject json = object.getJSONObject("constructor");
				
				constructor = type.getConstructor(cls); //get the constructor with specified arguments
				
				instance = (EngineInstance) constructor.newInstance(json);
				
				String gLayer = object.getString("graphicsLayer");
				long depth = object.getLong("graphicsDepth");
				
				if (gLayer != "none")
					GraphicsLayerManager.getInstance().getLayer(gLayer).addGraphics(instance, depth);
			
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				throw new EngineException(e.getClass().getName() + e.getMessage());
			}
			
			return instance;
		}
		else throw new InvalidInstanceException(className); //is not subclass of EngineInstance throw exception
	}

	

}
