package engine.util;

public class VarArgUtil 
{
	public static int[] range(int min, int max)
	{
		int size = max - min;
		int[] ret = new int[size+1];
		for (int n = min; n <= max; n++) {
			ret[n-min] = n;
			
		}
		return ret;
	}
}
