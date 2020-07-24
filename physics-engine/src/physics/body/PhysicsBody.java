package physics.body;

import engine.core.tick.TickInfo;
import physics.collision.HitBox;
import physics.collision.Shape;
import physics.general.Transform;
import physics.general.Vector2;

public class PhysicsBody 
{
	private MassData massData;
	private Material material;
	private HitBox hitbox;
	private Transform tx;
	private Vector2 velocity;
	private Vector2 force;
	
	
	public PhysicsBody(MassData md, Material mat, Transform tx)
	{
		this.massData = md;
		this.material = mat;
		this.tx = tx;
		this.force = new Vector2(0,0);
		this.velocity = new Vector2(0,0);
	}
	
	public PhysicsBody(MassData md, Material mat, Transform tx, Vector2 vel, Vector2 force)
	{
		this(md, mat, tx);
		this.force = force;
		this.velocity = vel;
	}
	
	
	public void applyForce(Vector2 force)
	{
		this.force.add(force);
	}
	
	public void applyForce(double x, double y)
	{
		force.addX(x);
		force.addY(y);
	}
	
	public void tick(TickInfo info)
	{
		double acceleration_x = force.getX() * massData.getInvMass();
		double acceleration_y = force.getY() * massData.getInvMass();
		
		velocity.addX(acceleration_x * info.delta);
		velocity.addY(acceleration_y * info.delta);
		
		tx.getPosition().add(velocity.scaled(info.delta));
		
		this.force.set(0, 0);
	}
	
	
	public Vector2 getVelocity()
	{
		return velocity;
	}
	
	public Vector2 getPosition()
	{
		return tx.getPosition();
	}
	
}
