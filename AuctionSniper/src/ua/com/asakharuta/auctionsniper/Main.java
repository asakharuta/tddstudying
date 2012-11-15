package ua.com.asakharuta.auctionsniper;

import static ua.com.asakharuta.auctionsniper.common.Constants.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import ua.com.asakharuta.auctionsniper.common.Announcer;
import ua.com.asakharuta.auctionsniper.ui.MainWindow;
import ua.com.asakharuta.auctionsniper.ui.SnipersTableModel;

public class Main
{
	public class XMPPAuction implements Auction
	{

		private final Chat chat;
		private final Announcer<AuctionEventListener> auctionEventListeners  = Announcer.to(AuctionEventListener.class);
		
		public XMPPAuction(XMPPConnection connection, String itemId)
		{
			this.chat = connection.getChatManager().createChat(auctionId(itemId,connection), 
					new AuctionMessageTranslator(connection.getUser(),auctionEventListeners.announce()));
		}

		@Override
		public void join()
		{
			sendMessage(JOIN_COMMAND_FORMAT);
		}
		
		@Override
		public void bid(int amount)
		{
			sendMessage(String.format(BID_COMMAND_FORMAT, amount));
		}

		private void sendMessage(String message)
		{
			try
			{
				chat.sendMessage(message);
			} catch (XMPPException e)
			{
				e.printStackTrace();
			}
		}

		private String auctionId(String itemId, XMPPConnection connection)
		{
			return String.format(AUCTION_ID_FORMAT, itemId,connection.getServiceName());
		}
		
		@Override
		public void addAuctionEventListener(AuctionSniper auctionSniper)
		{
			auctionEventListeners.addListener(auctionSniper);
		}

	}

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
		XMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
		main.disconnectWhenUICloses(connection);
		main.addUserRequestListenerFor(connection);
	}
	
	private void addUserRequestListenerFor(final XMPPConnection connection)
	{
		mainWindow.addUserRequestListener(new UserRequestListener()
		{
			
			@Override
			public void joinAuction(final String itemId)
			{
				snipers.addSniper(SniperSnapshot.joining(itemId));
				
				Auction auction = new XMPPAuction(connection,itemId);
				notToBeGarbageCollected.add(auction);
				
				auction.addAuctionEventListener(new AuctionSniper(auction,itemId,new SwingThreadSniperListener(snipers)));
				
				auction.join();
			}
		});
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
				mainWindow = new MainWindow(snipers);
			}
		});
	}
}
