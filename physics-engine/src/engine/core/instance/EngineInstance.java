package engine.core.instance;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import engine.core.Engine;
import engine.core.GameFiles;
import engine.core.JSON_CONSTANTS;
import engine.core.exceptions.EngineException;
import engine.util.EngineRemovable;
import engine.util.tree.HashTreeMap;
import external.org.json.JSONArray;
import external.org.json.JSONObject;
import graphics.instance.IGraphics;
import graphics.instance.InvalidInstanceException;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;

public abstract class EngineInstance 
{
	protected InstanceID<EngineInstance> id;
	protected boolean alive = true;
	
	private static LinkedList<EngineInstance> toFlush = new LinkedList<EngineInstance>();
	
	protected LinkedList<EngineRemovable> listmembers;
	
	protected LinkedList<EngineComponent> components;
	protected final static InstanceMap<EngineInstance> instanceMap = new InstanceMap<EngineInstance>();
	/**
	 * Provides a list of Intefaces that a EngineInstance Uses
	 */
	public static final HashMap<Class<?>, HashMap<String,Class<?>>> componentMap;
	
	/**
	 * loads in all engine instances listed in EngineInstances.json file
	 * and sets all the EngineComponent interfaces that that class implements into a hashmap
	 * so that certain class interfaces can be accessed abstractly eg. EngineInstance.getComponent("Tickable");
	 */
	static
	{
		JSONObject obj = Engine.getGameFiles().get("EngineInstances.json");
		JSONArray array = obj.getJSONArray("engineInstances");
		componentMap = new HashMap<Class<?>, HashMap<String,Class<?>>>();
		
		//iterate through all the classes listed in EngineInstances.json
		for (int index = 0; index < array.length(); index ++)
		{
			String clazz = array.getString(index);
			Class<?> rootClass = null;
			try //check if is valid class
			{
				rootClass = Class.forName(clazz);
			} catch (Exception e) {
				//Engine.printWarningMessage("Could find class -> " + clazz, EngineInstance.class);
				}
			
			if (rootClass == null) continue;
			HashMap<String, Class<?>> components = new HashMap<String, Class<?>>();
			
			//check if the class listed is an type of EngineInstance
			if (!EngineInstance.class.isAssignableFrom(rootClass)) continue;
			Class<?> superIterator = rootClass; //we may need to iterate super classes if the base class does not implement all interfaces eg. Player extends Entity
			
			do
			{
				
				Class<?>[] interfaces = superIterator.getInterfaces(); //load the classes interfaces
				for (Class<?> interFace : interfaces) 
				{
					if (EngineComponent.class.isAssignableFrom(interFace)) //check if that interface is defined as an EngineComponent
					{
						components.put(interFace.getSimpleName(), interFace); //add the simple name eg. Tickable vs engine.core.tick.Tickable and the class for fast casts
					}
				}
				superIterator = superIterator.getSuperclass(); //iterate to the next superclass
				
			}  while (EngineInstance.class.isAssignableFrom(superIterator)); 
			
			componentMap.put(rootClass, components); //add the hashmap of this class to the all instance hashmap
		}
	}
	
	/**
	 * 
	 * @param name the simple name of the EngineComponent interface eg "Collidable"
	 * @return the requetsed interface, null if the class never implements it
	 */
	public EngineComponent getComponent(String name)
	{
		try
		{
			return (EngineComponent) componentMap.get(this.getClass()).get(name).cast(this);
		}
		catch (Exception e) {
			System.out.println("no such interface of " + name + " in type " + this.getClass().getName()) ;
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
		this.id = instanceMap.newInstanceID(this);
		listmembers = new LinkedList<>();
	}
	
	public final InstanceID<EngineInstance> getID()
	{
		return id;
	}	
	
	public boolean addedToRemovableStruct(EngineRemovable struct)
	{
		if (listmembers.contains(struct)) System.out.println("ok");
		return listmembers.add(struct);
	}
	
	public final boolean delete()
	{
		beforeDelete();
		toFlush.add(this);
		alive = false;
		return true;
	}
	
	public static boolean flush()
	{
		for (EngineInstance instance : toFlush) 
		{
			synchronized (instance.listmembers) {
				while (!instance.listmembers.isEmpty())
				{
					EngineRemovable struct = instance.listmembers.poll();
					synchronized (struct) {
						struct.remove(instance.id);	
					}
				}
				instance.id.delete();
			}
		}
		return true;
	}
	
	
	protected void beforeDelete()
	{
		
	}
	
	public boolean isAlive()
	{
		return alive;
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
