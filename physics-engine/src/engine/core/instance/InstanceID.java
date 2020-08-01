package engine.core.instance;

public final class InstanceID<Value>
{
	private long id;
	private InstanceMap<Value> map;
	
	@Override
	public boolean equals(Object anObject)
	{
		return (anObject instanceof InstanceID)? ((InstanceID)anObject).id == id: false;
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
}
