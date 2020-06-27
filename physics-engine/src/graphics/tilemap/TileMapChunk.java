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
	private int[][] tileIds;
	
	private TileMap parent;
	
	private HitBox hitbox;
	
	private BufferedImage chunkImage;
	
	public TileMapChunk(TileMap parent, int width, int height, int[][] chunkData) throws EngineException
	{
		this.height = height;
		this.width = width;
		this.parent = parent;
		this.cellHeight = parent.getCellHeight();
		this.cellWidth = parent.getCellWidth();
		this.setChunck(chunkData);
	}
	
	public void setChunck(int[][] chunkData) throws EngineException
	{
		if (chunkData.length*cellWidth != width) throw new EngineException("TileMapChunk: chunckData width must be equal to tileMapChuck width");
		if (chunkData[0].length*cellHeight != height) throw new EngineException("TileMapChunk: chunckData height must be equal to tileMapChuck height");
		
		//create an image to draw the chunk on
		chunkImage = CompatibleImageFactory.createCompatibleImage(width, height, Transparency.BITMASK);
		Graphics2D g2 = chunkImage.createGraphics();
		
		//iterate through the chunk data
		for (int row = 0; row < chunkData.length; row ++) 
		{
			for (int col = 0; col < chunkData.length; col ++) 
			{
				if (chunkData[row][col] != 0)
				{
					BufferedImage tile = parent.getTileByID(chunkData[row][col]);
					g2.drawImage(tile, row*cellWidth, row*cellHeight, null);
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
