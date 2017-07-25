import java.io.*;
import java.net.*;

public class FileServer extends Thread
{
	private ServerSocket serverSocket;
	private String skey;
	private int portFileServer = 7788;
	private ConnectionFrame info;
	
	private ColbitServerPeersManager peersManager;
	private String[] files;
	private Boolean syncLockFiles = false;

	private FileServer(ConnectionFrame info)
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
		synchronized(syncLockFiles)
		{
			if( syncLockFiles == true )
				return;

			files = info.getFileList();
			syncLockFiles = true;

			// in frame window
			info.updateFilelist(String.join("\n",files));

			peersManager.sendFileNamesToAll(String.join("\n",files));
			peersManager.sendInitialFileDataToAll(null);
		}
	}

	private void spawnHandler()
	{
		// handles interaction
		peersManager = new ColbitServerPeersManager(this);
	}

	public void run()
	{
		System.out.println("FileServer run()");

		spawnHandler();
		
		while( true )
		{
			try
			{
				Socket s = serverSocket.accept();
				DataInputStream in = new DataInputStream(s.getInputStream());
				DataOutputStream out = new DataOutputStream(s.getOutputStream());

				String trykey = in.readUTF();

				if( trykey.equals(skey) )
				{
					out.writeUTF("granted");
					info.updateAClist("localhost");
					peersManager.addPeer(in,out);
				}
				else
				{
					out.writeUTF("not-granted");
					s.close();
					continue;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void startFileServer(ConnectionFrame info) 
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

	private String generateSecretKey()
	{
		String skey = "ksgfjsgvfhrvjhfwjfggvsvfm";
		return skey;
	}

	public Boolean getSyncLockFiles()
	{
		return syncLockFiles;
	}
}









