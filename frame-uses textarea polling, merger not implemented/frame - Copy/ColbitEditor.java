import java.io.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.concurrent.ThreadLocalRandom;

// Actual editing of files and UI

public class ColbitEditor
{
	private JFrame window;

	protected JTextArea textArea;
	protected String file;
	protected JPanel panelBorder;

	ColbitEditor()
	{}

	ColbitEditor(String file)
	{
		window = new JFrame("Colbit Editor - " + file);
		window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		textArea = new JTextArea();
		this.file = file;

		Font font = new Font("Arial", Font.PLAIN, 15);

		// all important variables
		textArea.setBounds(5,5, 540,640);
		textArea.setFont(font);
		textArea.setForeground(new Color(100,100,100));
		window.add(textArea);

		panelBorder = new JPanel();
		panelBorder.setBounds(5-1,5-1, 540+2,640+2);
		panelBorder.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.lightGray));
		window.add(panelBorder);

		// window
		window.setSize(550,695);
		window.setLayout(null);
		window.setVisible(true);
		window.setResizable(true);
		
		window.validate();
		window.repaint();
	}

	public void addEditor( JFrame window, int idx, int numEditors )
	{}

	public String fetchNewData()
	{
		return textArea.getText();
	}

	public void mergeCollectedData(String data)
	{
		textArea.setText( merger( data, textArea.getText() ) );
	}

	public String merger(String server, String textArea)
	{
		String[] options = { "my", "name", "is", "manashi", "sarkar", "monti",
								"sister", "shreyashi", "srakar", "mini" };

		int idx = ThreadLocalRandom.current().nextInt(0,10);

		return options[idx];
	}

}

// 0. ensure smooth working of the editor, by not taking curson to start

// 1. implement merger, use locks on files
// 2. saving and loading files, save on exit
// 3. mingw integration
// 4. implemnet client UI
