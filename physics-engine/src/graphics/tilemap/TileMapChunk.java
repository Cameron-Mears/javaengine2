package graphics.tilemap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import engine.core.exceptions.EngineException;
import engine.core.random.Rand;
import graphics.image.CompatibleImageFactory;
import graphics.tilemap.TileMapImageSet.ImgNode;
import physics.collision.HitBox;
import physics.collision.Rectangle;

/*
 * Subdivide tilemap into chunks to reduce render calls
 */

public class TileMapChunk 
{
	private int width;
	private int height;
	private int cellWidth, cellHeight;
	
	private TileMap parent;
	private int[][] data;
	private int[][] bgData;
	private Rectangle bounds;
	private BufferedImage chunkImage;
	private boolean useBuffer;
	private boolean wasBufferused;
	private BufferedImage chunkImageBuffered;
	private int[] bgtiles;
	private Color bgColor=Color.white;
	
	public TileMapChunk(TileMap parent, int width, int height, int[][] chunkData, int[] bgtiles) throws EngineException
	{
		this.height = height;
		this.width = width;
		this.parent = parent;
		this.cellHeight = parent.getCellHeight();
		this.cellWidth = parent.getCellWidth();
		this.setChunk(chunkData);
		this.bgtiles = bgtiles;
	}
	
	public void setBgTiles(int[] bgtiles)
	{
		this.bgtiles = bgtiles;
		for (int i = 0; i < bgData.length; i++) 
		{
			for (int j = 0; j < bgData[i].length; j++) {
				bgData[i][j]=0;
			}
		}
		useBuffer = true;
	}
	
	public void setBgColor(Color color)
	{
		this.bgColor = color;
	}
	
	public void setChunk(int[][] chunkData) throws EngineException
	{
		this.data = chunkData;
		if (bgData == null) this.bgData = new int[chunkData.length][chunkData[0].length];
		if (chunkData.length*cellHeight != height) throw new EngineException("TileMapChunk: chunckData height must be equal to tileMapChuck height");
		if (chunkData[0].length*cellWidth != width) throw new EngineException("TileMapChunk: chunckData width must be equal to tileMapChuck width");
		
		boolean isInit = chunkImage == null;
		
		if (chunkImageBuffered == null) chunkImageBuffered = CompatibleImageFactory.createCompatibleImage(width, height, CompatibleImageFactory.DEFAULT_TRANSPARENCY);
		//create an image to draw the chunk on
		if (chunkImage == null)chunkImage = CompatibleImageFactory.createCompatibleImage(width, height, CompatibleImageFactory.DEFAULT_TRANSPARENCY);
		Graphics2D g2 = chunkImage.createGraphics();
		draw(g2);
		g2.dispose();
		
		
		if (isInit)
		{
			g2 = chunkImageBuffered.createGraphics();
			draw(g2);
			g2.dispose();
		}
		else
		{
			useBuffer = true;
		}
		
		if (!isInit)
		{
			if (useBuffer)
			{
				useBuffer = false;
				wasBufferused = true;
			}
		}
		
	}
	
	private void draw(Graphics2D g2)
	{
		if (bgtiles != null)
		{
			if(bgtiles.length == 0) g2.setColor(bgColor == null? Color.white:bgColor);
		}
		g2.fillRect(0, 0, width, height);
		//iterate through the chunk data
		for (int col = 0; col < data.length; col ++) 
		{
			for (int row = 0; row < data[col].length; row ++) 
			{
				
				BufferedImage tile = parent.getTileByID(data[col][row]);
				if (bgtiles != null)
				{
					if (bgData[col][row] == 0)
					{
						bgData[col][row] = Rand.randElement(bgtiles);
					}
				}
				BufferedImage bgimg = parent.getTileByID(bgData[col][row]);
				if (bgimg != null) g2.drawImage(bgimg, row*cellWidth, col*cellHeight, null);
				g2.drawImage(tile, row*cellWidth, col*cellHeight, null);
				
			}
		}
		g2.dispose();
	}
	
	public void updateBgTiles()
	{
		
	}
	
	public BufferedImage getRenderedChuck()
	{
		if (useBuffer)
		{
			Graphics2D g2 = chunkImage.createGraphics();
			draw(g2);
			g2.dispose();
			useBuffer = false;
			wasBufferused = true;
			return chunkImageBuffered;
		}
		if (wasBufferused)
		{
			Graphics2D g2 = chunkImageBuffered.createGraphics();
			draw(g2);
			g2.dispose();
			wasBufferused = false;
		}
		return chunkImage;
	}

	void setBound(Rectangle bounds)
	{
		this.bounds = bounds;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * help gc when remaking tilemaps
	 */
	void free() 
	{
		chunkImage = null;	
	}

}
