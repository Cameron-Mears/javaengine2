package graphics.layer;

import engine.core.instance.EngineInstance;
import engine.core.instance.InstanceID;
import engine.util.bst.BST;
import engine.util.tree.HashTreeMap;

public class Layer 
{
	protected String name;
	
	protected HashTreeMap<Long, Object> members;

	
	public Layer(String name)
	{
		this.name = name;
		members = new HashTreeMap<Long,Object>();
	}
	
	
	public String getName()
	{
		return name;
	}
	
	public int getInstanceCount()
	{
		return 0;
	}
	
	public void addInstance(EngineInstance eInstance)
	{
		
		members.put(eInstance.getID().getID(), eInstance);
	}
	
}
