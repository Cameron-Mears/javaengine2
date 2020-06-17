package graphics.layer;

import engine.core.instance.EngineInstance;
import engine.core.instance.InstanceID;
import engine.util.bst.BST;

public class Layer 
{
	protected String name;
	
	protected BST<Long, Object> members;

	
	public Layer(String name)
	{
		this.name = name;
		members = new BST<Long,Object>();
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
		
		members.addNode(eInstance.getID().getID(), eInstance);
	}
	
}
