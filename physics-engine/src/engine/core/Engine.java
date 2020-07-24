package engine.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import engine.core.exceptions.EngineException;
import engine.util.files.FileUtils;
import engine.util.parsers.AssetMapParser;
import external.org.json.JSONException;
import external.org.json.JSONObject;
import external.org.json.JSONTokener;
import graphics.instance.InvalidInstanceException;

public final class Engine                                                                                                                                             implements Runnable
{
	
	private long deltaF, deltaU;	
	
	private boolean doLoad = true;
	
	private static String USER_DIR = System.getProperty("user.dir");
	
	private GameLoop gameLoop;
	
	private ConcurrentHashMap<String,Object> engineProperties;
	private GameFiles files;
	
	private static Engine instance;
	
	
	private static void init() throws JSONException, IOException, EngineException
	{
		long now = System.currentTimeMillis();
		instance = new Engine();
		instance.recomputeParams();
		System.out.println("init complete time: " + Long.toString(System.currentTimeMillis() - now) + "ms");
	}
	
	public static Engine getInstance()
	{
		if (instance == null)
			try {
				init();
			} catch (FileNotFoundException e) {
				System.out.println("Engine Properties File not Found");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new EngineException(e.getMessage());
			}
		return instance;
	}
	
	/**
	 * 
	 * @param property
	 * @param value
	 * @throws EngineException, if the key does not point to a property or the value does not match the type in the map
	 */
	public void setProperty(String property, Object value)
	{
		if (property == null) throw new EngineException("Null Key");
		Object currentValue = engineProperties.get(property);
		if (currentValue == null) throw new EngineException("The key does not point to any properties");
		if (currentValue.getClass().isInstance(value))
		{
			engineProperties.put(property, value);
			recomputeParams();
		} else throw new EngineException("Class of value: [\"" + value.getClass().getName() + "\"] does not match the excepted type: " + currentValue.getClass().getName());
	}
	
	
	private Engine() throws JSONException, IOException, EngineException
	{
		
		engineProperties = new ConcurrentHashMap<String, Object>();
		engineProperties.put("numberOfProcessors", Runtime.getRuntime().availableProcessors());
	
		//System.out.println(System.getProperty("user.dir")+"\\engine_properties.json");
		JSONObject jobj = new JSONObject(new JSONTokener(new FileInputStream(new File(System.getProperty("user.dir")+"\\engine_properties.json"))));
		
		
		//engine properties
		
		JSONObject enginePropertiesJSON = jobj.getJSONObject("engine");
		
		int tickrate = enginePropertiesJSON.getInt("tickrate");
		this.engineProperties.put("tickrate",tickrate);
		int framerate = enginePropertiesJSON.getInt("framerate");
		this.engineProperties.put("framerate", framerate);
		boolean printFramerate = enginePropertiesJSON.getBoolean("doTickAndFrameRatePrint");
		this.engineProperties.put("printRates", printFramerate);
		this.doLoad = enginePropertiesJSON.getBoolean("doLoad");
		this.engineProperties.put("doLoad", doLoad);
		JSONObject collisionLayerProperties = enginePropertiesJSON.getJSONObject("collisionDefaultLayerBounds");
		this.engineProperties.put("collisionDefaultLayerBounds", collisionLayerProperties);
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
		
		files = new GameFiles(USER_DIR + "\\assets\\gamefiles"); 
		AssetMapParser.parseAssetMap(new File(System.getProperty("user.dir") + "\\assets\\assetmap.json"));
	}
	
	
	public Object getProperty(String property)
	{
		return engineProperties.get(property);
	}
	
	private void recomputeParams()
	{
		this.deltaF = (1000000000)/(int)engineProperties.get("framerate");
		this.deltaU = (1000000000)/(int)engineProperties.get("tickrate");
		
		if (gameLoop != null) gameLoop.newParamters();
	}

	public void start()
	{
		if (gameLoop == null) gameLoop = new GameLoop(this, (int) engineProperties.get("tickrate"), (int) engineProperties.get("framerate"));
		gameLoop.start();
	}
	
	
	public static void printDebugMessage(Object message, Object sender)
	{
		System.out.print("[DEBUG][" + sender.getClass().getName() + "]: ");
		System.out.println(message);
	}
	
	public static void printWarningMessage(Object message, Object sender)
	{
		//System.out.print("[WARNING][" + sender.getClass().getName() + "]: ");
		//System.out.println(message);
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

	public static GameFiles getGameFiles() 
	{
		return getInstance().files;
	}
	
	
	
}
