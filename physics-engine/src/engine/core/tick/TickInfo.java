package engine.core.tick;

public class TickInfo 
{
	public String groupName;
	public double delta;
	public long deltaNS;
	
	@Override
	public String toString()
	{
		return "TickInfo: delta: " + Double.toString(delta) + " group: " + groupName;
	}
	
}
