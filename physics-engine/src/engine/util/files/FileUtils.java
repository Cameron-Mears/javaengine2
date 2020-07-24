package engine.util.files;

import java.io.File;

public class FileUtils 
{
	public static String getExtenstion(File file)
	{
		if (file == null) return "";
		String name = file.getAbsolutePath();
		int dotIndex = name.lastIndexOf('.');
		return name.substring(dotIndex + 1, name.length());
	}
}
