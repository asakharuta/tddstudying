package ua.com.asakharuta.auctionsniper;

import static ua.com.asakharuta.auctionsniper.common.Constants.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import ua.com.asakharuta.auctionsniper.common.SniperStatus;
import ua.com.asakharuta.auctionsniper.ui.MainWindow;

public class Main
{
	private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	private static final int ARG_ITEM_ID = 3;
	
	private MainWindow mainWindow;
	
	@SuppressWarnings("unused")
	private Chat notToBeGarbageCollected;
	
	public Main() throws InterruptedException, InvocationTargetException{
		startUserInterface();
	}
	
	public static void main(String... args) throws InterruptedException, InvocationTargetException, XMPPException{
		Main main = new Main();
		main.joinAuction(connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]),args[ARG_ITEM_ID]);
	}
	
	private void joinAuction(XMPPConnection connection, String itemId) throws XMPPException
	{
		disconnectWhenUICloses(connection);
		Chat chat  = connection.getChatManager().createChat(auctionId(itemId,connection), 
				new MessageListener()
		{
			
			@Override
			public void processMessage(Chat arg0, Message arg1)
			{
				mainWindow.showStatus(SniperStatus.LOST);
			}
		});
		this.notToBeGarbageCollected = chat;
		
		chat.sendMessage(JOIN_COMMAND_FORMAT);
	}

	private void disconnectWhenUICloses(final XMPPConnection connection)
	{
		mainWindow.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e){
				connection.disconnect();
			}
		});
	}

	private static String auctionId(String itemId, XMPPConnection connection)
	{
		return String.format(AUCTION_ID_FORMAT, itemId,connection.getServiceName());
	}

	private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException
	{
		XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect(); 
	    connection.login(username, password, AUCTION_RESOURCE); 
		return connection;
	}

	private void startUserInterface() throws InterruptedException, InvocationTargetException
	{
		SwingUtilities.invokeAndWait(new Runnable()
		{
			
			@Override
			public void run()
			{
				mainWindow = new MainWindow();
			}
		});
	}
}
