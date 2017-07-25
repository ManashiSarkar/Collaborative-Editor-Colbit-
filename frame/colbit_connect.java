public class colbit_connect
{
	public static void main(String[] args) 
	{
		ConnectionFrame frame = new ConnectionFrameA();
		frame.postProcessing();
		// the server's files will be handled as a client
		FileClient.main(null);
	}
}

interface connectionInterface
{
	public void postProcessing();
}

/*
relevant java files:

interaction:
colbitserverpeershandler
fileclient
colbiteditor

other backend files:
fileserver

UI related:
display
colbiteditorsplitpanes
colbitframe
colbitframeA
*/


