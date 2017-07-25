import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Display
{
	protected JFrame[] window;
	protected Font font;

	Display(ColbitEditor[] parentEditors, String[] files, DataInputStream in, DataOutputStream out)
	{
		window = new JFrame[(parentEditors.length+1)/2];

		ArrayList<ColbitEditorSplitPanes> editors = new ArrayList<ColbitEditorSplitPanes>();

		for(int i=0; i<files.length; i++)
		{
			editors.add( new ColbitEditorSplitPanes(files[i],in,out) );
		}

		for(int i=0; i < editors.size(); i+=2)
		{
			String filesInFrame = "";
			filesInFrame += editors.get(i).file;

			if( i+1 < editors.size() )
				filesInFrame += " || " + editors.get(i+1).file;

			window[i/2] = new JFrame(filesInFrame);
			window[i/2].setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

			// window
			window[i/2].setSize( (i+1 < editors.size() ? 2 : 1)*545+20, 690 );
			window[i/2].setLayout(null);
			window[i/2].setVisible(true);
			window[i/2].setResizable(true);

			editors.get(i).addEditor( window[i/2], 0, editors.size() );
			parentEditors[i] = editors.get(i);

			if( i+1 < editors.size() )
			{
				editors.get(i+1).addEditor( window[i/2], 1, editors.size() );
				parentEditors[i+1] = editors.get(i+1);
			}

			window[i/2].revalidate();
			window[i/2].repaint();

		}
	}
}