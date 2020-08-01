package graphics.util;

public class GraphicsUtils 
{
	/**
	 * 
	 * @param width the width of the line to center
	 * @return the x coordinate for where the line should be draw to be centered with the totalWidth
	 */
	public static int center(int width, int totalWidth)
	{
		return (totalWidth-width)/2;
	}
	
	public static final int putRight(int width, int totalWidth)
	{
		return totalWidth - width;
	}
	
	public static final int putBottom(int height, int totalHeight)
	{
		return totalHeight - height;
	}
}
