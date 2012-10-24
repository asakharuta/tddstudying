package ua.com.asakharuta.auctionsniper;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import ua.com.asakharuta.auctionsniper.AuctionEventListener.PriceSource;

public class AuctionMessageTranslator implements MessageListener
{
	private static final String EVENT_TYPE_CLOSE = "CLOSE";
	private static final String EVENT_TYPE_PRICE = "PRICE";
	
	private final AuctionEventListener listener;
	private final String sniperId;

	public AuctionMessageTranslator(String sniperId, AuctionEventListener listener)
	{
		this.sniperId = sniperId;
		this.listener = listener;
	}

	@Override
	public void processMessage(Chat chat, Message message)
	{
		AuctionEvent event = AuctionEvent.from(message.getBody());
		
		String eventType = event.type();
		if(eventType.equals(EVENT_TYPE_CLOSE)){
			listener.auctionClosed();
		}else if(eventType.equals(EVENT_TYPE_PRICE)){
			listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
		}
	}

	public static class AuctionEvent
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

		public PriceSource isFrom(String sniperId)
		{
			return sniperId.equals(bidder()) ?AuctionEventListener.PriceSource.SNIPER :  AuctionEventListener.PriceSource.OTHER ;
		}

		private String bidder()
		{
			return get("Bidder");
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
