package engine.util;

import engine.core.instance.EngineInstance;
import engine.core.instance.InstanceID;

public interface EngineRemovable 
{
	public void remove(InstanceID<EngineInstance> id);
}
