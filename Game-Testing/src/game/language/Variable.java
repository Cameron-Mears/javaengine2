package game.language;

public class Variable
{
	private boolean isExplictType; //defined as a certain type cannot change
	private boolean isPrimitive; //prim or string
	private Instance pointerTo;
	private Type type;
	
	public Type getType()
	{		
		return type;
	}
	
	public Instance getValue()
	{
		return pointerTo;
	}
	
	public void assign(Instance value)
	{
		if (!LanguageUtils.instanceOf(value.getType(), type))
		{
			if (isExplictType); //ExplictTypeError
			
			this.type = value.getType();
			this.pointerTo = value;
			this.isPrimitive = value.isPrimitive();
		}
	}
	
	public boolean isPrimitive()
	{
		return isPrimitive;
	}
	
	 
}