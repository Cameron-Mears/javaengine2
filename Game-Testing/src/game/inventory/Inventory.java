package game.inventory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

import engine.core.input.InputEventListener;
import engine.core.input.InputHandler;
import engine.util.json.JSONSerializable;
import external.org.json.JSONArray;
import external.org.json.JSONObject;
import game.entities.item.Item;
import graphics.sprite.Sprite;
import graphics.sprite.SpriteMap;
import graphics.util.GraphicsUtils;
import graphics.viewer.Display;

public class Inventory implements JSONSerializable, Comparator<Entry>, InputEventListener
{
	private ArrayList<Entry> list;
	private int size; //number of enteries that the inventory can hold
	private int drawRowsSize = 10;
	private int spacing = 15;
	
	private Sprite uiButtonExit;
	private Font fontlarge;
	private Font itemCountFont;
	private InputHandler input;
	
	private int drawWidth = 600;
	private int drawHeight = 600;
	
	private boolean active;
	
	private LinkedList<Entry> findOccurances(Item item)
	{
		LinkedList<Entry> occurances = new LinkedList<Entry>();
		for (Entry entry : list) 
		{
			if (entry != null)
			{
				if (entry.getItem().equals(item)) occurances.add(entry);
			}
		}
		
		return occurances;
	}
	
	public void sort()
	{
		Collections.sort(list, this);
	}
	
	public Inventory(int size)
	{
		fontlarge = new Font("Arial", 0, 40);
		itemCountFont = new Font("Arial",0,12);
		list = new ArrayList<Entry>(size);
		input = new InputHandler(true);
		input.addListener(this);
		this.size = size;
		for (int i = 0; i < size; i ++)
		{
			list.add(null);
		}
		
		uiButtonExit = SpriteMap.getClonedSprite("uibuttonexit");
	}
	
	public Inventory(JSONObject json)
	{
		this(json.getInt("size"));
		JSONArray contents = json.getJSONArray("contents");
		JSONObject entry = null;
		for (Iterator<Object> iter = contents.iterator(); iter.hasNext(); entry = (JSONObject)(iter.next()) ) 
		{
			int count = entry.getInt("count");
			Item item = Item.getItem(entry.getString("item"));
			Entry e = new Entry(item, count);
			list.add(e);
		}
	}
	
	public int addItems(Item item, int count)
	{
		LinkedList<Entry> occurances = findOccurances(item);	
		for (Iterator<Entry> iter = occurances.iterator(); iter.hasNext() && count > 0;) 
		{
			Entry entry = iter.next();
			count -= entry.putItems(count); //add as many items as possible to preexisting stacks
		}
		
		
		LinkedList<Integer> emptyEntries = findEmpty();
		for (Iterator<Integer> iter = emptyEntries.iterator(); iter.hasNext() && count > 0;) 
		{
			int index = iter.next();
			Entry entry = new Entry(item);
			list.set(index, entry);
			count -= entry.putItems(count);
		}
		return count;

	}
	
	private LinkedList<Integer> findEmpty() 
	{
		LinkedList<Integer> occurances = new LinkedList<Integer>();
		
		for (int index = 0; index < size; index++) 
		{
			if (list.get(index) == null) occurances.add(index);
		}
		return occurances;
	}
	
	public void setActive(boolean active)
	{
		this.active = active;
		if (!active)
		{
			input.disable();
		}
		else input.enable();
		
	}

	public void render(Graphics2D g2, int x, int y)
	{
		if (active)
		{	
			x = GraphicsUtils.center(drawWidth, Display.WIDTH);
			y = GraphicsUtils.center(drawHeight, Display.HEIGHT);
			int uix = GraphicsUtils.putRight(32, drawWidth) + x;
			Color color = new Color(0, 0, 0, 230);
			g2.setColor(color);
			g2.fillRect(x, y, 600, 600);
			g2.setColor(Color.white);
			g2.setFont(fontlarge);
			g2.drawString("Inventory", x + 10, y + g2.getFontMetrics().getHeight());
			
			int yOffset = spacing;
			int xOffset = spacing;
			Font t = new Font("Arial", 0, 10);
			g2.drawImage(uiButtonExit.getCurrentFrame(), uix, y, null);
			
			for (int index = 0; index < size; index++) 
			{
				Entry entry = list.get(index);
				BufferedImage img = null;
				int count = 0;
				if (entry 	!= null)
				{
					Item item = entry.getItem();
					img = item.getImage();
					count = entry.getCount();
				}	
				
				
				if (index % drawRowsSize == 0)
				{
					xOffset = spacing;		
					yOffset += spacing + Item.HEIGHT;
				} else xOffset += spacing + Item.WIDTH;	
				
				g2.setColor(Color.GRAY);
				g2.setStroke(new BasicStroke(5));
				g2.drawRect(x + xOffset -2, y + yOffset-2, Item.WIDTH+3, Item.HEIGHT+3);
				g2.setColor(Color.white);
				g2.fillRect(x + xOffset, y + yOffset, Item.WIDTH, Item.HEIGHT);
				if (img != null) g2.drawImage(img, x + xOffset, y + yOffset, null);
				if (count != 0)
				{
					g2.setColor(Color.black);
					g2.fillRect(x + xOffset, y + yOffset + GraphicsUtils.putBottom(12, Item.HEIGHT), 22, 12);
					g2.setColor(Color.white);
					g2.setFont(itemCountFont);
					g2.drawString(Integer.toString(count), x + xOffset + 1, y +yOffset + GraphicsUtils.putBottom(12, Item.HEIGHT) + g2.getFontMetrics().getHeight() - 3);
				}
			}
		}
		
	}

	@Override
	public JSONObject serialize() {
		
		JSONObject obj = new JSONObject();
		JSONArray contents = new JSONArray();
		
		for (Entry e : list) 
		{
			if (e != null)
			{
				JSONObject entry = new JSONObject();
				entry.put("count", e.getCount());
				entry.put("item", e.getItem().getName());
				contents.put(entry);
			}
		}
		
		obj.put("size", size);
		obj.put("contents", contents);
		return obj;
		
	}

	@Override
	public int compare(Entry e, Entry e1) //sort first by id then stack size
	{		
		int idSum = e.getItem().getID() - e1.getItem().getID();
		return (idSum != 0)? idSum:e.getCount() - e1.getCount();
	}

	@Override
	public void newInput() {
		if (input.isKeyDown(KeyEvent.VK_ESCAPE) || input.isKeyDown(KeyEvent.VK_TAB)) setActive(false);
		
	}
}
