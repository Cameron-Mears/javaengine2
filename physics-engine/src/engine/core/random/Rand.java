package engine.core.random;

public class Rand 
{
	/**
	 * 
	 * @return a long between 0 and Long.MAX_VALUE
	 */
	public static long randomLong()
	{
		return (long)(Math.random() * Long.MAX_VALUE);
	}
	/**
	 * 
	 * @param min 
	 * @param max
	 * @return a random number between min and max
	 */
	public double range(double min, double max)
	{
		return ((max - min) * Math.random()) + min;
	}
}
