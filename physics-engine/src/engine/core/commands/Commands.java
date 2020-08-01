package engine.core.commands;

import java.io.Console;
import java.text.NumberFormat;
import java.util.concurrent.ConcurrentHashMap;

import engine.core.Engine;

public final class Commands 
{
	private ConcurrentHashMap<String, ConsoleCommand> commands;
	private ConcurrentHashMap<String, ConcurrentHashMap<String, ConsoleCommand>> subCommands;
	private ConsoleCommand exit = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) 
		{
			System.exit(0);
			return null;
		}
	};
	
	private ConsoleCommand changeDrawFunc = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) {
			Engine.getInstance().setProperty("drawFunc", args[0]);
			return "";
		}
	};
	
	private ConsoleCommand changeTickRate = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) 
		{
			if (args.length < 1) return tooFewArguments(1);
			int value;
			try
			{
				value = Integer.parseInt(args[0]);
				if (value <= 0) return numberTooSmallError(value, 0);
				Engine.getInstance().setProperty("tickrate", value);
			}
			catch (NumberFormatException e)
			{
				return numberFormatError(e, args[0]);
			}
			
			return "Tickrate Succesfully Changed to " + numberToString(value);
		}
	};
	
	private ConsoleCommand printRates = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) {
			boolean b = Boolean.parseBoolean(args[0]);
			Engine.getInstance().setProperty("printRates", b);
			return null;
		}
	};
	
	private ConsoleCommand changeFrameRate = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) 
		{
			if (args.length < 1) return tooFewArguments(1);
			int value;
			try
			{
				value = Integer.parseInt(args[0]);
				if (value <= 0) return numberTooSmallError(value, 0);
				Engine.getInstance().setProperty("framerate", value);
			}
			catch (NumberFormatException e)
			{
				return numberFormatError(e, args[0]);
			}
			
			return "Framerate Succesfully Changed to " + numberToString(value);
		}
	};
	
	private ConsoleCommand addInstance = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) 
		{
			if (args.length < 1) return tooFewArguments(1);
			int value;
			try
			{
				value = Integer.parseInt(args[0]);
				if (value <= 0) return numberTooSmallError(value, 0);
				Engine.getInstance().setProperty("framerate", value);
			}
			catch (NumberFormatException e)
			{
				return numberFormatError(e, args[0]);
			}
			
			return "Framerate Succesfully Changed to " + numberToString(value);
		}
	};
	
	private String tooFewArguments(int numArgs)
	{
		return "Error -> this commands needs at least: " + Integer.toString(numArgs) + " arguments";
	}
	
	private String numberFormatError(NumberFormatException e, String arg)
	{
		try
		{
			StackTraceElement[] stacktrace = e.getStackTrace();
			if (stacktrace.length > 1)
			{
				Class<?> number = Class.forName(stacktrace[1].getClassName());
				String[] className = number.getName().split("\\.");
				String name = className[className.length-1];
				Number maxValue = (Number) number.getField("MAX_VALUE").get(null);//static field
				return "Error -> entered value: " + arg + " is either not an number of type " + name + " or is greater than the max value of that number : " + numberToString(maxValue); 
				
			}
		} catch (Exception e1) {}
		return "error";
	}
	
	private String numberTooLargeError(Number number, Number maxValue)
	{
		return "Error -> entered value: " + numberToString(number) + ", the command paramater must be less than " + numberToString(maxValue);
	}
	
	private String numberTooSmallError(Number number, Number minValue)
	{
		return "Error -> entered value: " + numberToString(number) + ", the command paramater must be greater than " + numberToString(minValue);
	}
	
	private String numberToString(Number number)
	{
		if (number instanceof Float || number instanceof Double)
		{
			return Double.toString(number.doubleValue());
		}
		else
		{
			return Long.toString(number.longValue());
		}
	}
	
	public String exucuteCommand(String command, String... args)
	{
		return commandFeedback(parseCommand(command, args), command);
	}
	
	private String commandFeedback(String feedback, String command)
	{
		if (command == null) command="";
		return "[Command Feedback][" + command + "]: " + feedback;
	}
	
	private String parseCommand(String command, String... args)
	{
		ConsoleCommand consoleCommand = getCommand(command);
		if (consoleCommand == null) return commandDoesNotExist(command);
		return consoleCommand.exucute(args);
	}
	
	private String commandDoesNotExist(String command)
	{
		return "the Command " + command + " does not exist";
	}
	
	private ConsoleCommand getCommand(String command)
	{
		String[] commandInfo = command.split("\\.");
		ConsoleCommand consoleCommand = null;
		if (commandInfo.length > 1)
		{
			if (commandInfo.length > 2) return null;
			ConcurrentHashMap<String, ConsoleCommand> commandMap = subCommands.get(commandInfo[0]);
			if (commandMap == null) return null;
			consoleCommand = commandMap.get(commandInfo[1]);
			
			if (consoleCommand == null) return null;
		}
		else
		{
			consoleCommand = commands.get(command);
			if (consoleCommand == null) return null;
		}
		
		return consoleCommand;
		
	}
	
	public Commands()
	{
		commands = new ConcurrentHashMap<String, ConsoleCommand>();
		commands.put("exit", exit);
		commands.put("draw", changeDrawFunc);
		commands.put("printRates",printRates);
		subCommands = new ConcurrentHashMap<String, ConcurrentHashMap<String,ConsoleCommand>>();
		subCommands.put("tick", new ConcurrentHashMap<String, ConsoleCommand>());
		subCommands.put("render", new ConcurrentHashMap<String, ConsoleCommand>());
		
		ConcurrentHashMap<String, ConsoleCommand> renderCommands = subCommands.get("render");
		renderCommands.put("setFrameRate", changeFrameRate);
		
		ConcurrentHashMap<String, ConsoleCommand> tickCommands = subCommands.get("tick");
		tickCommands.put("setRate", changeTickRate);
	}
}
