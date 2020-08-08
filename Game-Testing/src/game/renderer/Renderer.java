package game.renderer;

import java.awt.Graphics2D;
import java.util.HashMap;

import engine.core.instance.EngineInstance;
import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import graphics.Camera;
import graphics.instance.IGraphics;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;
import physics.collision.Rectangle;

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
	}
	
	/**
	 * 
	 * @param id the id of the camera, lower id cameras
	 * @param camera
	 */
	
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
			
			
		
	}

	@Override
	public Rectangle renderBoundingArea() {
		// TODO Auto-generated method stub
		return null;
	}

}
