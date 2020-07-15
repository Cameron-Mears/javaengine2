package game;

import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import engine.core.exceptions.EngineException;
import engine.core.networking.Connection;
import engine.core.networking.NetworkException;
import external.org.json.JSONException;
import graphics.instance.InvalidInstanceException;

public class Game
{
	
	public static void main(String[] args) throws JSONException, IOException, EngineException, InvalidInstanceException, IllegalArgumentException, IllegalAccessException, NoSuchAlgorithmException, NetworkException, InvalidKeyException, NoSuchPaddingException
	{  
		Socket sock = new Socket("google.ca", 80);
		new Connection(sock);
		//Engine.getInstance().start();
		
	}
	
}

