package engine.util;

public class TimeUtils 
{
	public static long secondsToNanos(double seconds)
	{
		return (long) (seconds * 1e9);
	}
}
