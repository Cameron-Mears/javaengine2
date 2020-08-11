package engine.util;

public class TimeUtils 
{
	public static long secondsToNanos(double seconds)
	{
		return (long) (seconds * 1e9);
	}

	public static double nanosToSeconds(long l) 
	{
		return ((double)l)/1e9;
	}
}
