package graphics.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import engine.core.exceptions.EngineException;
import engine.util.pathing.AStarGrid;
import engine.util.quadtree.CollisionNode;
import engine.util.quadtree.CollisionQuadTree;
import engine.util.quadtree.QuadTreeNode;
import external.org.json.JSONArray;
import external.org.json.JSONObject;
import graphics.instance.IGraphics;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.general.Vector2;

/**
 * 
 * @author Cameron
 * Provides the ability to create tilemaps which allow for more complex backgrounds in a game
 * tilemaps are subdivided into chunks so only the parts in the frame are render and to reduce render calss
 */
public class TileMap
{
	private String name;
	private TileMapImageSet imageSet;
	
	private TileMapChunk[][] chunks;
	
	private int rows, columns;
	private Rectangle bounds;
	private int width, height;
	private int cellWidth, cellHeight;
	private int chunkWidth;
	private int chunkHeight;
	private int chunkRows, chunkColumns;
	private int nChunkRows, nChunkColumns;
	
	/**
	 * the quadtree to capture chunks in the frame //maybe change to array
	 */
	private CollisionQuadTree<TileMapChunk> chunkTree;
	
	private int[][] tileMap;
	private boolean drawChunkBoxes;
	
	/**
	 * 
	 * @param json the json object containing the properties of this tilemap
	 * @param set the image set to use
	 * @param name the name of this tilemap
	 */
	public TileMap(JSONObject json, TileMapImageSet set, String name)
	{
		this.imageSet = set;
		this.columns = json.getInt("columns");
		this.rows = json.getInt("rows"); 
		this.cellWidth = set.getCellWidth();
		this.cellHeight = set.getCellHeight();
		this.width = cellWidth * rows; //total width of the tilemap
		this.height = cellHeight * columns; //total height of the tilemap
		
		JSONObject origin = json.getJSONObject("origin");
		
		Vector2 pos = new Vector2(origin.getDouble("origin_x"), origin.getDouble("origin_y"));
		
		this.bounds = new Rectangle(width, height, pos);
		this.chunkTree = new CollisionQuadTree<TileMapChunk>(bounds, pos);
		JSONObject chunkSize = json.getJSONObject("chunksize");
		
		this.tileMap = new int[columns][rows];
		this.name = name;
		
		int chunkCellsPerRow = chunkSize.getInt("cellsPerRow");
		int chunkCellsPerColumn = chunkSize.getInt("cellsPerColumn");
		this.chunkWidth = chunkCellsPerRow;
		this.chunkHeight = chunkCellsPerColumn;
		double nChunkRowsTest = rows/chunkCellsPerRow;
		double nChunkColumnsTest = columns/chunkCellsPerColumn;
		
		
		if (nChunkRowsTest % 1 != 0) throw new EngineException("TileMapChunk: property cellsPerRow must be a multiple of tilemap rows");
		if (nChunkRowsTest % 1 != 0) throw new EngineException("TileMapChunk: property cellsPerColumn must be a multiple of tilemap columns");
		
		nChunkRows = (int) nChunkRowsTest;
		nChunkColumns = (int) nChunkColumnsTest;
		
		JSONArray tileMapData = json.getJSONArray("map");
		
		int[][] chunkData = new int[columns][rows]; //create the int code map for the tilemap
		
		int chunkCol = 0, chunkRow = 0;
		
		//load codes into the array
		for (int column = 0; column < tileMapData.length(); column++) 
		{			
			JSONArray arr = tileMapData.getJSONArray(column);			
			for (int row = 0; row < arr.length(); row++) 
			{

				tileMap[column][row] = arr.getInt(row);
				
			}		
		}		
		subdivide();
		
	}
	
	public void setDrawChunkBounds(boolean drawBounds)
	{
		drawChunkBoxes = drawBounds;
	}
	
	public AStarGrid toAstarGrid(Set<Integer> ingoredCells)
	{
		return null;
	}
	
	private void subdivide() //create the chunks
	{
		this.chunks = new TileMapChunk[nChunkColumns][nChunkRows];
		for (int col = 0; col < chunks.length; col++) 
		{
			for (int row = 0; row < chunks[col].length; row++) 
			{
				int[][] chunkData = new int[chunkHeight][chunkWidth];
				copyChuckData(chunkData, col, row);				
				chunks[col][row] = new TileMapChunk(this, chunkWidth*cellWidth, chunkHeight*cellHeight, chunkData);
				CollisionNode<TileMapChunk> node = new CollisionNode<TileMapChunk>(new Vector2(row * cellWidth * chunkWidth, col * cellHeight * chunkHeight), chunks[col][row], new HitBox(new Rectangle(cellWidth * chunkWidth, cellHeight * chunkHeight, new Vector2(row * cellWidth * chunkWidth, col * cellHeight * chunkHeight)), null));
				chunkTree.insert(node);
			}
		}
	}
	
	private void copyChuckData(int[][] newChunkData, int colStart, int rowStart)
	{
		int column = colStart * chunkHeight;
		int row = rowStart * chunkWidth;
		
		for (int c = 0; c < newChunkData.length; c++)
		{
			for (int r = 0; r < newChunkData[c].length; r++) 
			{
				newChunkData[c][r] = tileMap[column + c][row + r];
			}
		}
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

	
	public int[][] getData()
	{
		return tileMap;
	}


	public void render(Graphics2D g2, Rectangle bounds) 
	{
		LinkedList<QuadTreeNode<TileMapChunk>> node = chunkTree.queryRange(bounds);
		
		for (QuadTreeNode<TileMapChunk> quadTreeNode : node)
		{
			CollisionNode<TileMapChunk> e = (CollisionNode<TileMapChunk>) quadTreeNode;
			BufferedImage img = quadTreeNode.get().getRenderedChuck();
			Vector2 pos = quadTreeNode.getPosition();
			HitBox box = e.getHitbox();
			if (box.getBounds().contains(bounds)) g2.drawImage(img, (int)pos.getX(), (int)pos.getY(), null);
			if (drawChunkBoxes) box.drawHitBox(g2);
		}
	}

	
	public Rectangle renderBoundingArea() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
