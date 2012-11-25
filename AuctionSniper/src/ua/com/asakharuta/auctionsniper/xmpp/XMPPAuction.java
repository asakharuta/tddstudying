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
		AuctionMessageTranslator translator = translatorFor(connection);
		this.chat = connection.getChatManager().createChat(itemId, 
				translator);
		addAuctionEventListener(chatDisconnectorFor(translator));
	}

	private AuctionEventListener chatDisconnectorFor(
			final AuctionMessageTranslator translator)
	{
		return new AuctionEventListener()
		{
			
			@Override
			public void currentPrice(int currentPrice, int increment, PriceSource other)
			{
				// not implemented
			}
			
			@Override
			public void auctionFailed()
			{
				chat.removeMessageListener(translator);
			}
			
			@Override
			public void auctionClosed()
			{
				// not implemented
			}
		};
	}

	private AuctionMessageTranslator translatorFor(XMPPConnection connection)
	{
		return new AuctionMessageTranslator(connection.getUser(),auctionEventListeners.announce());
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

	@Override
	public void addAuctionEventListener(AuctionEventListener auctionSniper)
	{
		auctionEventListeners.addListener(auctionSniper);
	}

}
