package physics;

public class PhysicsException extends RuntimeException
{
	private static final long serialVersionUID = -2087620765815612007L;
	
	public PhysicsException(String message)
	{
		super(message);
	}
	
	public PhysicsException(Exception error)
	{
		super(error);
	}
	
}
