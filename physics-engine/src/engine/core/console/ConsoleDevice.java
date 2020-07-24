package engine.core.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

public class ConsoleDevice
{
	private IODevice device;
	private LinkedList<String> history;
	
	private static class IODevice
	{
		BufferedWriter writer;
		BufferedReader reader;
		IODevice()
		{
			Console console = System.console();
			
			if (console == null)
			{
				initInput(System.in);
				initOutput(System.out);
				return;
			}
			
			reader = new BufferedReader(console.reader());
			writer = new BufferedWriter(console.writer());
		}
		
		private void initInput(InputStream s)
		{
			reader = new BufferedReader(new InputStreamReader(s));
		}
		
		private void initOutput(OutputStream s)
		{
			writer = new BufferedWriter(new OutputStreamWriter(s));
		}
		
		public String readLine() throws IOException
		{
			synchronized (reader) {
				return reader.readLine();
			}
		}
		
		public void writeLine(String s) throws IOException
		{
			synchronized (writer) {
				writer.write(s);
				writer.newLine();
			}
		}
		
		public void flush() throws IOException
		{
			synchronized (writer) {
				writer.flush();
			}
		}
	}
	
	public ConsoleDevice()
	{
		this.device = new IODevice();
	}
	
	public String nextLine() throws IOException
	{
		return device.readLine();
	}
	
	public void writeLine(String s) throws IOException
	{
		device.writeLine(s);
	}
	
	public void flush() throws IOException
	{
		device.flush();
	}
	
	
	
}
