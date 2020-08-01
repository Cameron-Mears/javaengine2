package engine.core.instance;

import java.util.concurrent.ConcurrentHashMap;

import engine.core.random.Rand;

public final class InstanceMap<Value>
{
	private final ConcurrentHashMap<Long, Value> instanceMap = new ConcurrentHashMap<Long, Value>();
	
	/**
	 * 
	 * @param instance the instance to add the map
	 * @return an instance id pointing to this instance
	 */
	public final InstanceID<Value> newInstanceID()
	{
		long id;
		do
		{
			id = Rand.randomLong();
		} while (instanceMap.get(id) != null);
		
		return new InstanceID<Value>(id,this);
	}
	
	/**
	 * 
	 * @param id the id of the instance
	 * @return the instance with that id
	 */
	public Value getInstanceFromID(long id)
	{
		return instanceMap.get(id);
	}
	
	public Value getInstanceFromID(InstanceID id)
	{
		return instanceMap.get(id.getID());
	}
	
	public void removeID(InstanceID id)
	{
		instanceMap.put(id.getID(), null);
	}

	
	
}
