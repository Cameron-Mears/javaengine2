package engine.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import engine.core.exceptions.EngineException;
import engine.core.input.InputHandler;
import engine.util.parsers.AssetMapParser;
import external.org.json.JSONException;
import external.org.json.JSONObject;
import external.org.json.JSONTokener;
import graphics.instance.InvalidInstanceException;

public class Engine                                                                                                                                             implements Runnable
{
	
	private long deltaF, deltaU;	
	
	private boolean doLoad = true;
	
	private static String USER_DIR = System.getProperty("user.dir");
	
	
	private HashMap<String,Object> engineProperties;
	
	
	private static Engine instance;
	
	
	private static void init() throws JSONException, IOException
	{
		long now = System.currentTimeMillis();
		instance = new Engine();
		System.out.println("init complete time: " + Long.toString(System.currentTimeMillis() - now) + "ms");
	}
	
	public static synchronized Engine getInstance() throws JSONException, IOException
	{
		if (instance == null)
			try {
				init();
			} catch (FileNotFoundException e) {
				System.out.println("Engine Properties File not Found");
			}
		return instance;
	}
	

	
	private Engine() throws JSONException, IOException
	{
		engineProperties = new HashMap<String, Object>();
		engineProperties.put("numberOfProcessors", Runtime.getRuntime().availableProcessors());
	
		
		JSONObject jobj = new JSONObject(new JSONTokener(new FileInputStream(new File(System.getProperty("user.dir")+"\\engine_properties.json"))));
		
		
		//engine properties
		
		JSONObject enginePropertiesJSON = jobj.getJSONObject("engine");
		
		int tickrate = enginePropertiesJSON.getInt("tickrate");
		this.engineProperties.put("tickrate",tickrate);
		int framerate = enginePropertiesJSON.getInt("framerate");
		this.engineProperties.put("framerate", framerate);
		boolean printFramerate = enginePropertiesJSON.getBoolean("doTickAndFrameRatePrint");
		this.engineProperties.put("printRates", printFramerate);
		this.doLoad = enginePropertiesJSON.getBoolean("doLoad");		;
		this.engineProperties.put("doLoad", doLoad);
		//window properties
		
		JSONObject windowProperties = jobj.getJSONObject("window");
		
		int width = windowProperties.getInt("window_width");
		int height = windowProperties.getInt("window_height");
		
		int monitor = windowProperties.getInt("screen");
		
		
		boolean fullscreen = windowProperties.getBoolean("fullscreen");
		
		String windowTitle = windowProperties.getString("title");
		
		engineProperties.put("window_width", width);
		engineProperties.put("window_height", height);
		engineProperties.put("window_fullscreen", fullscreen);
		engineProperties.put("window_monitor", monitor);
		engineProperties.put("title", windowTitle);
		
		//networking settings
		
		JSONObject serverSettings = jobj.getJSONObject("server");
		
		int defaultServerPort = serverSettings.getInt("server_port");
		
		engineProperties.put("server_port", defaultServerPort);
		
		
		//System.out.println("C:\\Users\\camer\\OneDrive\\Documents\\GitHub\\javaengine2\\Game-Testing\\config\\tickHandlerConfig.json".equals((USER_DIR+"\\config\\tickHandlerConfig.json")));
		
		//add to seperate parsing methods
		engineProperties.put("tickHandlerConfig", new JSONObject(new JSONTokener(new FileInputStream(new File(USER_DIR+"\\config\\tickHandlerConfig.json")))).getJSONArray("tick_handler"));
		
		AssetMapParser.parseAssetMap(new File(System.getProperty("user.dir") + "\\assets\\assetmap.json"));
	
		this.recomputeParams();

	}
	
	
	public Object getProperty(String property)
	{
		return engineProperties.get(property);
	}
	
	public boolean setProperty(String property, int value)
	{
		if (engineProperties.containsKey(property))
		{
			engineProperties.put(property,value);
			return true;
		}
		return false;
	}
	
	private void recomputeParams()
	{
		this.deltaF = (1000000000)/(int)engineProperties.get("framerate");
		this.deltaU = (1000000000)/(int)engineProperties.get("tickrate");
	}

	public void start() throws JSONException, EngineException, InvalidInstanceException, IOException 
	{
		GameLoop gl = new GameLoop(this, (int) this.getProperty("tickrate"), (int) this.getProperty("framerate"));
		gl.start();
	}
	
	
	public static void printDebugMessage(Object message, Object sender)
	{
		System.out.print("[DEBUG][" + sender.getClass().getName() + "]: ");
		System.out.println(message);
	}
	
	
	public static void setPrintFrameAndTick(boolean value)
	{
		if (instance != null)
		{
			instance.engineProperties.put("printRates", value);
		}
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		
	}
	
	
	
}
