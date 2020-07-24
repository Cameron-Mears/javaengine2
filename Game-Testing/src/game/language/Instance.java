package game.language;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;


public class Instance
{
	private Instance parent;
	private boolean isPrimitive;
	private Object rootValue;
	private Type type;
	private final HashMap<String, Function> functions;
	private HashMap<String, Variable> variables;
	
	public Type getType()
	{
		return type;
	}
	
	public Instance(Type type, Instance parent, boolean isPrimitive, Object rootValue)
	{
		if (!isPrimitive)
		{
			this.functions = new HashMap<String, Function>();
			this.parent = parent;
			this.type = type;
		}
		else
		{
			this.functions = null;
			this.rootValue = rootValue;
		}
	}
	
	public Object getRootValue()
	{
		return rootValue;
	}
	
	public Variable get(String var)
	{
		return variables.get(var);
	}
	
	public void set(Variable value, String var)
	{
		variables.put(var, value);
	}
	
	public Variable invokeFunction(String function, Variable... args)
	{
		return null;
	}

	public boolean isPrimitive() 
	{
		return isPrimitive;
	}

	
}