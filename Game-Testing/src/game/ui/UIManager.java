package game.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import engine.core.Engine;
import engine.core.exceptions.EngineException;
import engine.core.instance.EngineInstance;
import external.org.json.JSONObject;
import game.entities.player.PlayerTemplate;
import game.enums.PlayerClass;
import game.weapons.Weapon;
import graphics.instance.IGraphics;
import physics.collision.Rectangle;

public class UIManager extends EngineInstance implements IGraphics
{
	private PlayerTemplate player;
	
	private HashMap<String, BufferedImage> images;
	
	private ArrayList<Weapon> weapons;
	
	private PlayerClass playerType;
	
	public UIManager(PlayerTemplate controllerPlayer)
	{
		this.player = controllerPlayer;
		this.weapons = player.getWeapons();
		this.playerType = player.getPlayerClass();
		
		JSONObject playerimages = Engine.getGameFiles().get(playerType.toString() + "_ui_images.json");
		if (playerimages == null) throw new EngineException("Cannot load UI images for -> " + playerType.toString());
	}

	@Override
	public void render(Graphics2D g2) 
	{
		
	}

	@Override
	public Rectangle renderBoundingArea() 
	{
		return null;
	}
	
}
