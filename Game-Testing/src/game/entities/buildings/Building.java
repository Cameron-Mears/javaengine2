package game.entities.buildings;

import java.util.LinkedList;

import graphics.instance.IGraphics;

public abstract class Building implements Traverseable, IGraphics
{
	protected LinkedList<Traverseable> children;
	
	public Building()
	{
		this.children = new LinkedList<Traverseable>();
	}
}
