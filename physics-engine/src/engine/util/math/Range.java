package engine.util.math;

public class Range 
{
	private double min, max;
	
	
	@Override
	public String toString()
	{
		return "Min: " + Double.toString(min) + ", Max: " + Double.toString(max);
	}
	
	public Range(double min, double max)
	{
		this.min = min;
		this.max = max;
	}
	
	public Range(double a, double b, boolean auto)
	{
		this(a,b);
		if (auto)
		{
			if (max < min)
			{
				double temp = min;
				min = max;
				max = temp;
			}
		}
		
	}
	
	public double clamp(double val)
	{
		return MathUtils.clamp(val, min, max);
	}
	
	public boolean contains(double val)
	{
		return val >= min && val <= max;
	}

	/**
	 * returns an array from floor (min) to ceil(max)
	 * @return 
	 */
	public int[] toIntArray() {
		int lb = (int) Math.floor(min);
		int ub = (int) Math.ceil(max);
		int size = ub-lb;
		int[] arr = new int[size+1];
		for (int i = 0; i < arr.length; i++) 
		{
			arr[i] = lb+i;
		}
		return arr;
	}
}
