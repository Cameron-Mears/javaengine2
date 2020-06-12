package graphics.instance;

public class InvalidInstanceException extends Exception 
{

	public InvalidInstanceException(String className)
	{
		super("The class \"" + className + "\" is not a subclass of EngineInstance");
	}
	
	private static final long serialVersionUID = 7364789888653929954L;

}
