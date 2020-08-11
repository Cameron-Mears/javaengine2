package engine.core.instance;

public final class InstanceID<Value> implements Comparable<InstanceID<?>>
{
	private final long id;
	private InstanceMap<Value> map;
	
	@Override
	public boolean equals(Object anObject)
	{
		return (anObject instanceof InstanceID)? ((InstanceID<?>)anObject).id == id: false;
	}
	
	@Override
	public String toString()
	{
		return "InstanceID<id=\""  + Long.toString(id) + "\">";
	}
	
	public InstanceID(long id, InstanceMap<Value> map)
	{
		this.map = map;
		this.id = id;
	}
	
	public long getID()
	{
		return id;
	}
	
	public void delete()
	{
		map.removeID(this);
	}

	@Override
	public int compareTo(InstanceID<?> o) {
		return (int)(o.id - id);
	}
}
