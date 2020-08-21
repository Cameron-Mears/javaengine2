package graphics.tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Set;

import engine.core.exceptions.EngineException;
import engine.util.json.JSONSerializable;
import engine.util.pathing.AStarGrid;
import external.org.json.JSONArray;
import external.org.json.JSONObject;
import graphics.util.GraphicsUtils;
import physics.collision.Rectangle;
import physics.collision.quadtree.CRQuadTree;
import physics.general.Vector2;

/**
 * 
 * Provides the ability to create tilemaps which allow for more complex backgrounds in a game
 * tilemaps are subdivided into chunks so only the parts in the frame are render and to reduce render calss
 * 
 * @author Cameron
 * 
 */
public class TileMap implements JSONSerializable
{
	private String name;
	private TileMapImageSet imageSet;
	private LinkedList<BufferedImage> renderQueue;
	private TileMapChunk[][] chunks;
	
	
	private Vector2 pos;
	private int rows, columns;
	private Rectangle bounds;
	private int width, height;
	private int cellWidth, cellHeight;
	private int chunkWidth;
	private int chunkHeight;
	private int chunkRows, chunkColumns;
	private int nChunkRows, nChunkColumns;
	private int chunkCellsPerRow, chunkCellsPerColumn;
	
	private int[] backgroundTiles;
	
	/**
	 * the quadtree to capture chunks in the frame //maybe change to array
	 */
	private CRQuadTree<TileMapChunk> chunkTree;
	
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
		pos = new Vector2(origin);
		
		this.bounds = new Rectangle(width, height, pos);
		this.chunkTree = new CRQuadTree<TileMapChunk>(2,bounds);
		JSONObject chunkSize = json.getJSONObject("chunksize");
		
		this.tileMap = new int[columns][rows];
		this.name = name == null? json.getString("name"):name;
		
		chunkCellsPerRow = chunkSize.getInt("cellsPerRow");
		chunkCellsPerColumn = chunkSize.getInt("cellsPerColumn");
		this.chunkWidth = chunkCellsPerRow;
		this.chunkHeight = chunkCellsPerColumn;
		double nChunkRowsTest = rows/chunkCellsPerRow;
		double nChunkColumnsTest = columns/chunkCellsPerColumn;
		
		
		if (nChunkRowsTest % 1 != 0) throw new EngineException("TileMapChunk: property cellsPerRow must be a multiple of tilemap rows");
		if (nChunkRowsTest % 1 != 0) throw new EngineException("TileMapChunk: property cellsPerColumn must be a multiple of tilemap columns");
		nChunkRows = (int) nChunkRowsTest;
		nChunkColumns = (int) nChunkColumnsTest;
		
		JSONArray tileMapData = json.getJSONArray("map");
		
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
		
		JSONArray bgtiles = json.getJSONArray("bgtiles");
		if (bgtiles.length() > 0)
		{
			backgroundTiles = new int[bgtiles.length()];
			for (int i = 0; i < backgroundTiles.length; i++) 
			{
				backgroundTiles[i] = bgtiles.getInt(i);
			}
			
		}
		subdivide();
		
	}
	
	public TileMap(TileMapImageSet set, String name, int rows, int columns, Vector2 origin)
	{
		this.rows = rows;
		this.name = name;
		this.columns = columns;
		this.cellWidth = set.getCellWidth();
		this.cellHeight = set.getCellHeight();
		this.imageSet = set;
		this.pos = origin;
		this.width = rows*cellWidth;
		this.height = rows*cellHeight;
		this.tileMap = new int[columns][rows];
		this.bounds = new Rectangle(width, height, origin);
		this.chunkTree = new CRQuadTree<TileMapChunk>(2,bounds);
		
		determineChunkSize();
		this.chunkWidth = chunkCellsPerRow;
		this.chunkHeight = chunkCellsPerColumn;
		double nChunkRowsTest = rows/chunkCellsPerRow;
		double nChunkColumnsTest = columns/chunkCellsPerColumn;
		
		
		if (nChunkRowsTest % 1 != 0) throw new EngineException("TileMapChunk: property cellsPerRow must be a multiple of tilemap rows");
		if (nChunkRowsTest % 1 != 0) throw new EngineException("TileMapChunk: property cellsPerColumn must be a multiple of tilemap columns");
		nChunkRows = (int) nChunkRowsTest;
		nChunkColumns = (int) nChunkColumnsTest;
		subdivide();
	}
	
	private void determineChunkSize()
	{
		/*
		LinkedList<Integer> rowFactors = engine.util.MathUtils.factors(rows);
		if (rowFactors.size() > 2)
		{
			
		}
		else //only factors are 1 and n
		{
			chunkCellsPerRow = rowFactors.pop(); //higher factor at end of list
		}
		*/
		chunkCellsPerRow = rows;
		chunkCellsPerColumn = columns;
		
	}
	
	public int rows()
	{
		return rows;
	}
	
	public int columns()
	{
		return columns;
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
		if (chunks != null) clearChunks();
		this.chunks = new TileMapChunk[nChunkColumns][nChunkRows];
		int width = chunkWidth*cellWidth, height = chunkHeight*cellHeight;
		for (int col = 0; col < chunks.length; col++) 
		{
			for (int row = 0; row < chunks[0].length; row++) 
			{
				
				int[][] chunkData = new int[chunkHeight][chunkWidth];
				copyChuckData(chunkData, col, row);			
				TileMapChunk chunk = new TileMapChunk(this, chunkWidth*cellWidth, chunkHeight*cellHeight, chunkData,this.backgroundTiles);
				chunks[col][row] = chunk;
				int x = row * width;
				int y = col * height;
				Rectangle chunkbounds = new Rectangle(x,y,width,height);
				chunk.setBound(chunkbounds);
				chunkTree.put(chunkbounds, chunk);
				chunks[col][row]=chunk;
			}
		}
	}
	
	private void clearChunks() {
		
		for (int i = 0; i < chunks.length; i++) 
		{
			for (int j = 0; j < chunks[0].length; j++) 
			{
				if (chunks[i][j] != null)chunks[i][j].free();
				chunks[i][j]=null;
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
		if (id == 0) 
		{
			return null;
		}
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
		LinkedList<TileMapChunk> nodes = chunkTree.queryCollisions(bounds);
		for (TileMapChunk chunk : nodes)
		{
			BufferedImage img = chunk.getRenderedChuck();
			Rectangle chunkbounds = chunk.getBounds();
			
			Vector2 pos = chunkbounds.getPosition();
			g2.drawImage(img, (int)pos.getX(), (int)pos.getY(), null);
			if (drawChunkBoxes) GraphicsUtils.drawRect(g2, chunkbounds);
		}
		//drawAllBorders(g2);
	}

	
	public Rectangle renderBoundingArea() 
	{
		// TODO Auto-generated method stub
		return bounds;
	}

	@Override
	public JSONObject serialize() {
		JSONObject object = new JSONObject();
		object.put("name", name);
		object.put("rows", rows);
		object.put("columns", columns);
		object.put("imageset", imageSet.getName());
		JSONObject chunksize = new JSONObject();
		chunksize.put("cellsPerRow", this.chunkCellsPerRow);
		chunksize.put("cellsPerColumn", this.chunkCellsPerColumn);
		object.put("chunksize", chunksize);
		object.put("origin", pos.serialize());
		object.put("type", "tilemap");
		
		JSONArray map = new JSONArray();
		
		for (int i = 0; i < tileMap.length; i++) 
		{
			JSONArray row = new JSONArray();
			for (int j = 0; j < tileMap[0].length; j++) 
			{
				row.put(tileMap[i][j]);
			}
			map.put(row);
		}
		object.put("map", map);
		
		JSONArray bgtiles = new JSONArray();
		if (backgroundTiles != null)
		{
			for (int i = 0; i < backgroundTiles.length; i++) 
			{
				bgtiles.put(backgroundTiles[i]);
			}
		}
		object.put("bgtiles", bgtiles);
		
		return object;
	}

	public void updateChunks() 
	{	
		if (chunks == null)
		{
			subdivide();
			return;
		}
		for (int i = 0; i < chunks.length; i++) 
		{
			for (int j = 0; j < chunks[0].length; j++) 
			{
				int[][] chunkData = new int[chunkHeight][chunkWidth];
				copyChuckData(chunkData, i, j);
				chunks[i][j].setChunk(chunkData);
			}
		}
	}
	
	
	
	/**
	 * all tiles except for 0
	 * @return
	 */
	public CRQuadTree<Rectangle> toQuadTree()
	{
		CRQuadTree<Rectangle> tree = new CRQuadTree<Rectangle>(2, bounds);
		for (int i = 0; i < tileMap.length; i++) 
		{
			for (int j = 0; j < tileMap[0].length; j++) 
			{
				int tile = tileMap[i][j];
				if (tile != 0)
				{
					double x = j * cellWidth;
					double y = i * cellHeight;
					Rectangle rect = new Rectangle(x, y, cellWidth, cellHeight);
					tree.put(rect, rect);
				}
			}
		}
		
		return tree;
	}
	
	
	public void drawAllBorders(Graphics2D g2)
	{
		for (int i = 0; i < chunks.length; i++) 
		{
			for (int j = 0; j < chunks[0].length; j++) 
			{
				GraphicsUtils.drawRect(g2, chunks[i][j].getBounds());
			}
		}
	}

	public TileMapImageSet getImageSet() {
		return imageSet;
	}

	public void setBackgroundTiles(int[] tiles) 
	{
		this.backgroundTiles = tiles;
		
		for (int i = 0; i < chunks.length; i++) 
		{
			for (int j = 0; j < chunks[0].length; j++) 
			{
				chunks[i][j].setBgTiles(backgroundTiles);
			}
		}
	}
}
