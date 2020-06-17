package engine.core.instance;

public final class InstanceID 
{
	private long id;
	
	public InstanceID()
	{
		id = (long) (Math.random() * Long.MAX_VALUE);
	}
	
	public long getID()
	{
		return id;
	}
}
