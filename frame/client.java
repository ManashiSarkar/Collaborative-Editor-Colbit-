import java.net.*;
import java.io.*;

public class client // sends invites for colbit
{
	public static void main(String[] args) 
	{
		// invitations to collaborate
		int MAX = 10;
		Integer ports[] = new Integer[MAX];
		String names[] = new String[MAX];
		Boolean connected[] = new Boolean[MAX];
		Socket friends = new Socket[MAX];

		try
		{
			InputStreamReader in = new InputStreamReader(System.in);
			BufferedReader cin = new BufferedReader(in);

			int requests = MAX, idx = 0;

			while( requests-- ) // opens threads for each friend
			{
				String name = cin.readLine();
				int port = parseInt(cin.readLine());
				Socket c = new Socket(name,port);

				DataOutputStream out = new DataOutputStream(c.getOutputStream());

				out.writeUTF("lol");

				DataInputStream in = new DataInputStream(c.getInputStream());
				System.out.println(in.readUTF());

				c.close();
			}
			
		}
		catch(Exception e) // Input Output Stream Exception
		{
			e.printStackTrace();
		}
	}

}