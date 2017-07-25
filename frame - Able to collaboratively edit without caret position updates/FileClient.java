import java.io.*;
import java.net.*;

public class FileClient
{
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
				DataInputStream in = new DataInputStream(c.getInputStream());
				DataOutputStream out = new DataOutputStream(c.getOutputStream());

				out.writeUTF(skey);
				String grant = in.readUTF();
				System.out.println(grant);

				if( !grant.equals("granted") )
				{
					c.close();
					break;
				}

				// request accepted. get file names
				files = in.readUTF().split("\\n");

				for(int i=0;i<files.length;i++)
				{
					System.out.println(files[i]);
				}

				String[] fileData = new String[files.length];

				for(int i=0;i<files.length;i++)
				{
					fileData[i] = in.readUTF();//.split("InitFilesDelimiter");	
				}

				//System.out.println(fileData);
				/*
				for(int i=0;i<files.length;i++)
				{
					System.out.println(fileData[i]);
				}
				*/
				// get file contents
				interact(in,out,files, fileData );

				break;	// after a successful connection, break	without socket closing
			}
			catch(Exception e) // Input Output Stream Exception
			{
				e.printStackTrace();
			}
		}

	}

	private static void interact(DataInputStream in, DataOutputStream out, String[] files, String[] fileData)
	{
		// manages IO
		ColbitEditor[] editors;
		Display display;
		
		editors = new ColbitEditor[files.length];

		// we can use decorators here for UI
		display = new Display( editors, files, in, out );

		for(int i=0; i<files.length; i++)
		{
			editors[i].initialize(fileData[i]);
			editors[i].startThreadForEachEditor();
		}

		// incomingDataRouter
		String update[];
		Boolean inputLock = true;
		String serverDelimiter = "cgfyjefladrjhl";
		String clientDelimiter = "yfdqejdkebfhvz";

		int tries = 100;

		while( true && tries-- > 0 )
		{
			try
			{
				update = in.readUTF().split(serverDelimiter);
				editors[Integer.parseInt(update[0])].input(update[1]);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}









