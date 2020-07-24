package engine.core.tick;

import engine.core.instance.EngineComponent;

public interface Tickable extends EngineComponent
{
	public void onTick(TickInfo info);

}
