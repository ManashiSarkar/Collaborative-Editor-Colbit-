import java.io.*;
import java.net.*;

public class FileClient // sends request to fileserver
{
	// we can pass servername along with skey, portFileServer from PeerClient to PeerServer
	public static void main(String args[])
	{
		String fileServerName = "localhost";
		String portFileServer = "7788";
		String skey = "ksgfjsgvfhrvjhfwjfggvsvfm";
		Socket c;
		String[] files;

		int tries = 10;

		while( true && tries-- != 0 )
		{
			try
			{
				Thread.sleep(1000);
				c = new Socket(fileServerName,7788);

				DataOutputStream out = new DataOutputStream(c.getOutputStream());
				out.writeUTF(skey);

				DataInputStream in = new DataInputStream(c.getInputStream());
				String grant = in.readUTF();
				System.out.println(grant);

				if( !grant.equals("granted") )
				{
					System.out.println("Oh No!");
					c.close();
					break;
				}

				System.out.println("Oh Yes!");

				// request accepted. get file names
				files = in.readUTF().split("\\n");

				for(int i=0;i<files.length;i++)
				{
					System.out.println(files[i]);
				}

				interact(in,out,files);

				break;	// after a successful connection, break	without socket closing
			}

			catch(NullPointerException e)
			{
				e.printStackTrace();
				break;
			}

			catch(Exception e) // Input Output Stream Exception
			{
				e.printStackTrace();
			}
		}

	}

	private static void interact(DataInputStream in, DataOutputStream out, String[] files)
	{
		ColbitClientFilesManager manager = new ColbitClientFilesManager(files);

		manager.sendData(out); // sends fresh data to server

		manager.periodicReceiver(in);
	}

}









