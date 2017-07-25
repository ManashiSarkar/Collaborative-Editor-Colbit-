import java.io.*;
import java.util.*;
import java.net.*;

public class ColbitClientFilesManager implements ClientInteraction
{
	protected ColbitEditor[] editors;
	protected Display display;

	ColbitClientFilesManager(String[] files)
	{
		System.out.println("CFM constructor");

		editors = new ColbitEditor[files.length];

		display = new Display( editors, files );
	}
	
	public void sendData(DataOutputStream out)
	{
		new Thread(() -> {
	    	try
	    	{
				while( true )
				{
					Thread.sleep(3000);

					System.out.println("sendData");

					// take what's in text and send to editors
					String response = "";

					for(int i=0; i<editors.length; i++)
					{
						response += editors[i].fetchNewData() + "fileSplitter"; // takes from editor
					}

					out.writeUTF(response);
				}
			}

			catch(Exception f)
			{
				f.printStackTrace();
			}

		}).start();
	}

	public void periodicReceiver(DataInputStream in)
	{
		try
		{
			while( true )
			{
				String[] response = in.readUTF().split("fileSplitter");

				System.out.println("periodicReceiver");

				for(int i=0; i<editors.length; i++)
				{
					// applies merger(), then assigns to editor
					editors[i].mergeCollectedData(response[i]);
				}
			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}

interface ClientInteraction
{
	public void sendData(DataOutputStream out);
	public void periodicReceiver(DataInputStream in);
}









