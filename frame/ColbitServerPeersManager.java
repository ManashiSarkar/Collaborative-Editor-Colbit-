import java.io.*;
import java.net.*;
import java.util.*;

// handles all interaction

class UpdateInfo
{
	int updatePosition;
	int caretPosition;
	String str;

	UpdateInfo(int a, int b, String c)
	{
		updatePosition = a;
		caretPosition = b;
		str = c;
	}

	UpdateInfo()
	{

	}
}

public class ColbitServerPeersManager
{
	private String serverDelimiter = "cgfyjefladrjhl";
	private String clientDelimiter = "yfdqejdkebfhvz";

	private Vector<DataInputStream> in;
	private Vector<DataOutputStream> out;
	private FileServer fileserver;
	private String[] files;
	private ArrayList< Vector<UpdateInfo> > versions;

	ColbitServerPeersManager(FileServer fileserver)
	{
		in = new Vector<DataInputStream>(); // Vector class is synchronized, ArrayList is not
		out = new Vector<DataOutputStream>();
		this.fileserver = fileserver;
	}

	public void sendFileNamesToAll(String fileNames)
	{
		files = fileNames.split("\\n");

		for(int i = 0; i < out.size() ; i++) 
		{
			try
			{
				startThreadForEachClient(in.elementAt(i),out.elementAt(i));
				out.elementAt(i).writeUTF(fileNames);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	public void sendInitialFileDataToAll(DataOutputStream out)
	{
		// opens file if present, and sends data to clients
		//versions = (Vector<UpdateInfo>[]) new Object[files.length];

		//@SuppressWarnings("unchecked")
		versions = new ArrayList< Vector<UpdateInfo> >();

		for(int i=0;i<files.length;i++)
		{
			//System.out.println("lololo");
			versions.add( new Vector<UpdateInfo>() );
			versions.get(i).addElement( new UpdateInfo(0,0,"") );
		}

		String text = "";
		int idx = 0;

		for(String file : files)
		{
			try
			{
				file = "C:\\Users\\Lenovo\\java\\frame\\" + file;
				File f = new File(file);
				f.createNewFile();

				BufferedReader data = new BufferedReader(new FileReader(f));
			    String line;

			    text = "";

			    while ( (line = data.readLine() ) != null )
			    	text += line + "\n";

			    //System.out.println("text="+text);

				if( out == null )
					sendDataToAll(text);
				else
					out.writeUTF(text);

			    data.close();

			    //versions[idx] = new Vector<UpdateInfo>(200,200);
			    //versions[idx].addElement( new UpdateInfo(text.size(),0,text.size(),text) );
			    idx++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		// 
	}

	private void sendDataToAll(String text)
	{
		for(int i = 0; i < out.size() ; i++) 
		{
			try
			{
				out.elementAt(i).writeUTF(text);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	int getFileNumFromName(String file)
	{
		for(int i=0;i<files.length;i++)
			if( files[i].equals(file) )
				return i;

		return 0;
	}

	public void propagate(int fileNum, String id,Vector<UpdateInfo> versions,int versionNum,int updatePosition,int caretPosition,String str)
	{
		//synchronized(versions)
		{
			// update acc to versions
			// push a new version
			// send updates and sender to all
			for(int i=versionNum+1;i<versions.capacity();i++)
			{
				if( i<versions.size() && versions.elementAt(i).updatePosition <= updatePosition )
					updatePosition++;

				if( i<versions.size() && versions.elementAt(i).caretPosition <= caretPosition )
					caretPosition++;
			}

			versions.addElement( new UpdateInfo(updatePosition,caretPosition,str) );

			String text =   Integer.toString(fileNum) + serverDelimiter +
							id + clientDelimiter +
							Integer.toString(versions.capacity()) + clientDelimiter + 
							Integer.toString(updatePosition) + clientDelimiter +
							Integer.toString(caretPosition) + clientDelimiter +
							str;

			sendDataToAll(text);
			//sendDataToAll("0PARROT" + text);
		}

	}

	// each thread communicates with one {in} value, and sends to all {out} values
	public void startThreadForEachClient(DataInputStream in, DataOutputStream out)
	{
		new Thread(() -> {
	    	try
	    	{
	    		String id;
	    		int fileNum;
	    		int versionNum;
	    		int updatePosition;
	    		int caretPosition;
	    		String str;

				while( true )
				{
					String text = in.readUTF();

					String[] changeParams = text.split(serverDelimiter);

					// split text
					String fileName = changeParams[0];
					fileNum = getFileNumFromName(fileName);
					id = changeParams[1];
					versionNum = Integer.parseInt(changeParams[2]);
					updatePosition = Integer.parseInt(changeParams[3]);
					caretPosition = Integer.parseInt(changeParams[4]);
					str = changeParams[5];

					propagate( fileNum, id, versions.get(fileNum), versionNum, updatePosition, caretPosition, str );
				}
			}

			catch(Exception f)
			{
				f.printStackTrace();
			}

		}).start();

	}

	public synchronized void addPeer(DataInputStream in_, DataOutputStream out_)
	{
		synchronized(fileserver.getSyncLockFiles())
		{
			in.addElement(in_);
			out.addElement(out_);

			if( fileserver.getSyncLockFiles() == false )
				return;

			try
			{
				startThreadForEachClient(in_,out_);
				out_.writeUTF(String.join("\n",files));
				sendInitialFileDataToAll(out_);
			}
			catch(Exception f)
			{
				f.printStackTrace();
			}

		}
	}
}