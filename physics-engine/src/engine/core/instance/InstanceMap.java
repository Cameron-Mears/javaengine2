package engine.core.instance;

import java.util.concurrent.ConcurrentHashMap;

import engine.core.random.Rand;

public final class InstanceMap 
{
	private final static ConcurrentHashMap<Long, EngineInstance> instanceMap = new ConcurrentHashMap<Long, EngineInstance>();
	
	/**
	 * 
	 * @param instance the instance to add the map
	 * @return an instance id pointing to this instance
	 */
	public static InstanceID newInstanceID(EngineInstance instance)
	{
		long id;
		do
		{
			id = Rand.randomLong();
		} while (instanceMap.get(id) != null);
		
		return new InstanceID(id);
	}
	
	/**
	 * 
	 * @param id the id of the instance
	 * @return the instance with that id
	 */
	public static EngineInstance getInstanceFromID(long id)
	{
		return instanceMap.get(id);
	}
	
	public static EngineInstance getInstanceFromID(InstanceID id)
	{
		return instanceMap.get(id.getID());
	}
	
	static void removeID(InstanceID id)
	{
		instanceMap.put(id.getID(), null);
	}
	
	
}
