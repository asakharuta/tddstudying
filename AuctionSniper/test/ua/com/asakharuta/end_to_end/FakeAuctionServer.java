package ua.com.asakharuta.end_to_end;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import static ua.com.asakharuta.auctionsniper.common.Constants.*;
public class FakeAuctionServer
{
	static final String XMPP_HOSTNAME = "user-hp";

	private final String itemId;
	private final Connection connection;

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
				chat.addMessageListener(messageListener);
			}
		});
	}

	public String getItemId()
	{
		return itemId;
	}

	public void hasReceivedJoinRequestFromSniper() throws InterruptedException
	{
		messageListener.receivesAMessage();
	}

	public void announceClosed() throws XMPPException
	{
		currentChat.sendMessage(new Message());
	}

	public void stop()
	{
		connection.disconnect();
	}

}
