import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class synctestclass 
{
	public static void main(String args[]) 
	{
		JFrame frame = new JFrame("Title");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JTextArea textArea = new JTextArea();

		Integer lock = 0;

		KeyListener action = new KeyListener() 
		{
			public void keyTyped(KeyEvent e) 
			{

				System.out.println("Clicked");
				SwingWorker worker = new SwingWorker() 
				{
					protected String doInBackground() throws InterruptedException 
					{
						//Thread.sleep(3000);
						synchronized(lock)
						{
							System.out.println("well");
							return null;
						}
					}
					protected void done() 
					{
						String label = textArea.getText();
						textArea.setText(label + "0");
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
		frame.add(textArea, BorderLayout.CENTER);
		frame.setSize(200, 200);
		frame.setVisible(true);

		ActionListener listener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("mimi");
			}
		};

		listener.actionPerformed(new ActionEvent(textArea,1,"null"));

		while(true)
		{
			try
		    {
				Thread.sleep(2000);
				new Thread(() -> {
			    	
						//while( true )
						{
							synchronized(lock)
							{
								
								System.out.println("periodicUpdater");
							}

							// take what's in text and send to editors
							
						}
				}).start();
			}

			catch(Exception f)
			{
				f.printStackTrace();
			}

		}
	}
}