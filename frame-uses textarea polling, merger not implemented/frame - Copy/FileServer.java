import java.io.*;
import java.net.*;

// singleton on inviter's system, coming from peerclient
// singleton is not working, as attempt to start thread is happening again and again
public class FileServer extends Thread
{
	private ServerSocket serverSocket;
	private String skey;
	private int portFileServer = 7788;

	private connectionFrame info;
	private Boolean filesready = false;
	private String[] files;
	private Boolean isFilesStored = false;

	private ColbitServerFilesManager manager;

	private String generateSecretKey()
	{
		String skey = "ksgfjsgvfhrvjhfwjfggvsvfm";
		return skey;
	}

	private FileServer(connectionFrame info)
	{
		try
		{
			serverSocket = new ServerSocket(portFileServer);
		}
		catch(Exception f)
		{
			System.exit(0);
		}

		skey = generateSecretKey();
		this.info = info;
		this.info.storeServerObject(this);
	}

	public void filesReady()
	{
		filesready = true;
	}

	private void spawnHandler()
	{
		// 3. periodically sets the variable updateOnClients to true

		new Thread(() -> {
	    	try
	    	{
	    		// 1. waits on filesready
				while( !filesready )
				{
					Thread.sleep(1000);
				}

				if( !isFilesStored )
				{
					files = info.getFileList();
					isFilesStored = true;
				}

				info.updateFilelist(String.join("\n",files));

				// 2. opens filemanager
				manager = new ColbitServerFilesManager(files);

				manager.periodicServerUpdater();
				manager.periodicServerFetcher();

				System.out.println("FileServer.spawnHandler()");

			}
			catch(Exception f)
			{
				f.printStackTrace();
			}
		}).start();

	}

	private void interact(DataInputStream in, DataOutputStream out)
	{
		new Thread(() -> {
	    	try
	    	{
				while( !filesready )
				{
					Thread.sleep(1000);
				}

				if( !isFilesStored )
				{
					files = info.getFileList();
					isFilesStored = true;
				}

				// send files to client
				out.writeUTF(String.join("\n",files));

				while( manager == null )
				{
					Thread.sleep(1000);
				}

				manager.periodicUpdater(out); // thread
				manager.receiveData(in); // while loop, not a new thread

				System.out.println("FileServer.interact()");
			}
			catch(Exception f)
			{
				f.printStackTrace();
			}
		}).start();
	}

	public void run()
	{
		System.out.println("manashi");

		// spawn it's own thread for colbit
		spawnHandler();
		
		// connect to peers
		while( true ) // in case of multiple peers
		{
			try
			{
				Socket s = serverSocket.accept();

				System.out.println("socket accepts");

				DataInputStream in = new DataInputStream(s.getInputStream());
				String trykey = in.readUTF();
				System.out.println(trykey);

				DataOutputStream out = new DataOutputStream(s.getOutputStream());

				if( !trykey.equals(skey) )
				{
					out.writeUTF("not-granted");
					s.close();
					continue;
				}
				
				System.out.println("manashi");
				info.updateAClist("localhost");
				out.writeUTF("granted");

				// connected to client

				// wait for user to enter files
				interact(in,out);
			}

			catch(Exception e)
			{
				e.printStackTrace();
				//break;
			}
		}
	}

	public static void startFileServer(connectionFrame info) 
	{
		try
		{
			Thread t = new FileServer(info);
			t.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}









