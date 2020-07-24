package engine.core.tick;

import engine.core.exceptions.EngineException;
import engine.core.instance.EngineInstance;
import engine.core.random.Rand;
import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import external.org.json.JSONArray;
import external.org.json.JSONException;
import graphics.instance.InvalidInstanceException;


public class TickableGroup implements Tickable
{
	private boolean enabled;
	private TickInfo info;
	private TraverseFunction<Tickable> function;
	private HashTreeMap<Long, Tickable> members;
	private String name;
	
	public TickableGroup(String name) 
	{
		this.name = name;
		this.members = new HashTreeMap<Long, Tickable>();
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
		if (size % 1000 == 0)System.out.println(members.size());
		this.info = info;
		members.inOrderTraverse(function);
	}
		
	public boolean addInstancesFromJSON(JSONArray instances) throws EngineException, JSONException, InvalidInstanceException
	{
		try
		{
		
		for (int index = 0; index < instances.length(); index++) 
		{
			EngineInstance instance = EngineInstance.instanceFromJSON(instances.getJSONObject(index));
			addTickable(instance);
		}
	}
	catch (Exception e) {};
		
		return false;
	}

	public void addTickable(EngineInstance tickable)
	{
		if (tickable.getComponent("Tickable") == null)
		{
			System.out.println(tickable.getClass());
			//System.out.println("must be tickable");
		}
		members.put(Rand.randomLong(), (Tickable)tickable);
	}

	@Override
	public void onTick(TickInfo info) 
	{
		System.out.println(members.size());
		tick(info);
	}



	public String getName() {
		
		return name;
	}
		
	
}
