package FileSocketPackage;
import java.io.*;
import java.net.*;

public class FileClient // sends request to fileserver
{
	public static void request(String skey, String portFileServer) 
	{
		String fileserverName = "localhost";
		try
		{
			Socket c = new Socket(fileserverName,Integer.parseInt(portFileServer));

			DataOutputStream out = new DataOutputStream(c.getOutputStream());

			out.writeUTF(skey);

			DataInputStream in = new DataInputStream(c.getInputStream());
			System.out.println(in.readUTF());

			c.close();
			
		}
		catch(Exception e) // Input Output Stream Exception
		{
			e.printStackTrace();
		}
	}

}