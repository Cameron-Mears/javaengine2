package game.language;

import java.util.HashMap;
import java.util.TreeMap;

public class Function 
{
	private static enum LINE_PURPOSE
	{
		ASSIGMENT,
		MATH,
		FUNCTION_CALL,
		OTHER
	}
	
	private HashMap<String, Variable> localVariables;
	private Instance caller;
	private HashMap<String, Variable> paramaters;
	
	private void nextLine(String line)
	{
		line = line.replaceAll(" ", "");
		String[] vars = line.split("=")[0].split("\\,");
		
		if (vars.length != 2);//error
		for (String string : vars) 
		{
			Variable var = find(string);				
		}
	}
	
	private Variable find(String name)
	{
		Variable var = null;
		var = caller.get(name);
		if (var == null) paramaters.get(name);
		if (var == null) localVariables.get(name);
		if (var == null) //variable not definined in function paramters in calling instance therefore it is a new local variable
		{
			var = new Variable();
			localVariables.put(name, var);
		}
		
		return var;
	}
	
	private LINE_PURPOSE[] getPurpose(String line)
	{
		if (line.contains("="))
		{
			if (line.contains("+="))
			{
				
			}
			else
			{
				
			}
			return null;
		}
		return null;
	}
}
