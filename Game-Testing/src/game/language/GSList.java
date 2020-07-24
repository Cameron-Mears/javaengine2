package game.language;

import java.util.LinkedList;

public class GSList 
{
	private Type type;
	private LinkedList<Variable> list;
	private Instance instanceType;
	
	
	
	
	public boolean isValidType(Variable var)
	{
		return LanguageUtils.instanceOf(var.getType(), type);
	}
	
	public void add(Variable var)
	{
		if (!isValidType(var))
		{
			
		}
		else
		{
			list.add(var);
		}
	}
	
	public void fill(GSList other)
	{
		
	}
}
