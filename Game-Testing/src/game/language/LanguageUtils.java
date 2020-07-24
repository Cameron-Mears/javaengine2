package game.language;

import java.util.Iterator;

public class LanguageUtils 
{
	public static boolean instanceOf(Type test, Type type)
	{
		if (type.getType().equals("Instance")) return true;
		Iterator<String> typeIterator = test.getTypeIter();
		
		while (typeIterator.hasNext())
		{
			String temp = typeIterator.next();
			if (temp.equals("Instance")) return false; //hit root must be false
			
			if (temp.equals(type.getType())) return true;
			
		}
		return false;
	}
}
