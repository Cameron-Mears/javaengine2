package engine.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import engine.util.files.FileUtils;
import external.org.json.JSONObject;
import external.org.json.JSONTokener;
import physics.general.Vector2;

public class GameFiles 
{
	private HashMap<String, JSONObject> files;
	
	GameFiles(String rootDir) 
	{
		files = new HashMap<String, JSONObject>();
		File folder = new File(rootDir);
		parseFolder(folder);
	}
	
	public JSONObject get(String string) 
	{
		return files.get(string);
	}
	
	private void parseFolder(File folder)
	{
		for (File file : folder.listFiles()) 
		{
			if (file.isDirectory())
			{
				parseFolder(file);
			}
			else
			{
				if (FileUtils.getExtenstion(file).equals("json"))
				{
					JSONObject obj = parseJSONFile(file);
					files.put(file.getName(), obj);
				}
			}
		}
	}
	
	private JSONObject parseJSONFile(File file)
	{
		try
		{
			FileInputStream fis = new FileInputStream(file);
			Vector2 a;
			JSONTokener tokener = new JSONTokener(fis);
			try
			{
				return new JSONObject(tokener);
			}
			catch (Exception e) {
				Engine.printWarningMessage("Failied to load file -> " + file.getPath() + "\n Reason -> " + e.toString(), this);
			}
		}
		catch (IOException e) {System.out.println("Error reading File " + file.getAbsolutePath());}
		return null;
	}

}
