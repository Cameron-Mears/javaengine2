package engine.core.instance;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import engine.core.JSON_CONSTANTS;
import engine.core.exceptions.EngineException;
import engine.util.bst.BST;
import external.org.json.JSONObject;
import graphics.instance.IGraphics;
import graphics.instance.InvalidInstanceException;
import graphics.layer.GraphicsLayerManager;

public abstract class EngineInstance 
{
	protected InstanceID id;
	
	protected LinkedList<BST<Long,Object>> listmembers;
	
	
	public EngineInstance()
	{
		this.id = new InstanceID();
	}
	
	public final InstanceID getID()
	{
		return id;
	}	
	
	public boolean addedToTree(BST<InstanceID,EngineInstance> tree)
	{
		return false;
	}
	
	public final boolean remove()
	{
		for (BST<Long, Object> bst : listmembers) 
		{
			bst.deleteRec(bst.getRoot(), this.id.getID());
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
					GraphicsLayerManager.getInstance().getLayer(gLayer).addIGrpahics((IGraphics)instance, depth);
			
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
