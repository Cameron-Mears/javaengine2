package graphics.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import engine.core.exceptions.EngineException;
import engine.util.quadtree.CollisionNode;
import engine.util.quadtree.CollisionQuadTree;
import engine.util.quadtree.QuadTreeNode;
import external.org.json.JSONArray;
import external.org.json.JSONObject;
import graphics.instance.IGraphics;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class TileMap implements IGraphics
{
	private String name;
	private TileMapImageSet imageSet;
	
	private TileMapChunk[][] chunks;
	
	private int rows, columns;
	private Rectangle bounds;
	private int width, height;
	private int cellWidth, cellHeight;
	private int chuckWidth;
	private int chunkHeight;
	private int chunkRows, chunkColumns;
	
	private CollisionQuadTree<TileMapChunk> chunkTree;
	
	private int[][] tileMap;
	
	public TileMap(JSONObject json, TileMapImageSet set, String name) throws EngineException
	{
		this.imageSet = set;
		this.columns = json.getInt("columns");
		this.rows = json.getInt("rows");
		this.cellWidth = set.getCellWidth();
		this.cellHeight = set.getCellHeight();
		this.width = cellWidth * rows;
		this.height = cellHeight * columns;
		
		JSONObject origin = json.getJSONObject("origin");
		
		Vector2 pos = new Vector2(origin.getDouble("origin_x"), origin.getDouble("origin_y"));
		
		this.bounds = new Rectangle(width, height, pos);
		this.chunkTree = new CollisionQuadTree<TileMapChunk>(bounds, pos);
		JSONObject chunkSize = json.getJSONObject("chunksize");
		
		this.tileMap = new int[columns][rows];
		this.name = name;
		
		int chunkCellsPerRow = chunkSize.getInt("cellsPerRow");
		int chunkCellsPerColumn = chunkSize.getInt("cellsPerColumn");
		this.chuckWidth = chunkCellsPerRow * cellWidth;
		this.chuckWidth = chunkCellsPerColumn * cellHeight;
		Number nChunkRows = rows/chunkCellsPerRow;
		Number nChunkColumns = columns/chunkCellsPerColumn;
		
		if (!(nChunkRows instanceof Integer)) throw new EngineException("TileMapChunk: property cellsPerRow must be a multiple of tilemap rows");
		if (!(nChunkRows instanceof Integer)) throw new EngineException("TileMapChunk: property cellsPerColumn must be a multiple of tilemap columns");
		
		this.chunks = new TileMapChunk[nChunkColumns.intValue()][nChunkRows.intValue()];
		
		JSONArray tileMapData = json.getJSONArray("map");	
		
		int[][] chunkData = new int[chunkCellsPerColumn][chunkCellsPerRow];
		
		int chunkCol = 0, chunkRow = 0;
		
		for (int column = 0; column < tileMapData.length(); column++) 
		{			
			JSONArray arr = tileMapData.getJSONArray(column);			
			for (int row = 0; row < arr.length(); row++) 
			{

				tileMap[column][row] = arr.getInt(row);
				
			}		
		}
		
		int row = 0, col = 0;
		int[][] chuckData = new int[chunkCellsPerColumn][chunkCellsPerColumn];
		
		
		for (col = 0; col < nChunkColumns.intValue(); col++) 
		{
			for (row = 0; row < nChunkRows.intValue(); row++) 
			{
				System.arraycopy(tileMap[col], row*chunkCellsPerRow, chuckData[col], 0, chunkCellsPerRow);
			}
			
		}
		
		subdivide();
		
	}
	
	public void toAstarGrid()
	{
		
	}
	
	private void subdivide() //create the chunks
	{
		
	}
	
	
	public BufferedImage getTileByID(int id)
	{
		return imageSet.getTile(id);
	}
	
	public int getCellWidth() 
	{
		return cellWidth;
	}

	public int getCellHeight() 
	{
		return cellHeight;
	}
	
	public String getName() 
	{
		return name;
	}


	@Override
	public void render(Graphics2D g2) 
	{
		LinkedList<QuadTreeNode<TileMapChunk>> node = chunkTree.queryRange(null);
		
		for (QuadTreeNode<TileMapChunk> quadTreeNode : node)
		{
			BufferedImage img = quadTreeNode.get().getRenderedChuck();
			Vector2 pos = quadTreeNode.getPosition();
			g2.drawImage(img, (int)pos.getX(), (int)pos.getY(), null);
		}
	}

	@Override
	public Rectangle renderBoundingArea() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
