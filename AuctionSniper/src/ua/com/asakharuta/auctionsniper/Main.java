package ua.com.asakharuta.auctionsniper;

import static ua.com.asakharuta.auctionsniper.common.Constants.AUCTION_RESOURCE;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import ua.com.asakharuta.auctionsniper.ui.MainWindow;
import ua.com.asakharuta.auctionsniper.ui.SnipersTableModel;
import ua.com.asakharuta.auctionsniper.xmpp.XMPPAuctionHouse;

public class Main
{
	private static final int ARG_HOSTNAME = 0;
    private static final int ARG_USERNAME = 1;
	private static final int ARG_PASSWORD = 2;
	
	private final SnipersTableModel snipers = new SnipersTableModel();
	private MainWindow mainWindow;
	
	private final List<Auction> notToBeGarbageCollected = new ArrayList<Auction>();
	
	public Main() throws InterruptedException, InvocationTargetException{
		startUserInterface();
	}
	
	public static void main(String... args) throws InterruptedException, InvocationTargetException, XMPPException{
		System.out.println(Arrays.toString(args));
		Main main = new Main();
		XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.disconnectWhenUICloses(auctionHouse);
		main.addUserRequestListenerFor(auctionHouse);
	}
	
	private void addUserRequestListenerFor(final XMPPAuctionHouse auctionHouse)
	{
		mainWindow.addUserRequestListener(new UserRequestListener()
		{
			
			@Override
			public void joinAuction(final String itemId)
			{
				snipers.addSniper(SniperSnapshot.joining(itemId));
				
				Auction auction = auctionHouse.auctionFor(itemId);
				notToBeGarbageCollected.add(auction);
				
				auction.addAuctionEventListener(new AuctionSniper(auction,itemId,new SwingThreadSniperListener(snipers)));
				
				auction.join();
			}
		});
	}

	private void disconnectWhenUICloses(final XMPPAuctionHouse auctionHouse)
	{
		mainWindow.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e){
				auctionHouse.disconnect();
			}
		});
	}


	public static XMPPConnection connection(String hostname, String username, String password) throws XMPPException
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
				mainWindow = new MainWindow(snipers);
			}
		});
	}
}
