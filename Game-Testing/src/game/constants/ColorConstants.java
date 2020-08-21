package game.constants;

import java.awt.Color;
import java.util.HashMap;

import game.enums.Rarity;

public class ColorConstants 
{
	public static final Color LEGENDARY = new Color(0.97f, 0.62f, 0.16f, 1f);
	
	private static HashMap<Rarity, Color> rarityColors;
	
	public static Color getColorByRarity(Rarity rarity)
	{
		return rarityColors.get(rarity);
	}
	
	static
	{
		rarityColors = new HashMap<Rarity, Color>();
		rarityColors.put(Rarity.LEGENDARY, LEGENDARY);
	}
}
