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

	int width = 550, height = 660;

	Display(ColbitEditor[] editors)
	{
		window = new JFrame[(editors.length+1)/2];

		for(int i=0; i < editors.length; i+=2)
		{
			String filesInFrame = "";
			filesInFrame += editors[i].file;

			if( i+1 < editors.length )
				filesInFrame += " || " + editors[ i+1 ].file;

			window[i/2] = new JFrame(filesInFrame);
			window[i/2].setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

			// window
			window[i/2].setSize( (i+1 < editors.length ? 2 : 1)*545+20, 690 );
			window[i/2].setLayout(null);
			window[i/2].setVisible(true);
			window[i/2].setResizable(true);

			editors[i].addEditor( window[i/2], 0, editors.length );

			if( i+1 < editors.length )
				editors[i+1].addEditor( window[i/2], 1, editors.length );

			window[i/2].revalidate();
			window[i/2].repaint();
		}
	}
}