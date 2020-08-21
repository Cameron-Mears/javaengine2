package engine.util;

import java.util.LinkedList;

public class MathUtils 
{
	public static LinkedList<Integer> factors(int n)
	{
		int factor = 1;
		LinkedList<Integer> factors = new LinkedList<Integer>();
		while (factor <= n)
		{
			if (n % factor == 0) factors.add(factor);
			factor++;
		}
		return factors;
	}
}
