package game.language;

import java.util.HashMap;
import java.util.Stack;

public class Program 
{
	private long size;
	private int maxStackSize;
	private HashMap<String, Variable> variables;
	
	
	private Stack<Function> stack;
	

	/**
	 * 
	 * @param program, the code in a string, pass with no indents, only spaces and lines
	 */
	private void load(String program)
	{
		
	}
	
	private void getStacktrace()
	{
		
	}
	
	public void functionCall()
	{
		if (stack.size() + 1 > maxStackSize) throw new StackOverflowError();
	}
}
