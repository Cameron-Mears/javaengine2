package physics.collision;

import engine.core.instance.EngineInstance;
import engine.event.EngineEvent;

public class CollisionEvent extends EngineEvent
{
	public CollisionEvent(EngineInstance instance, EngineInstance other)
	{
		super(instance, other, EngineEvent.TYPE.COLLISION);
	}
}
