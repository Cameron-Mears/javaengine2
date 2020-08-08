package engine.util.pathing;

import engine.util.pathing.PathNode.Mode;
import external.org.json.JSONArray;
import external.org.json.JSONObject;

public class PathLoader 
{
	private PathNode current;
	private PathNode prev;
	
	public Path load(JSONObject path)
	{
		String mode = path.getString("mode");
		
		Mode pathMode = Enum.valueOf(Mode.class, mode);

		
		JSONArray paths = path.getJSONArray("path");
		
		JSONObject head = paths.getJSONObject(0);
		
		for (int index = 1; index < paths.length(); index++) 
		{
			JSONObject node = paths.getJSONObject(index);
			
			if (node.getBoolean("isJunction"))
			{
				
			}
		}
		
		return null;
	}
	
	private void parseNode(JSONObject node)
	{
		
	}
}
