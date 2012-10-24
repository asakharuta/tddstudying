package ua.com.asakharuta.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class AuctionMessageTranslator implements MessageListener
{

	private final AuctionEventListener listener;

	public AuctionMessageTranslator(AuctionEventListener listener)
	{
		this.listener = listener;
	}

	@Override
	public void processMessage(Chat arg0, Message arg1)
	{
		listener.auctionClosed();
	}

}
