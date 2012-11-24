package ua.com.asakharuta.auctionsniper.xmpp;

import static ua.com.asakharuta.auctionsniper.common.Constants.BID_COMMAND_FORMAT;
import static ua.com.asakharuta.auctionsniper.common.Constants.JOIN_COMMAND_FORMAT;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import ua.com.asakharuta.auctionsniper.Auction;
import ua.com.asakharuta.auctionsniper.AuctionEventListener;
import ua.com.asakharuta.auctionsniper.common.Announcer;

public class XMPPAuction implements Auction
{

	private final Chat chat;
	private final Announcer<AuctionEventListener> auctionEventListeners  = Announcer.to(AuctionEventListener.class);
	
	public XMPPAuction(XMPPConnection connection, String itemId)
	{
		this.chat = connection.getChatManager().createChat(itemId, 
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
			System.out.println("sending message " + message);
			chat.sendMessage(message);
		} catch (XMPPException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void addAuctionEventListener(AuctionEventListener auctionSniper)
	{
		auctionEventListeners.addListener(auctionSniper);
	}

}
