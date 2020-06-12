package engine.util.parsers.image;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageParser 
{
	//coverts any supported image type into a image compatible with the graphics configuration of the system
	
	public static BufferedImage parseImage(File image) throws IOException
	{
		BufferedImage bimg = ImageIO.read(image);
		
		GraphicsConfiguration gConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		
		
		BufferedImage optimized = gConfig.createCompatibleImage(bimg.getWidth(), bimg.getHeight(), bimg.getTransparency());
		
		Graphics2D g2 = optimized.createGraphics();
		g2.drawImage(bimg, 0, 0, null);
		g2.dispose();
		return optimized;
	}
}
