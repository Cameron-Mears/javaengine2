package engine.util.json;

import java.awt.geom.AffineTransform;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import external.org.json.JSONArray;
import external.org.json.JSONException;
import external.org.json.JSONObject;

/**
 * 
 * Class for converting java objects into JSONObjects
 * 
 * @author Cameron
 * By default this only will only the write the values of public and package private fields,
 * will not write values of super classes or static fields. All fields can be written by calling the appropriate function
 * 
 */
public class JSONObjectWriter 
{
	
	private boolean recurseSuperClasses;

	public JSONObjectWriter(boolean recurSuperClasses) 
	{
		this.recurseSuperClasses = recurSuperClasses;
	};
	
	public JSONObjectWriter()
	{
		this.recurseSuperClasses = true;
	}
	
	
	/**
	 * 
	 * @param object the value to be tested
	 * @return if the value is a base level json component
	 */
	private boolean isValidType(Object object)
	{
		return object == null ||
				object instanceof Number || 
				object instanceof String || 
				object instanceof Boolean || 
				object instanceof JSONObject ||
				object instanceof JSONArray;
	}
	
	private boolean isArrayType(Object object)
	{
		return object instanceof Collection<?> || object instanceof Array;
	}
	
	/**
	 * 
	 * @param mods, the modifiers of the field
	 * @return returns false if the any of the modifiers are specified as do not write, true if all the the fields are write
	 */
	private boolean shouldWriteField(int mods)
	{
		return Modifier.isPublic(mods);
	}
	
	public JSONObject toJSONObject(Object javaObject) throws IllegalArgumentException, IllegalAccessException
	{
		if (javaObject == null) throw new JSONException("null object");
		
		JSONObject jsonObject = new JSONObject();
		Class<?> clazz = javaObject.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		do
		{
			//iterate through each field check if the field needs to be converted to as json object its self
			for (Field field : fields) 
			{
				String name = field.getName();
				int mods = field.getModifiers();				
				if (!shouldWriteField(mods)) continue;
				field.setAccessible(true);
				Object value = field.get(javaObject);
				if (isValidType(value))
				{
					jsonObject.put(name, value);
					continue;
				}
				if (isArrayType(value))
				{
					Object a = (value instanceof Collection<?>)? ((Collection<?>) value).toArray() : value;
					JSONArray arr = parseArray((value instanceof Collection<?>)? ((Collection<?>) value).toArray() : value);
					jsonObject.put(name, arr);
					continue;
				}
				if (value instanceof Map<?,?>)
				{
					Map<?,?> map = (Map<?,?>) value;
					JSONArray arr = new JSONArray();
					for (Entry<?, ?> e : map.entrySet()) 
					{
						Object key = e.getKey();
						Object val = e.getValue();
						if (key == null) throw new JSONException("Null key.");
						JSONObject entry = new JSONObject();
						
						if (isValidType(key)) entry.put("key", key);
						else if (isArrayType(key)) entry.put("key", parseArray((key instanceof Collection<?>)? ((Collection<?>) key).toArray() : key));
						else entry.put("key", toJSONObject(key));
						
						if (isValidType(val)) entry.put("value", val);
						else if (isArrayType(val)) entry.put("value", parseArray((val instanceof Collection<?>)? ((Collection<?>) val).toArray() : val));
						else entry.put("value", toJSONObject(val));
						arr.put(entry);						
						
					}
					jsonObject.put(field.getName(), arr);
				}
				else //field is java object recurse
				{
					JSONObject obj = toJSONObject(value);
					jsonObject.put(name, obj);
					continue;
				}
				
			}
			clazz = clazz.getSuperclass();
		}
		//if we are recurring super classes loop until we hit object.class
		while (!clazz.equals(Object.class) && recurseSuperClasses);
		
		return jsonObject;
	}
	
	/**
	 * 
	 * @param array the array to be parsed
	 * @return a JSON representation of the array
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private JSONArray parseArray(Object array) throws IllegalArgumentException, IllegalAccessException
	{
		if (array == null) throw new JSONException("nullptr");
		if (!(array instanceof Array || array.getClass().isArray())) throw new JSONException("not an array");
		JSONArray ret = new JSONArray();
		int len = Array.getLength(array);
		
		for (int index = 0; index < len; index++) 
		{
			Object temp = Array.get(array, index);
			
			//is a root level JSONObject we can just add to our JSONArray
			if (isValidType(temp))
			{
				ret.put(temp);
			}
			else //not a root level object we will need to convert the objects to JSON
			{
				if (temp instanceof Array) //multi dimension array, parse the sub array
				{
					JSONArray subArray = parseArray(temp);
					ret.put(subArray);
				}
				else //Some other java object convert it to json
				{
					JSONObject jsonObject = toJSONObject(temp);
					ret.put(jsonObject);
				}
			}
		}
		return ret;
	}
	
	
}
