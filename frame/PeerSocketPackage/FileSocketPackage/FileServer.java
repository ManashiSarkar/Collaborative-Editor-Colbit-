package FileSocketPackage;
import java.io.*;
import java.net.*;

public class FileServer extends Thread
{
	private ServerSocket serverSocket;
	private String skey;
	private int portFileServer;

	public FileServer(int port,String skey) throws IOException
	{
		serverSocket = new ServerSocket(port);
		this.skey = skey;
		this.portFileServer = port;
	}

	public void run()
	{
		while( true ) // in case of multiple peers
		{
			try
			{
				Socket s = serverSocket.accept();

				DataInputStream in = new DataInputStream(s.getInputStream());
				String trykey = in.readUTF();
				System.out.println(trykey);

				DataOutputStream out = new DataOutputStream(s.getOutputStream());
				out.writeUTF("pop");

				if( !trykey.equals(skey) )
					s.close();
				else
					System.out.println("manashi");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				//break;
			}
		}
	}

	public static void startFileServer(int portFileServer,String secretKey) 
	{
		System.out.println(secretKey);
		try
		{
			Thread t = new FileServer(portFileServer,secretKey);
			t.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
