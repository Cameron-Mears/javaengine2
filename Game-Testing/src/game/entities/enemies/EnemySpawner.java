package game.entities.enemies;

import engine.core.instance.InstanceID;
import engine.core.tick.TickInfo;
import engine.core.tick.TickScheduler;
import engine.core.tick.Tickable;
import engine.util.TimeUtils;

public class EnemySpawner implements Tickable
{
	InstanceID<TickScheduler.Node> node;
	long n = 0;
	int count;
	double interval;
	double sum;
	public EnemySpawner(int count, double interval) 
	{
		this.interval = interval;
		this.count = count;
		this.node = TickScheduler.getInstance().addPeriodic(this, TimeUtils.secondsToNanos(interval));
	}

	@Override
	public void onTick(TickInfo info) {
		
		sum = 0;
		while (info.delta - sum > interval)
		{
			new BasicEnemy(n);
			count--;
			sum += interval;
			n++;
			if (count == 0) TickScheduler.getInstance().remove(node);
		}
		
	}
	
}
