package engine.core.tick;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Function;

import engine.core.Engine;
import engine.core.JSON_CONSTANTS;
import engine.core.exceptions.EngineException;
import engine.util.bst.BST;
import engine.util.bst.Node;
import external.org.json.JSONArray;
import external.org.json.JSONException;
import external.org.json.JSONObject;
import graphics.instance.InvalidInstanceException;

public class TickHandler 
{
	private BST<String,TickableGroup> groups;
	
	private int nProcessors = 0;
	private Function<TickableGroup, Void> inOrderTraversalFunction;
	private long deltaNS;
	private Queue<Tickable> tickables;
	
	
	private static TickHandler instance;
	
	public static TickHandler getInstance() throws JSONException, InvalidInstanceException, EngineException, IOException
	{
		if (instance == null)
		{
			init();
		}
		return instance;
	}
	
	private TickHandler() throws EngineException, JSONException, InvalidInstanceException, IOException
	{
		
		this.nProcessors = (int) Engine.getInstance().getProperty("numberOfProcessors");
		
		
		inOrderTraversalFunction = new Function<TickableGroup, Void>() 
		{

			@Override
			public Void apply(TickableGroup tickGroup) {
				
				String groupName = tickGroup.getName();
				TickInfo tf = new TickInfo();
				tf.groupName = groupName;
				
				tf.delta = ((double)deltaNS)/(1e9);
				
				tickGroup.tick(tf);
				
				return null;
			}
		};
		
		groups = new BST<String, TickableGroup>();
		this.tickables = new LinkedList<Tickable>();
		if ((boolean)Engine.getInstance().getProperty("doLoad")) this.parseJSONConfig((JSONArray) Engine.getInstance().getProperty("tickHandlerConfig"));
		
	}
	
	public boolean isGroupEnabled(String group)
	{
		return groups.findNode(group);
		
	}
	
	public static void init() throws EngineException, JSONException, InvalidInstanceException, IOException
	{
		instance = new TickHandler();
	}
	
	private void parseJSONConfig(JSONArray groupList) throws EngineException, JSONException, InvalidInstanceException
	{
		
		/*
		 * iterates through each TickableGroup listed in the JSON file, and will add it to the tree
		 */
		for (int index = 0; index < groupList.length(); index ++)
		{
			JSONObject groupJSON = groupList.getJSONObject(index);
			TickableGroup group = this.parseGroupfromJSON(groupJSON);
			groups.addNode(group.getName(), group);
		}
		
	}
	
	public void tick(long deltaNS)
	{
		this.deltaNS = deltaNS;
		groups.inOrderTraverse(inOrderTraversalFunction);
		
		TickInfo tf = new TickInfo();
		
		tf.groupName = "queuedTickable";
		tf.delta = deltaNS/(1e9);
		tf.deltaNS = deltaNS;
		
		while (!this.tickables.isEmpty())
		{
			tickables.poll().onTick(tf);
		}
	}
	
	private TickableGroup parseGroupfromJSON(JSONObject jsonObject) throws  JSONException, InvalidInstanceException, EngineException
	{
		TickableGroup group = new TickableGroup(jsonObject.getString(JSON_CONSTANTS.OBJECT_NAME));
		
		boolean state = jsonObject.getBoolean(JSON_CONSTANTS.OBJECT_ENABLED);
		if (!state)
		{
			group.disalbe();
		}
		
		group.addInstancesFromJSON(jsonObject.getJSONArray("members"));
		
		return group;
		
	}
	
	public void queueTickable(Tickable tickable)
	{
		this.tickables.add(tickable);
	}
}
