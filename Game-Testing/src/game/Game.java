package game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Function;

import engine.core.Engine;
import engine.core.exceptions.EngineException;
import engine.util.bst.BST;
import engine.util.quadtree.QuadTree;
import engine.util.quadtree.QuadTreeNode;
import external.org.json.JSONException;
import graphics.instance.InvalidInstanceException;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class Game
{
	
	public static void main(String[] args) throws JSONException, IOException, EngineException, InvalidInstanceException
	{
    	
		Vector2 vec2;
		
		QuadTree<Vector2> quad = new QuadTree<Vector2>(new Rectangle(1200, 800), new Vector2(0,0));
		
		
		long now = System.currentTimeMillis();
		
		for (int i = 0; i < 1000; i++) 
		{
			double x = 1200 * Math.random();
			double y = 800 * Math.random();
			vec2 = new Vector2(x,y);
			QuadTreeNode<Vector2> node = new QuadTreeNode<Vector2>(vec2, vec2);
			quad.insert(node);
		}
		
		now = System.nanoTime();
		
		LinkedList<QuadTreeNode<Vector2>> test = quad.queryRange(new Rectangle(23, 43, 100, 200));
		
		GraphicsLayer g = GraphicsLayerManager.getInstance().getLayer("default");
		g.addIGrpahics(quad, 0);
		
		Engine.getInstance().start();
		
		
	}
	
}

