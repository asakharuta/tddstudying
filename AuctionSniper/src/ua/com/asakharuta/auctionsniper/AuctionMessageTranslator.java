package ua.com.asakharuta.auctionsniper;

import java.util.HashMap;
import java.util.Map;

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
	public void processMessage(Chat chat, Message message)
	{
		Map<String, String> event = unpackEventFrom(message);
		
		String eventType = event.get("Event");
		if(eventType.equals("CLOSE")){
			listener.auctionClosed();
		}else if(eventType.equals("PRICE")){
			listener.currentPrice(Integer.parseInt(event.get("CurrentPrice")), Integer.parseInt(event.get("Increment")));
		}
	}

	private Map<String, String> unpackEventFrom(Message message)
	{
		HashMap<String, String> event = new HashMap<String,String>();
		for (String element : message.getBody().split(";"))
		{
			String[] pair = element.split(":");
			event.put(pair[0].trim(), pair[1].trim());
		}
		return event;
	}

}
