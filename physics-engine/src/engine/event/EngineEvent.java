package engine.event;

import engine.core.instance.EngineInstance;

public abstract class EngineEvent 
{
	protected EngineInstance instance;
	protected EngineInstance other;
	protected int id;
}
