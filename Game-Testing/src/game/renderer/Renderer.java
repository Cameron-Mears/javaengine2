package game.renderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

import engine.core.Engine;
import engine.core.instance.EngineInstance;
import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import graphics.Camera;
import graphics.instance.IGraphics;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class Renderer extends EngineInstance implements IGraphics
{
	
	private static Renderer instance;
	
	public static Renderer getInstance()
	{
		return instance;
	}

	private HashTreeMap<Long, Camera> cameras;
	private HashMap<String, Camera> camerasByName;
	private TraverseFunction<GraphicsLayer> traverseLayers;
	private TraverseFunction<Camera> traverseCameras;
	private GraphicsLayerManager layerManager;
	private Graphics2D g2;
	private Font drawRatesFont;
	
	
	private Camera activeCamera;
	
	
	public Camera getCamera(String name)
	{
		return camerasByName.get(name);
	}
	
	public Renderer()
	{
		instance = this;
		layerManager = GraphicsLayerManager.getInstance();
		layerManager.setRenderer(this);
		cameras = new HashTreeMap<Long, Camera>();
		camerasByName = new HashMap<String,Camera>();
		
		traverseLayers = (layer)->
		{
			activeCamera.capture(layer,g2);
		};
		
		traverseCameras = (camera)->{
			
			activeCamera = camera;
			layerManager.getAllLayers().inOrderTraverse(traverseLayers);
			
		};
		
		drawRatesFont = new Font("Arial", Font.BOLD, 18);
	}
	
	/**
	 * 
	 * @param id the id of the camera, lower id cameras
	 * @param camera
	 */
	
	
	public Camera getActiveCamera()
	{
		return activeCamera;
	}
	
	public void addCamera(long id, Camera camera)
	{
		cameras.put(id, camera);
		camerasByName.put(camera.getName(), camera);
	}
	
	@Override
	public void render(Graphics2D g2) 
	{
		this.g2 = g2;
		
		cameras.inOrderTraverse(traverseCameras);
		
		if ((boolean) Engine.getInstance().getProperty("drawRates"))
		{
			g2.setTransform(new AffineTransform());
			g2.setFont(drawRatesFont);
			g2.setColor(Color.black);
			String fps = Integer.toString((int)Engine.getInstance().getProperty("lastFrameRate"));
			String tps = Integer.toString((int)Engine.getInstance().getProperty("lastTickRate"));
			g2.drawString("FPS: " + fps, 20, g2.getFontMetrics().getHeight()+30);
			g2.drawString("TPS: " + tps, 20, g2.getFontMetrics().getHeight()*2+30);
		}
			
		
	}

	@Override
	public Rectangle renderBoundingArea() {
		// TODO Auto-generated method stub
		return null;
	}

	public void translateCord(Vector2 mouse) 
	{	
		if (activeCamera != null && mouse != null)
		{
			mouse.add(activeCamera.getPosition().x, activeCamera.getPosition().y);
		}
	}

}
