import java.io.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

class UpdateInfoReceive
{
	public String sender;
	public int versionNumCur;
	public int updateposition;
	public int caretposition;
	public String value;

	UpdateInfoReceive()
	{

	}
}

// interaction with server versions
public class ColbitEditor
{
	protected JTextArea textArea;
	public String file;

	protected DataInputStream in;
	protected DataOutputStream out;

	// we want speed here, so minimum synchronization desired
	protected Integer versionNumCur, versionNumOld;
	protected String versionDataCur, versionDataOld;
	protected int caretposition;
	protected UpdateInfoReceive updateInfoReceive;
	protected int changesQueue;

	protected String sendData;

	protected ActionListener listener;
	protected String id;
	protected String serverDelimiter = "cgfyjefladrjhl";
	protected String clientDelimiter = "yfdqejdkebfhvz";

	ColbitEditor(String file,DataInputStream in,DataOutputStream out)
	{
		textArea = new JTextArea();
		this.file = file;
		this.in = in;
		this.out = out;
	}

	public void initialize(String fileData)
	{
		textArea.setText(fileData);
		versionNumCur = versionNumOld = 0;
		versionDataCur = versionDataOld = fileData;
		caretposition = 0;
		updateInfoReceive = new UpdateInfoReceive();
		changesQueue = 0;

		// unique id for this client-editor
		id = UUID.randomUUID().toString();

		listener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// if empty(), setText = versionData
				textArea.setText(versionDataCur);
				textArea.setCaretPosition(caretposition);
				
			}
		};
	}

	public void startThreadForEachEditor()
	{
		// just have a documentlistener
		output();
	}

	public void input(String data)
	{
		// this should be in each input() call

		String[] incoming = data.split(clientDelimiter);

		updateInfoReceive.sender = incoming[0];
		updateInfoReceive.versionNumCur = Integer.parseInt(incoming[1]);
		updateInfoReceive.updateposition = Integer.parseInt(incoming[2]);
		updateInfoReceive.caretposition = Integer.parseInt(incoming[3]);
		updateInfoReceive.value = incoming[4];
		

		// if sender is self, pop from changeQueue
		if( updateInfoReceive.sender.equals(id) )
		{
			changesQueue--;
		}

		// merge data with versionData
		versionDataCur = versionDataCur.substring(0,updateInfoReceive.updateposition) + 
						updateInfoReceive.value + 
						versionDataCur.substring(updateInfoReceive.updateposition, versionDataCur.length());

		if( updateInfoReceive.updateposition <= textArea.getCaretPosition() )
		{
			//caretposition++;
		}

		caretposition = updateInfoReceive.caretposition + 1;

		versionNumCur = updateInfoReceive.versionNumCur;

		if( changesQueue == 0 )
		{
			synchronized(versionDataOld)
			{
				synchronized(versionNumOld)
				{
					listener.actionPerformed(new ActionEvent(textArea,1,"null"));
					versionDataOld = versionDataCur;
					versionNumOld = versionNumCur;
				}
			}
			
		}
	}

	// shares {out} with other editors, hence needs sync
	private void output()
	{
		// output will maintain a request queue for new updates

		// pushes requests in eventQueue

		// on an input, lock the editor, and push the merger on eventQueue, to incorporate all new updates in the response 
		// text, make updates, finally release the editor. twenty IO operations are quite fast, so hopefully 5-10 merging 
		// updates will be fast too.

		// use stringbuiler to insert substring

		try
		{
			KeyListener action = new KeyListener() 
			{
				public void keyTyped(KeyEvent e) 
				{

					System.out.println("Clicked");
					SwingWorker worker = new SwingWorker() 
					{
						protected String doInBackground() throws InterruptedException 
						{
							// stuff to be run on a separate thread
							return null;
						}
						protected void done() 
						{
							// stuff to be done in event dispatching thread
							try
							{
								synchronized(versionDataOld)
								{
									synchronized(versionNumOld)
									{
										// stuff we need to do
										int updateposition = textArea.getCaretPosition() - 1;
										int caretposition = updateposition;
										String value = Character.toString(e.getKeyChar());

										// info we need to send:
										// filename
										// id
										// version number
										// update position
										// caret position
										// string inserted

										// out.writeUTF( versionnumber + caretposition + stringinserted )

										sendData = 	file + serverDelimiter + 
													id + serverDelimiter + 
													Integer.toString(versionNumOld) + serverDelimiter + 
													Integer.toString(updateposition) + serverDelimiter + 
													Integer.toString(caretposition) + serverDelimiter +
													value;

										changesQueue++;

										out.writeUTF(sendData);

										//text = textArea.getText();
										//System.out.println("well");
										//return null;
									}
								}
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					};
					worker.execute();
				}

				public void keyPressed(KeyEvent e)
				{

				}

				public void keyReleased(KeyEvent e)
				{

				}
			};


			textArea.addKeyListener(action);
		
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}



// caret position has to work correctly when external updates are received. 
// eg: 
// 1. client1 sends updates to client2, client2's caret position must work correctly
// 2. user points the cursor somewhere else without updating contents of editor

// sol 1 (bad)-> use a separate channel for caret position updates, which requests current caret position
// from server. This caret position request will have a separate response queue in FileClient.
// on server, the quesries will be made only up to versionNumCur.
// on editor, a caret position server request will be made only when setting text.
// we might need to use a latch for this, to use callback on setting the caretUpdateReceived

// sol 2 -> 
