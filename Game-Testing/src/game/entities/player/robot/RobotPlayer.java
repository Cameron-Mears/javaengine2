package game.entities.player.robot;

import java.awt.Graphics2D;
import java.util.ArrayList;

import engine.core.tick.TickInfo;
import game.entities.player.PlayerTemplate;
import game.renderer.Renderer;
import game.weapons.Weapon;
import game.weapons.laser.GammaRayLaser;
import graphics.Camera;
import graphics.layer.GraphicsLayerManager;
import physics.body.PhysicsBody;
import physics.collision.HitBox;
import physics.collision.Rectangle;
import physics.general.Vector2;

public class RobotPlayer extends PlayerTemplate
{
	private GammaRayLaser weapon;
	private Drone drone;
	
	private ArrayList<Weapon> weapons;
	private int selectedWeapon;
	
	public RobotPlayer() {
		this.addToLayers(null, null, "default");
		GraphicsLayerManager.getInstance().getLayer("turrets").add(this);
		weapon = new GammaRayLaser();
	}
	
	@Override
	public void onTick(TickInfo info)
	{
		move(info);
		Camera camera = Renderer.getInstance().getActiveCamera();
		if (camera != null)camera.setPosition(getPosition().x - camera.getBounds().getWidth()/2, getPosition().y - camera.getBounds().getHeight()/2);
		int scrolls = input.scrollDelta();
		int newIndex = selectedWeapon + scrolls;
		if (newIndex < 0)
		{
			selectedWeapon = (weapons.size()-1) + (newIndex % weapons.size()-1);
		}
		
	}

	@Override
	public void render(Graphics2D g2) 
	{
		g2.drawImage(sprCurrent.getCurrentFrame(), (int)getPosition().x, (int)getPosition().y, null);
		weapon.render(g2, getPosition().x,  getPosition().y);
	}

	@Override
	public Rectangle renderBoundingArea() 
	{		
		return bounds;
	}

	@Override
	public HitBox getHitBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCollision(HitBox other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PhysicsBody getPhysicsBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2 getPosition() {
		return body.getPosition();
	}

	@Override
	public void actionSkill() 
	{
		
	}
	
}
