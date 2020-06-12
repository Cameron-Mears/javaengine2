package graphics.layer;

import java.util.LinkedList;
import java.util.function.Function;

import engine.core.instance.InstanceID;
import engine.core.tick.Tickable;
import engine.util.bst.BST;
import graphics.instance.EngineInstance;

public class Layer 
{
	protected String name;
	
	protected BST<InstanceID, Object> members;

	
	public Layer(String name)
	{
		this.name = name;
		members = new BST<InstanceID,Object>();
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
		
	}
	
}
