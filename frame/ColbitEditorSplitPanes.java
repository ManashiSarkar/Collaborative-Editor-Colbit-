import java.io.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

// derived classes' role is UI related only
public class ColbitEditorSplitPanes extends ColbitEditor
{
	public JPanel panelBorder;

	ColbitEditorSplitPanes(String file,DataInputStream in,DataOutputStream out)
	{
		super(file,in,out);
		panelBorder = new JPanel();
	}

	public void addEditor( JFrame window, int idx, int numEditors )
	{
		int colOffset = idx*545;
		Font font = new Font("Arial", Font.BOLD, 15);

		textArea.setBounds( colOffset + 5, 5, 540, 640);
		textArea.setFont(font);
		textArea.setForeground(new Color(100,100,100));
		textArea.setTabSize(4);
		textArea.setLineWrap(true);
		textArea.setVisible(true);
		
		panelBorder.setBounds( colOffset + 5 - 1, 5 - 1, 540+2, 640+2 );
		panelBorder.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.lightGray));

		window.add(textArea);
		window.add(panelBorder);
	}
}






