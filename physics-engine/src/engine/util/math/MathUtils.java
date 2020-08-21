package engine.util.math;

public final class MathUtils 
{
	public static double clamp(double val, double min, double max)
	{
		boolean isLarger = val > max;
		
		if (isLarger) return max;
		
		return val > min? val:min;
	}

	public static double direction(double yDirection, double xDirection) 
	{
		double angle =  Math.atan2(-yDirection, xDirection);
		return (angle < 0)? (2*Math.PI)+angle:angle;
	}
}
