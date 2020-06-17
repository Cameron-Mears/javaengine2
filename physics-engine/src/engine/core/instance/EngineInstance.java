package engine.core.instance;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

import engine.core.JSON_CONSTANTS;
import engine.core.exceptions.EngineException;
import engine.util.bst.BST;
import external.org.json.JSONArray;
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
			Constructor<?>constructor;
			try 
			{
				
				JSONArray constructorParamTypes = object.getJSONArray(JSON_CONSTANTS.OBJECT_CONSTRUCTORS_TYPES);
				JSONArray constructorParamValues = object.getJSONArray(JSON_CONSTANTS.OBJECT_CONSTRUCTORS_PARAMS);
				
				//create the array to hold class types as Class
				Class<?> types[] = null;
				Object[] values = null;
				
				
				String root = System.getProperty("user.dir") + "\\config\\levels\\level1\\objects\\";
				if (constructorParamTypes.length() > 0)
				{
					types = new Class<?>[constructorParamTypes.length()];
					values = new Object[constructorParamTypes.length()];
					root += constructorParamValues.getString(0);
					root += object.getString("folderID") + "\\";
					
					for (int index = 0; index < types.length; index++) 
					{
						File file = new File(root + Integer.toString(index+1) + ".txt");
						//add the type to the array to find the correct constructor
						String typeString = constructorParamTypes.getString(index);
						types[index] = Class.forName(typeString);
						
						FileInputStream fis = new FileInputStream(file);
						ObjectInputStream ois = new ObjectInputStream(fis);
						values[index] = ois.readObject();
						ois.close();
					}		
				}
				constructor = type.getConstructor(types); //get the constructor with specified arguments
				
				
				instance = (EngineInstance) constructor.newInstance(values);
				
				String gLayer = object.getString("graphicsLayer");
				long depth = object.getLong("graphicsDepth");
				
				if (gLayer != "none")
					GraphicsLayerManager.getInstance().getLayer(gLayer).addIGrpahics((IGraphics)instance, depth);
			
			} 
			catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException | IOException e) 
			{
				e.printStackTrace();
				throw new EngineException(e.getMessage());
			}
			
			//instance.configureFromJSON(object.getJSONObject(JSON_CONSTANTS.OBJECT_SUBCLASS_INFO));
			return instance;
		}
		else throw new InvalidInstanceException(className); //is not subclass of EngineInstance throw exception
	}

	

}
