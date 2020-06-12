package engine.event;

import graphics.instance.EngineInstance;

public abstract class EngineEvent 
{
	protected EngineInstance instance;
	protected EngineInstance other;
	protected int id;
}
