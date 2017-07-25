import java.io.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;


// has all the important UI <-> logic
class ConnectionFrame implements connectionInterface
{
	// has the important variables for any ConnectionFrame
	protected JFrame window;
	protected JButton invitePeers;
	protected JTextArea peerList;
	protected JButton openFiles;
	protected JTextArea fileList;

	protected String acceptedInvitees = "manashi\nshreyashi";
	protected String successfullyOpened = "a.cpp\nb.cpp";

	protected FileServer fileserver;
	protected Boolean filesBeforeInvite = false;
	// String joined2 = String.join(",", array);
	ConnectionFrame()
	{
		// initializes important variables for any ConnectionFrame
		window = new JFrame("Colbit - Connect with peers");
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		invitePeers = new JButton("Invite");
		peerList = new JTextArea();
		openFiles = new JButton("Open");
		fileList = new JTextArea();

		ConnectionFrame info = this;

		invitePeers.addActionListener
		(
			new ActionListener()
			{  
	    		public void actionPerformed(ActionEvent e)
	    		{
	    			if( fileserver == null && isPeerList() )
					{
						peerList.setEditable(false);
						peerList.setFocusable(false);
						peerList.setBackground(Color.black);
						window.validate();

						FileServer.startFileServer(info); // server starts after button press

				    	try
				    	{
				    		if( filesBeforeInvite )
							{
								while( fileserver == null )
								{
									Thread.sleep(200);
								}

								fileserver.filesReady();
							}
						}
						catch(Exception f)
						{
							f.printStackTrace();
						}
					}
			    }  
			}
		);

		openFiles.addActionListener
		(
			new ActionListener()
			{  
	    		public void actionPerformed(ActionEvent e)
	    		{
	    			if( isFileList() )
	    			{
	    				fileList.setEditable(false);
						fileList.setFocusable(false);
						fileList.setBackground(Color.black);
						window.validate();
						
	    				if( fileserver != null )
							fileserver.filesReady(); 
							// files were opened after connect

						else if( fileserver == null )
							filesBeforeInvite = true;
	    			}
			    }  
			}
		);
	}

	public void storeServerObject(FileServer s)
	{
		this.fileserver = s;
	}

	public Boolean isPeerList()
	{
		String[] peers = peerList.getText().split("[\\s\\n,]");

		return ( peers.length > 0 && peers[0] != "" );
	}

	public Boolean isFileList()
	{
		String[] files = fileList.getText().split("[\\s\\n,]");

		return ( files.length > 0 && files[0] != "" );
	}

	public String[] getFileList()
	{
		// different users can get different file lists
		String[] files = fileList.getText().split("[\\s\\n,]");

		return files;
	}

	public void updateAClist(String v)
	{
		acceptedInvitees += "\n" + v;
		System.out.println(acceptedInvitees);
		postProcessing(); // has paint-over issues. Best to not care, else, have new panels
	}

	public void updateFilelist(String v)
	{
		successfullyOpened += "\n" + v;
		System.out.println(successfullyOpened);
		postProcessing(); // has paint-over issues. Best to not care, else, have new panels
	}

	public void postProcessing()
	{
		
	}
}