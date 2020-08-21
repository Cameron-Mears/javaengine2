package game.items.artifact;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import external.org.json.JSONArray;
import external.org.json.JSONObject;
import game.entities.modifiers.EntityModifiers;
import game.enums.ActionType;
import game.enums.Rarity;

public class Artifact 
{
	private String name;
	private String description;
	
	
	private BufferedImage img;
	
	
	private Rarity rarity;
	
	private HashMap<ActionType, Boolean> isActionType;
	
	private EntityModifiers mods;
	
	public Artifact(JSONObject object)
	{
		this.isActionType = new HashMap<ActionType, Boolean>();
		this.name = object.getString("name");
		String rarity = object.getString("rarity");
		this.rarity = Enum.valueOf(Rarity.class, rarity);
		JSONArray actionTypes = object.getJSONArray("actionTypes");
		for (int i = 0; i < actionTypes.length(); i ++)
		{
			ActionType action = Enum.valueOf(ActionType.class, actionTypes.getString(i));
			isActionType.put(action, true);
		}
	}
	
	public void render(Graphics2D g2)
	{
		
	}
	
	
}
