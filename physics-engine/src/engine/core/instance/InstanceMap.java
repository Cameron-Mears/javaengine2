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
	public final InstanceID<Value> newInstanceID(Value value)
	{
		long id;
		do
		{
			id = Rand.randomLong();
		} while (instanceMap.get(id) != null);
		instanceMap.put(id, value);
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
	
	public Value getInstanceFromID(InstanceID<?> id)
	{
		return instanceMap.get(id.getID());
	}
	
	public boolean removeID(InstanceID<?> id)
	{
		return instanceMap.remove(id.getID()) != null;
	}
	
	public boolean removeID(long id)
	{
		return instanceMap.remove(id) != null;
	}

	public int size() {
		
		return instanceMap.size();
	}

	
	
}
