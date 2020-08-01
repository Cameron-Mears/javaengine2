package engine.util.math;

public final class MathUtils 
{
	public static double clamp(double val, double min, double max)
	{
		boolean isLarger = val > max;
		
		if (isLarger) return max;
		
		return val > min? val:min;
	}
}
