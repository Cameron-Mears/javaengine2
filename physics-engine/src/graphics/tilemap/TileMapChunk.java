package graphics.tilemap;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import engine.core.exceptions.EngineException;
import graphics.image.CompatibleImageFactory;
import physics.collision.HitBox;

/*
 * Subdivide tilemap into chunks to reduce render calls
 */

public class TileMapChunk 
{
	private int width;
	private int height;
	private int cellWidth, cellHeight;
	
	private TileMap parent;
	
	private BufferedImage chunkImage;
	
	public TileMapChunk(TileMap parent, int width, int height, int[][] chunkData) throws EngineException
	{
		this.height = height;
		this.width = width;
		this.parent = parent;
		this.cellHeight = parent.getCellHeight();
		this.cellWidth = parent.getCellWidth();
		this.setChunk(chunkData);
	}
	
	public void setChunk(int[][] chunkData) throws EngineException
	{
		if (chunkData.length*cellHeight != height) throw new EngineException("TileMapChunk: chunckData height must be equal to tileMapChuck height");
		if (chunkData[0].length*cellWidth != width) throw new EngineException("TileMapChunk: chunckData width must be equal to tileMapChuck width");
		
		//create an image to draw the chunk on
		chunkImage = CompatibleImageFactory.createCompatibleImage(width, height, CompatibleImageFactory.DEFAULT_TRANSPARENCY);
		Graphics2D g2 = chunkImage.createGraphics();
		
		//iterate through the chunk data
		for (int col = 0; col < chunkData.length; col ++) 
		{
			for (int row = 0; row < chunkData[col].length; row ++) 
			{
				if (chunkData[col][row] != 0)
				{
					BufferedImage tile = parent.getTileByID(chunkData[col][row]);
					g2.drawImage(tile, row*cellWidth, col*cellHeight, null);
				}
			}
		}
		g2.dispose();
	}
	
	public BufferedImage getRenderedChuck()
	{
		return chunkImage;
	}

}
