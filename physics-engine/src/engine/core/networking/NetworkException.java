package engine.core.networking;

public class NetworkException extends Exception
{
	private static final long serialVersionUID = -10971075258341693L;
	
	public NetworkException(String message)
	{
		super(message);
	}
	
	public NetworkException(Exception e)
	{
		super(e);
	}
}
