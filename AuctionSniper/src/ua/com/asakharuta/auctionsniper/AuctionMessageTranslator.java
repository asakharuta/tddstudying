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
		AuctionEvent event = AuctionEvent.from(message.getBody());
		
		String eventType = event.type();
		if(eventType.equals("CLOSE")){
			listener.auctionClosed();
		}else if(eventType.equals("PRICE")){
			listener.currentPrice(event.currentPrice(), event.increment());
		}
	}

	private static class AuctionEvent
	{
		private Map<String, String> fields  = new HashMap<String, String>();
		
		public static AuctionEvent from(String messageBody)
		{
			AuctionEvent event = new AuctionEvent();
			for (String field : fieldsIn(messageBody))
			{
				event.addField(field);
			}
			return event;
		}

		public int currentPrice()
		{
			return getInt("CurrentPrice");
		}
		
		public int increment()
		{
			return getInt("Increment");
		}
		
		public String type()
		{
			return get("Event");
		}

		private void addField(String field)
		{
			String[] pair = field.split(":");
			fields.put(pair[0].trim(), pair[1].trim());
		}

		private static String[] fieldsIn(String messageBody)
		{
			return messageBody.split(";");
		}


		private String get(String field)
		{
			return fields.get(field);
		}
		
		private int getInt(String field)
		{
			return Integer.parseInt(get(field));
		}
	}
	
}
