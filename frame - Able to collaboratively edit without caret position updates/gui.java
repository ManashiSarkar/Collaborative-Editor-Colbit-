import java.awt.*;
import javax.swing.*;

public class gui extends JFrame
{
	private JLabel label, label2;
	private JTextField address, port;
	private JButton connect;

	public gui()
	{
		super("Colbit connect with a friend");
		setLayout(new FlowLayout());

		label = new JLabel("Address");
		add(label);

		address = new JTextField(16);
		add(address);

		label2 = new JLabel("Port");
		add(label2);

		port = new JTextField(4);
		add(port);

		connect = new JButton("Allow");
		add(connect);
	}

	public static void main(String args[])
	{
		gui window = new gui();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setSize(400,200);
	}
}

private class guiEventListener implements connectInterface
{
	gui window;

	
}













