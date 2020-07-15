package engine.core.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.crypto.NoSuchPaddingException;

import engine.core.networking.crypto.EncryptionStream;
import engine.core.random.Rand;
import engine.util.json.JSONSerializable;
import external.org.json.JSONObject;

public class Connection implements SocketListener
{
	private Socket socket;
	private LinkedList<JSONObject> inbound;
	private boolean hasNewItem;
	private long connectionID;
	private BufferedReader reader;
	private static final Charset UTF_8 = Charset.forName("UTF-8");
	private EncryptionStream stream;
	
	public Connection(Socket socket) throws NetworkException
	{
		if (socket == null) throw new NetworkException("Null Socket.");
		this.socket = socket;
		try
		{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (IOException e) {throw new NetworkException(e);}
		inbound = new LinkedList<JSONObject>();
		do
		{
			connectionID = Rand.randomLong();
		} while (NetworkManager.connectionTable.get(connectionID) != null);
		NetworkManager.getInstance().addConnection(this);
		
	}
	
	public void write(JSONObject object) throws NetworkException
	{
		if (stream == null) throw new NetworkException("not initizlaed");
		if (object == null) throw new NetworkException("Null Object.");
		String jsonString = object.toString()+'\n'; //new line for scanner when reading
		try
		{
			OutputStream socketStream = socket.getOutputStream();
			socketStream.write(jsonString.getBytes(UTF_8));
		} catch (IOException e) {
			throw new NetworkException(e);
		}
		
	}
	
	long getID()
	{
		return connectionID;
	}
	
	public void write(JSONSerializable serializable) throws NetworkException
	{
		if (serializable == null) throw new NetworkException("Null Object.");
		write(serializable.serialize());		
	}
	
	public void flush() throws NetworkException
	{
		try
		{
			socket.getOutputStream().flush();
		} catch (Exception e) {
			throw new NetworkException(e);
		}
	}
	
	public void close(boolean flush) throws NetworkException
	{	try
		{
			if (flush) socket.getOutputStream().flush();
			socket.close();
		} catch (Exception e) {
			throw new NetworkException(e);
		}
	}

	@Override
	public void newData() throws NetworkException
	{	
		try
		{
			synchronized (this) 
			{				
				InputStream socketStream = socket.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(socketStream);
				while (reader.ready())
				{
					JSONObject object = new JSONObject(reader.readLine());
					if (object == null || !(object instanceof JSONObject)) throw new NetworkException("Error reading Object");
					inbound.add((JSONObject) object);
					hasNewItem = true;
				};
			}
		}
		catch (Exception e) {throw new NetworkException(e);}
	}
	
	/**
	 * 
	 * @return the next the last unread message, (front of queue), if there no messages in the queue this will wait for the next message
	 * try connection.next() for the next object without waiting for a new object
	 */
	public JSONObject getNext() throws NetworkException
	{
		JSONObject next = null;
		try
		{
			next =  inbound.poll();
			if (next == null) next = new JSONObject(reader.readLine());			
		}
		catch (Exception e) {throw new NetworkException(e);}
		return next;
	}
	
	/**
	 * the next element in the inbound packet queue, null if there are no items in the queue
	 * @return
	 */
	public JSONObject next()
	{
		return inbound.poll();
	}
	
	/**
	 * 
	 * @return the newest received message, null if there are no newer messages
	 */
	public JSONObject getNewest()
	{
		if (hasNewItem)
		{
			hasNewItem = false;
			return inbound.pop();
		}
		return null;
	}
	
	
	Socket getSocket()
	{
		return socket;
	}
	
	void init(Key aeskey) throws Exception
	{
		this.stream = new EncryptionStream(aeskey);
		reader = new BufferedReader(stream.decrypt(socket.getInputStream()));
	}
	
}
