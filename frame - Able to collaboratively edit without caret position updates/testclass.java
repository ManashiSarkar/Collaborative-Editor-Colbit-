import java.io.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.concurrent.ThreadLocalRandom;

public class testclass
{
	public static void main(String[] args) throws Exception
	{
		JFrame frame = new JFrame("testing speed of swing");
		JTextArea textArea = new JTextArea();

		textArea.setBounds(20,20,500,500);
		textArea.setVisible(true);
		frame.add(textArea);

		frame.setSize(600,600);
		frame.setLayout(null);
		frame.setVisible(true);

		frame.revalidate();
		frame.repaint();

		textArea.setEditable(true);

		Thread.sleep(3000);

		textArea.setEditable(false);
		String text = textArea.getText();

		for(int i=0;i<1000;i++)
		{

			text += "manashi and her mummy are very very happy :)\n";
			
		}

		textArea.setText(text);

		textArea.setEditable(true);

	}
}