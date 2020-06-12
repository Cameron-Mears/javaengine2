package physics.body;

public class MassData 
{
	private double mass;
	private double inv_mass;
	
	
	public MassData(double mass)
	{
		this.mass = mass;
		this.inv_mass = 1.0d/mass;
	}
	
	public double getInvMass()
	{
		return inv_mass;
	}
}
