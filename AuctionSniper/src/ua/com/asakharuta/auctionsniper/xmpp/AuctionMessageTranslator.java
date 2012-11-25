package ua.com.asakharuta.auctionsniper.xmpp;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import ua.com.asakharuta.auctionsniper.AuctionEventListener;
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
		
		try{
			translate(message.getBody());
		}catch(Exception parseException){
			listener.auctionFailed();
		}
	}

	private void translate(String body) throws ua.com.asakharuta.auctionsniper.xmpp.AuctionMessageTranslator.AuctionEvent.MissingValueException
	{
		AuctionEvent event = AuctionEvent.from(body);
		
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

		public PriceSource isFrom(String sniperId) throws MissingValueException
		{
			return sniperId.equals(bidder()) ?AuctionEventListener.PriceSource.SNIPER :  AuctionEventListener.PriceSource.OTHER ;
		}

		private String bidder() throws MissingValueException
		{
			return get("Bidder");
		}

		public int currentPrice() throws NumberFormatException, MissingValueException
		{
			return getInt("CurrentPrice");
		}
		
		public int increment() throws NumberFormatException, MissingValueException
		{
			return getInt("Increment");
		}
		
		public String type() throws MissingValueException
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


		private String get(String name) throws MissingValueException
		{
			String value = fields.get(name);
			if(value == null){
				throw new MissingValueException(name);
			}
			return value;
		}
		
		private int getInt(String field) throws NumberFormatException, MissingValueException
		{
			return Integer.parseInt(get(field));
		}
		 
		private static class MissingValueException extends Exception {
			    /**
			 * 
			 */
			private static final long serialVersionUID = -3128979671958977180L;

				public MissingValueException(String fieldName) {
			      super("Missing value for " + fieldName);
			    }
			  }
		 
	}
	
}
