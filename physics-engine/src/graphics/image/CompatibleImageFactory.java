package graphics.image;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

public class CompatibleImageFactory 
{
	public static BufferedImage createCompatibleImage(int width, int height, int transparency)
	{
		GraphicsConfiguration gConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();	
		
		
		BufferedImage optimized = gConfig.createCompatibleImage(width, height, transparency);
		return optimized;
	}
}
