package engine.core.console;

import java.io.IOException;
import java.util.Scanner;

import engine.core.commands.Commands;

public class ConsoleHandler 
{
	private ConsoleDevice console;
	private boolean running;
	private Commands commands;
	
	public ConsoleHandler()
	{
		console = new ConsoleDevice();
		running = true;
		commands = new Commands();
		new Thread(()->{
			
			start();
			
		}).start();
	}
	
	public void stop()
	{
		running = false;
	}
	
	public void start()
	{
		while (running)
		{
			try {
				String nextLine = console.nextLine();
				String[] split = nextLine.split(" ");
				
				String command = split[0];
				String[] commandArgs = new String[split.length-1];
				System.arraycopy(split, 1, commandArgs, 0, commandArgs.length);
				
				String feedback = commands.exucuteCommand(command, commandArgs);
				synchronized (console) {
					console.writeLine(feedback);
					console.flush();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
