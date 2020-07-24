package engine.core.instance;

public final class InstanceID 
{
	private long id;
	
	public InstanceID(long id)
	{
		this.id = id;
	}
	
	public long getID()
	{
		return id;
	}
	
	public void delete()
	{
		InstanceMap.removeID(this);
	}
}
