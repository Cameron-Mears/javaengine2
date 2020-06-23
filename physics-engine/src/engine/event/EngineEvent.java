package engine.event;

import engine.core.instance.EngineInstance;

public abstract class EngineEvent 
{
	protected EngineInstance instance;
	protected EngineInstance other;
	protected TYPE type;
	
	public static enum TYPE
	{
		COLLISION,
		TIMER
	}
	
	public EngineEvent(EngineInstance instance, EngineInstance other, TYPE type)
	{
		this.instance = instance;
		this.other = other;
		this.type = type;
	}
	
	public TYPE getType()
	{
		return type;
	}
}
