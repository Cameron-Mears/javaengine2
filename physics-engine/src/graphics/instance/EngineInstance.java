package graphics.instance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.LinkedList;

import engine.core.JSON_CONSTANTS;
import engine.core.exceptions.EngineException;
import engine.core.instance.InstanceID;
import engine.util.bst.BST;
import engine.util.string.StringHexConverter;
import external.org.json.JSONArray;
import external.org.json.JSONObject;

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
	
	public static EngineInstance instanceFromJSON(JSONObject object) throws EngineException, InvalidInstanceException
	{
		String test = null;
		byte[] a = null;
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream( baos );
	        oos.writeObject(109);		
	        oos.close();
	        a = baos.toByteArray();
	        
	        FileOutputStream s = new FileOutputStream(new File("C:\\Users\\camer\\OneDrive\\Documents\\test.txt"));
	        s.write(a);
	        s.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
		
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
				Class<?> types[] = new Class<?>[constructorParamTypes.length()];
				Object[] values = new Object[constructorParamTypes.length()];
				
				ObjectInputStream objectStream;
				
				
				
				for (int index = 0; index < types.length; index++) 
				{
					String typeString = constructorParamTypes.getString(index);
					types[index] = Class.forName(typeString);
					
					String objectRaw = constructorParamValues.getString(index);
					byte[] objectDecoded = StringHexConverter.toByteArray(objectRaw);
					objectStream = new ObjectInputStream(new ByteArrayInputStream(objectDecoded));
					values[index] = objectStream.readObject();
					objectStream.close();
				}		
				
				constructor = type.getConstructor(types); //get the constructor with specified arguments
				
				
				instance = (EngineInstance) constructor.newInstance(values);
				
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
