package engine.util.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import external.org.json.JSONArray;
import external.org.json.JSONException;
import external.org.json.JSONObject;

public class JSONObjectReader 
{
	public Object toJavaObject(JSONObject obj, Class<?> objectClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Object instance =  objectClass.getConstructor().newInstance();
		toJavaObject(obj, instance);
		return instance;		
	}
	
	public void toJavaObject(JSONObject obj, Object instance) throws JSONException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getFields();
		
		for (Field field : fields) 
		{
			Object value = null;
			String name = field.getName();
			try
			{
				value = obj.get(name);
			}
			catch (JSONException e)
			{
				throw e;
			}
			
			if (value.equals(JSONObject.NULL))
			{
				field.set(instance, null);
				continue;
			}
			Class<?> valueClass = value.getClass();
			
			Class<?> fieldClass = field.getDeclaringClass();
			
			
			if (valueClass.isAssignableFrom(fieldClass)) //object can be put, to field, class is compatatible
			{
				Object casted = fieldClass.cast(value);
				field.set(instance, casted);
			}
			else
			{
				if (value instanceof JSONObject)
				{
					Object object = toJavaObject((JSONObject) value, valueClass);
					field.set(instance,object);
					
				} 
				else if (value instanceof JSONArray)
				{
					JSONArray array = (JSONArray) value;
					if (field.getDeclaringClass().isAssignableFrom(Map.class)) //json array is listed as map
					{
						Map<?,?> map = (Map<?, ?>) field.get(obj); //get the map object from the class
						if (map == null) throw new JSONException("Null Map.");
						for (int index = 0; index < array.length(); index++) 
						{
							JSONObject object = array.getJSONObject(index);
							if (object == null) throw new JSONException("Null Entry.");
							Object key = object.get("key"), val =  object.get("value");
							
						
						}
					}
				}
			}
		}
	}
}
