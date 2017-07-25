package PeerSocketPackage;
import FileSocketPackage.FileClient;
import java.net.*;
import java.io.*;

// only one invite can be accepted
public class PeerServer extends Thread
{
	private ServerSocket serverSocket;

	public PeerServer(int port) throws IOException
	{
		serverSocket = new ServerSocket(port);
	}

	public void run()
	{
		try
		{
			Socket s = serverSocket.accept();

			DataInputStream in = new DataInputStream(s.getInputStream());
			String skey_portFileServer[] = in.readUTF().split("\\n");
			System.out.println(skey_portFileServer[0] + " " + skey_portFileServer[1]);

			// spawns FileClient and sends requent to fileserver for files
			FileClient.request(skey_portFileServer[0],skey_portFileServer[1]);

			s.close();
			serverSocket.close(); // for simplicity
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//break;
		}
	}

	public static void main(String[] args) 
	{
		int portPeerServer = 7788;

		try
		{
			Thread t = new PeerServer(portPeerServer);
			t.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}