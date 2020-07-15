package engine.core.networking;

interface SocketListener 
{
	void newData() throws NetworkException;
}
