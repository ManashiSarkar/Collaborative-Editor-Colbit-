import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;

// Inviter's responsibilities

// making singleton might make it impossible to extend
public class ColbitHandler
{
	private FileServer server;
	private ColbitFilesManager manager;
	private static Boolean isHandlerStarted = false;

	private ColbitHandler(String[] files, FileServer server)
	{
		manager = new ColbitFilesManager(files);
		this.server = server;
		this.server.setHandler(this);
	}

	public void serverInteraction(DataInputStream in, DataOutputStream out)
	{
		// a thread per client (already opened by caller) to receive changes from clients
		// lock files on server to update them on server

		while( true )
		{

		}
	}

	public static void startHandler(String[] files, FileServer server)
	{		
		if( !isHandlerStarted )
		{
			isHandlerStarted = true;
			ColbitHandler handler = new ColbitHandler(files,server); 
			
			// start a thread to periodically send changes to all clients
			handler.sendMergedFiles
		}
		
	}
}











