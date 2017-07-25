import java.io.*;
import java.util.*;
import java.net.*;

// handles UI
public class ColbitServerFilesManager implements ServerInteraction
{
	protected ColbitEditor[] editors;
	protected String[] text;	
	protected Display display;

	ColbitServerFilesManager(String[] files)
	{
		editors = new ColbitEditor[files.length];
		text = new String[files.length];

		System.out.println("CServerFM constructor");

		for(int i=0; i<files.length; i++)
		{
			editors[i] = new ColbitEditorSplitPanes(files[i]);
			text[i] = new String("");
		}

		display = new Display( editors );

		System.out.println("CServerFM constructor -> 2");

		for(int i=0; i<files.length; i++)
		{
			System.out.println(files[i]+" "+text[i]);
		}
	}

	// sends text to client
	public void periodicUpdater(DataOutputStream out)
	{
		new Thread(() -> {
	    	try
	    	{
				while( true )
				{
					Thread.sleep(3000);
					System.out.println("periodicUpdater");

					// take what's in text and send to editors
					String response = "";

					for(int i=0; i<text.length; i++)
					{
						response += text[i] + "fileSplitter";
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

	// receives from client, saves in text
	public void receiveData(DataInputStream in)
	{
		try
		{
			while( true )
			{
				String[] response = in.readUTF().split("fileSplitter");

				System.out.println("receiveData");

				for(String r : response)
					System.out.println(r);

				for(int i=0; i<text.length; i++)
				{
					text[i] = editors[i].merger(response[i],text[i]);
				}
			}
		}

		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}

interface ServerInteraction
{
	public void periodicUpdater(DataOutputStream out);
	public void receiveData(DataInputStream in);
	public void periodicServerUpdater();
	public void periodicServerFetcher();
}










