package engine.core.tick;

import java.util.function.Function;

public interface Tickable
{
	public void onTick(TickInfo info);

}
