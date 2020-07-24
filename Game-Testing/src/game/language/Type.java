package game.language;

import java.util.Iterator;
import java.util.Stack;

public class Type 
{
	public static final String OBJECT = "O"; //1 char faster
	
	private Stack<String> typeStack; //for extending types
	
	private String type; //final type
	
	public Type(Stack<String> superClass, String type)
	{
		typeStack = new Stack<String>();
		if (superClass != null)
		{
			for (String superType : superClass) 
			{
				typeStack.push(superType);
			}
		} else typeStack.push(OBJECT);
		if (type != OBJECT) typeStack.push(type);
		
	}
	
	public String getType()
	{
		return type;
	}
	
	public Iterator<String> getTypeIter()
	{
		return null;
	}
}
