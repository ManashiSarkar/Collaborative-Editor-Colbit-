import java.io.*;
import java.net.*;

// connection PeerClient
public class PeerClient
{
	private PeerClient()
	{
		// no objects will be created of this class
	}

	static int getRandomPort()
	{
		int portFileServer = 5555;
		return portFileServer;
	}

	static String generateSecretKey()
	{
		String skey = "ksgfjsgvfhrvjhfwjfggvsvfm";
		return skey;
	}

	/*
		FUNCTIONALITY:
		1. called only once, and all connections must be made now, ie: not asynchronous
		2. sends invites, and closes client port immediately after that
		3. so far, doesn't skip exceptions to the next peer
	*/

	// only one skey is generated per colbit fileserver
	public static void connectWithPeer(String[] names, String acceptedInvitees) 
	{
		// invitations to collaborate
		int portFileServer = getRandomPort();
		String skey = generateSecretKey();
		FileServer.startFileServer(portFileServer,skey,acceptedInvitees);

		int MAX = names.length;
		int portPeerServer = 7788; // specially reserved for this app, this purpose

		try
		{
			for(int i=0;i<MAX;i++) // sends invites to each friend
			{
				String name = names[i];
				Socket peer = new Socket(name,portPeerServer);
				DataOutputStream out = new DataOutputStream(peer.getOutputStream());

				out.writeUTF(skey+"\n"+portFileServer);
				peer.close(); 
				// we don't need socket after succesfully sending invite
			}
			
		}
		catch(Exception e) // if peerserver is not open
		{
			e.printStackTrace();
		}
	}

}