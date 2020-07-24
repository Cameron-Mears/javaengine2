package engine.core.networking;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

import engine.util.tree.HashTreeMap;
import engine.util.tree.TraverseFunction;
import external.org.json.JSONArray;
import external.org.json.JSONObject;

public class NetworkManager 
{
	
	static HashMap<Long, Connection> connectionTable;
	private HashMap<InetAddress, Boolean> bannedIPs;
	private static boolean runAsServer = false;
	private static NetworkManager instance;
	private KeyPairGenerator rsaKeyGenerator;
	
	static
	{
		connectionTable = new HashMap<Long, Connection>();
	}
	
	public static synchronized NetworkManager getInstance()
	{
		if (instance == null) instance = new NetworkManager();
		return instance;
	}
	
	private HashTreeMap<Long, Connection> connections;
	private TraverseFunction<Connection>travese;
	private Thread acceptConnections;
	
	private NetworkManager()
	{
		
		connections = new HashTreeMap<Long, Connection>();
		
		travese = new TraverseFunction<Connection>() 
		{
			@Override
			public void apply(Connection connection)
			{
				synchronized (connection) 
				{
					try 
					{
						if (connection.getSocket().getInputStream().available() > 0)
						{
							connection.newData();
						}
					} catch (Exception e) {e.printStackTrace();}
				}
			}
		};
		try {
			rsaKeyGenerator =  KeyPairGenerator.getInstance("RSA");
			rsaKeyGenerator.initialize(2048, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
		}
		//load banned ips
		
		
	}
	
	void addConnection(Connection connection) throws NetworkException
	{
		connections.put(connection.getID(), connection);
		initConnection(connection);
	}
	
	public void asServer(int port)
	{
		runAsServer = true;
		acceptConnections = new Thread(()->{
			try {
				ServerSocket server = new ServerSocket(port);
				while (runAsServer)
				{
					Socket newConnection = server.accept();
					InetAddress sockAdr = newConnection.getInetAddress();
					Connection connection = null;
					try {
						connection = new Connection(newConnection);
					} catch (NetworkException e)
					{
						e.printStackTrace();
					}
					if (bannedIPs.get(sockAdr) != null)
					{
						//send haha banned
						JSONObject lolBanned = new JSONObject();
						lolBanned.put("connectionRefused", true);
						connection.write(lolBanned);
						connection.close(true);
						continue;
					}
					
					initConnection(connection);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NetworkException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		acceptConnections.start();
	}
	
	private void initConnection(Connection connection) throws NetworkException
	{
		new Thread(()-> {
			try
			{
				synchronized (connection) 
				{
					JSONObject object = new JSONObject();
					object.put("connectionRefused", false);
					KeyPair rsa;
					synchronized (rsaKeyGenerator) {
						 rsa = rsaKeyGenerator.generateKeyPair();
					}
					connection.init(rsa.getPrivate());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(baos);
					oos.writeObject(rsa.getPublic());
					oos.flush();
					byte[] publicKey =  baos.toByteArray();
					JSONArray keyBytes = new JSONArray();
					
					for (int index = 0; index < publicKey.length; index ++)
					{
						keyBytes.put(publicKey[index]);
					}
					object.put("publicRSA", keyBytes);
					System.out.println(object.toString());
					connection.write(object);
					connection.flush();
					JSONObject response = connection.getNext();
					JSONArray aeskey = object.getJSONArray("publicRSA");
					byte[] aes = new byte[aeskey.length()];
					for (int index = 0; index < aes.length; index++) 
					{
						aes[index] = (byte) (aeskey.getInt(index));
					}
					ByteArrayInputStream bais = new ByteArrayInputStream(aes);
					Key key = (Key) (new ObjectInputStream(bais).readObject());
					connection.init(key);
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				try
				{
					connection.close(false);
				}
				catch (Exception e2)
				{
					e2.printStackTrace();
				}
			}
		}).start();
	}
	
	
	
	
	public void checkSockets()
	{
		new Thread(()->{
			connections.inOrderTraverse(travese);
		}).start();
		
	}
}
