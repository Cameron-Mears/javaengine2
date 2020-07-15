package physics.collision;

import engine.core.instance.EngineInstance;
import engine.event.EngineEvent;
import physics.general.Vector2;

public class CollisionEvent extends EngineEvent
{
	private Vector2 place;
	
	public CollisionEvent(EngineInstance instance, EngineInstance other)
	{
		super(instance, other, EngineEvent.TYPE.COLLISION);
	}
}
