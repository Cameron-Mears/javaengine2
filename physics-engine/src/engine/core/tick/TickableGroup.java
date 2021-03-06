package engine.core.tick;

import engine.core.Engine;
import engine.core.exceptions.EngineException;
import engine.core.instance.EngineInstance;
import engine.core.instance.InstanceID;
import engine.core.random.Rand;
import engine.util.EngineRemovable;
import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import external.org.json.JSONArray;
import external.org.json.JSONException;
import external.org.json.JSONObject;
import graphics.instance.InvalidInstanceException;
import graphics.viewer.Display;


public class TickableGroup implements Tickable, EngineRemovable
{
	private boolean enabled;
	private TickInfo info;
	private TraverseFunction<Tickable> function;
	private HashTreeMap<InstanceID<EngineInstance>, Tickable> members;
	private String name;
	
	public TickableGroup(String name) 
	{
		this.name = name;
		this.members = new HashTreeMap<>();
		function = new TraverseFunction<Tickable>() 
		{
			
			@Override
			public void apply(Tickable tickable) {
				
				Tickable t = (Tickable) tickable;
				t.onTick(info);
				
				
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
		int size = members.size();
		this.info = info;
		this.info.groupName = "default";
		members.inOrderTraverse(function);
	}
		
	public boolean addInstancesFromJSON(JSONArray instances) throws EngineException, JSONException, InvalidInstanceException
	{
		try
		{
		
		for (int index = 0; index < instances.length(); index++) 
		{
			JSONObject obj = instances.getJSONObject(index);
			try
			{
				EngineInstance instance = EngineInstance.instanceFromJSON(obj);
				addTickable(instance);
			}
			catch (Exception e) {
				Engine.printWarningMessage("Error loading instance -> " + obj.toString(), this);
			}
		}
	}
	catch (Exception e) {e.printStackTrace();};
		
		return false;
	}

	public void addTickable(EngineInstance tickable)
	{
		if (tickable.getComponent("Tickable") == null)
		{
			System.out.print(tickable.getClass());
			System.out.println(" -> Must be tickable");
		}
		members.put(tickable.getID(), (Tickable)tickable);
		tickable.addedToRemovableStruct(this);
	}

	@Override
	public void onTick(TickInfo info) 
	{
		tick(info);
	}



	public String getName() {
		
		return name;
	}



	@Override
	public void remove(InstanceID<EngineInstance> id) 
	{		
		members.put(id, null);
	}
		
	
}
