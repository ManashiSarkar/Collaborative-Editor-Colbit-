import java.io.*;
import java.util.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;

// only job of this inherited classes is to prettify
public class ConnectionFrameA extends ConnectionFrame
{
	JLabel peerLabel, fileLabel;
	JPanel peerBorder, fileBorder;
	JTextArea acceptedPanel, openedPanel;
	Font font;

	// styles the base class variables, and any extra variable
	ConnectionFrameA()
	{
		super();
		font = new Font("Arial", Font.BOLD, 15);

		// all important variables
		// friends
		invitePeers.setBounds(170,30,80, 23);
		invitePeers.setBackground(new Color(0,200,200));
		window.add(invitePeers);

		peerList.setBounds(30,60, 220,120);
		peerList.setFont(font);
		peerList.setForeground(new Color(120,120,120));
		window.add(peerList);

		peerBorder = new JPanel();
		peerBorder.setBounds(30-2,60-1, 220+3,120+2);
		peerBorder.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.lightGray));
		window.add(peerBorder);

		// files
		openFiles.setBounds(170,210,80, 23);
		openFiles.setBackground(new Color(0,200,200));
		window.add(openFiles);

		fileList.setBounds(30,240, 220,120);
		fileList.setFont(font);
		fileList.setForeground(new Color(120,120,120));
		window.add(fileList);

		fileBorder = new JPanel();
		fileBorder.setBounds(30-2,240-1, 220+3,120+2);
		fileBorder.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.lightGray));
		window.add(fileBorder);

		// window
		window.setSize(540,440);
		window.setLayout(null);
		window.setVisible(true);
		window.setResizable(false);

		// all extra variables
		// friends label
		peerLabel = new JLabel();
		peerLabel.setText("Friends' names");
		peerLabel.setForeground(new Color(0,128,128));
		peerLabel.setBounds(32,30,120,25);
		peerLabel.setFont(font);
		window.add(peerLabel);

		// files label
		fileLabel = new JLabel();
		fileLabel.setText("Files");
		fileLabel.setForeground(new Color(0,128,128));
		fileLabel.setBounds(32,210,120,25);
		fileLabel.setFont(font);
		window.add(fileLabel);
		
		window.validate();
		window.repaint();
	}

	// must implement any base class interface methods, and extra methods
	public void postProcessing()
	{
		// peer names
		acceptedPanel = new JTextArea();

		acceptedPanel.setForeground(new Color(0,158,0));
		acceptedPanel.setBackground(Color.white);//(220,220,220));
		acceptedPanel.setFont(font);
		acceptedPanel.setBounds(30+220+30,60, 220,120);
		window.add(acceptedPanel);

		acceptedPanel.setFocusable(false);
		acceptedPanel.setText(acceptedInvitees);
		acceptedPanel.setEditable(false);

		// file names
		openedPanel = new JTextArea();

		openedPanel.setForeground(new Color(0,0,178));
		openedPanel.setBackground(Color.white);//(220,220,220));
		openedPanel.setFont(font);
		openedPanel.setBounds(30+220+30,240, 220,120);
		window.add(openedPanel);

		openedPanel.setFocusable(false);
		openedPanel.setText(successfullyOpened);
		openedPanel.setEditable(false);

		window.validate();
		window.repaint();
	}
}







