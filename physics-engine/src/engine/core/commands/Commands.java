package engine.core.commands;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import engine.core.Engine;
import graphics.layer.GraphicsLayer;
import graphics.layer.GraphicsLayerManager;

public final class Commands 
{
	private ConcurrentHashMap<String, ConsoleCommand> commands;
	private ConcurrentHashMap<String, ConcurrentHashMap<String, ConsoleCommand>> subCommands;
	private static Commands instance;
	public static Commands getInstance()
	{
		return instance;
	}
	private ConsoleCommand exit = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) 
		{
			System.exit(0);
			return null;
		}

		@Override
		public String syntax() {
			 return "Syntax -> | shutdown the engine";
		}
	};
	
	private ConsoleCommand changeDrawFunc = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) {
			Engine.getInstance().setProperty("drawFunc", args[0]);
			return "";
		}

		@Override
		public String syntax() {
			return "Syntax -> command does nothing";
		}
	};
	
	private ConsoleCommand setLayerHidden = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) 
		{
			if (args.length < 2) return tooFewArguments(2);
			String layer = args[0];
			GraphicsLayer gl = GraphicsLayerManager.getInstance().getLayer(layer);
			if  (gl == null) return "No such GraphicsLayer -> " + layer;
			boolean b = Boolean.parseBoolean(args[1]);
			gl.setHidden(b);
			return "GrphicsLayer -> " + layer + " succesfully " + ((b)?"hidden":"shown");
		}

		@Override
		public String syntax() {
			
			return "Syntax -> layer->string hidden->boolean | the name of the layer, if hidden is true the engine not render the layer";
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

		@Override
		public String syntax() {
			return "Syntax -> rate->int the rate to change the target tickrate to";
		}
	};
	
	private ConsoleCommand printRates = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) {
			boolean b = Boolean.parseBoolean(args[0]);
			Engine.getInstance().setProperty("printRates", b);
			return b? "Started Printing Rates":"Stopped Printing Rates";
		}

		@Override
		public String syntax() {
			return "Syntax -> printRates->boolean if true the engine will print rates to the console";
		}
	};
	
	private ConsoleCommand help = new ConsoleCommand() {
		
		@Override
		public String exucute(String... args) {
			String ret = "\n";
			Set<Entry<String,ConsoleCommand>> cmds = commands.entrySet();
			Set<Entry<String, ConcurrentHashMap<String, ConsoleCommand>>> subcmds = subCommands.entrySet();
			for (Entry<String, ConcurrentHashMap<String, ConsoleCommand>> entry : subcmds) {
				
				ret += "Command Subset -> " + entry.getKey() + "\n";
				ret += "\t" + printCommandSet(entry.getValue().entrySet()) + "\n";
			}
			return printCommandSet(cmds) + ret;
		}
		
		@Override
		public String syntax() {
			return "";
		}
	};
	
	private String printCommandSet(Set<Entry<String,ConsoleCommand>> commands)
	{
		String ret = "\n";
		for (Entry<String, ConsoleCommand> entry : commands) {
			ret += "Command: \"" +entry.getKey() + "\" \t" + entry.getValue().syntax()+"\n";
		}
		return ret;
	}
	
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

		@Override
		public String syntax() {
			return "Syntax -> rate->int the rate to change the target framerate to";
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

		@Override
		public String syntax() {
			return "Syntax -> command does nothing right now";
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
	
	public void addCommands(ConcurrentHashMap<String, ConsoleCommand> commands, boolean override, boolean disableOverrideWarnings)
	{
		if (commands == null)
		{
			Engine.printWarningMessage("The command list to add was null", this);
			return;
		}
		
		Set<Entry<String, ConsoleCommand>> set = commands.entrySet();
		
		for (Entry<String, ConsoleCommand> entry : set) 
		{
			if (entry.getValue() == null)
			{
				Engine.printWarningMessage("The command " + entry.getKey() + " contains a null commands", this);
				
			}
			if (this.commands.get(entry.getKey()) != null)
			{
				if (override)
				{
					this.commands.put(entry.getKey(), entry.getValue());
					if (!disableOverrideWarnings) Engine.printWarningMessage("Overwriting Command -> " + entry.getKey(), this);;
				}
			}
			this.commands.put(entry.getKey(), entry.getValue());
		}
	}
	
	public Commands()
	{
		instance = this;
		commands = new ConcurrentHashMap<String, ConsoleCommand>();
		commands.put("exit", exit);
		commands.put("draw", changeDrawFunc);
		commands.put("printRates",printRates);
		commands.put("help",help);
		subCommands = new ConcurrentHashMap<String, ConcurrentHashMap<String,ConsoleCommand>>();
		subCommands.put("tick", new ConcurrentHashMap<String, ConsoleCommand>());
		subCommands.put("render", new ConcurrentHashMap<String, ConsoleCommand>());
		
		ConcurrentHashMap<String, ConsoleCommand> renderCommands = subCommands.get("render");
		renderCommands.put("setFrameRate", changeFrameRate);
		renderCommands.put("setLayerHidden", this.setLayerHidden);
		
		ConcurrentHashMap<String, ConsoleCommand> tickCommands = subCommands.get("tick");
		tickCommands.put("setRate", changeTickRate);
	}
}
