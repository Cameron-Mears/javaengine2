package engine.timer;

import engine.core.instance.EngineInstance;
import engine.event.EngineEvent;

public class TimerEvent extends EngineEvent
{
	public TimerEvent(EngineInstance instance, EngineInstance other) {
		super(instance, other, TYPE.TIMER);
		// TODO Auto-generated constructor stub
	}

	private Timer timer;
}
