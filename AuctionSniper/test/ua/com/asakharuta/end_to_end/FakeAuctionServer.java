package ua.com.asakharuta.end_to_end;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import ua.com.asakharuta.auctionsniper.AuctionEventListener;
import static ua.com.asakharuta.auctionsniper.common.Constants.*;

public class FakeAuctionServer
{
	private final String itemId;
	private final XMPPConnection connection;

	private Chat currentChat;

	private final SingleMessageListener messageListener = new SingleMessageListener();

	public FakeAuctionServer(String itemId)
	{
		this.itemId = itemId;
		this.connection = new XMPPConnection(XMPP_HOSTNAME);
	}

	public void startSellingItem() throws XMPPException
	{
		connection.connect();
		connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD,AUCTION_RESOURCE);
		connection.getChatManager().addChatListener(new ChatManagerListener()
		{
			
			@Override
			public void chatCreated(Chat chat, boolean createdLocally)
			{
				currentChat = chat;
				currentChat.addMessageListener(messageListener);
			}
		});
	}

	public String getItemId()
	{
		return itemId;
	}

	public void hasReceivedJoinRequestFrom(String sniperXmppId) throws InterruptedException
	{
		receivesAMessageMatching(sniperXmppId,equalTo(JOIN_COMMAND_FORMAT));
	}

	public void announceClosed() throws XMPPException
	{
		currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
	}

	public void stop()
	{
		connection.disconnect();
	}

	public void reportPrice(int price, int increment, AuctionEventListener.PriceSource bidder) throws XMPPException
	{
		currentChat.sendMessage(String.format("SOLVersion: 1.1; Event: PRICE; " 
                + "CurrentPrice: %d; Increment: %d; Bidder: %s;", 
                price, increment, bidder.getName()));
	}

	public void hasReceivedBid(int bid, String sniperXmppId) throws InterruptedException
	{
		receivesAMessageMatching(sniperXmppId, equalTo(String.format(BID_COMMAND_FORMAT, bid)));
	}
	
	private void receivesAMessageMatching(String sniperXmppId,
			Matcher<String> messageMatcher) throws InterruptedException
	{
		messageListener.receivesAMessage(messageMatcher);
		assertThat(currentChat.getParticipant(), equalTo(sniperXmppId));
	}

	public XMPPConnection getConnection()
	{
		return connection;
	}
}
