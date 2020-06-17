package engine.core.tick;

import java.util.function.Function;

import engine.core.exceptions.EngineException;
import engine.core.instance.EngineInstance;
import external.org.json.JSONArray;
import external.org.json.JSONException;
import graphics.instance.InvalidInstanceException;
import graphics.layer.Layer;


public class TickableGroup extends Layer
{
	private boolean enabled;
	private TickInfo info;
	private Function<Object, Void> function;
	
	
	
	public TickableGroup(String name) 
	{
		super(name);
		
		function = new Function<Object, Void>() 
		{
			
			@Override
			public Void apply(Object tickable) {
				
				Tickable t = (Tickable) tickable;
				
				t.onTick(info);
				return null;
			}
		};
		
		enabled = true;
	}

	
	
	public void disalbe()
	{
		this.enabled = false;
	}
	
	public void enable() 
	{
		this.enabled = true;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void tick(TickInfo info)
	{
		this.info = info;
		
		if (members.getRoot() != null)
			
			members.inOrderTraverse(members.getRoot(), function);
	}
		
	public boolean addInstancesFromJSON(JSONArray instances) throws EngineException, JSONException, InvalidInstanceException
	{
		
		for (int index = 0; index < instances.length(); index++) 
		{
			EngineInstance instance = EngineInstance.instanceFromJSON(instances.getJSONObject(index));
			
			members.addNode(instance.getID().getID(), instance);
		}
		
		return false;
	}
		
	
}
