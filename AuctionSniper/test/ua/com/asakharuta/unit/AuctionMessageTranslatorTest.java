package ua.com.asakharuta.unit;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Test;

import ua.com.asakharuta.auctionsniper.AuctionEventListener;
import ua.com.asakharuta.auctionsniper.common.Constants;
import ua.com.asakharuta.auctionsniper.xmpp.AuctionMessageTranslator;

public class AuctionMessageTranslatorTest
{
	private final Mockery context = new Mockery();
	private final AuctionEventListener listener = context.mock(AuctionEventListener.class);
	
	private static final Chat UNUSED_NULL_CHAT = null;
	private final AuctionMessageTranslator translator = new AuctionMessageTranslator(Constants.SNIPER_ID,listener);
	
	@Test
	public void notifiesAuctionClosedWhenCloseMessageReceived()
	{
		context.checking(new Expectations(){{
				oneOf(listener).auctionClosed();
			}}
		);
	
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: CLOSE;");
		translator.processMessage(UNUSED_NULL_CHAT, message);
	}

	@Test
	public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder()
	{
		final int currentPrice = 192;
		final int increment = 7;
		
		context.checking(new Expectations(){{
				oneOf(listener).currentPrice(currentPrice,increment,AuctionEventListener.PriceSource.OTHER);
			}}
		);
	
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice:"+ currentPrice +"; Increment: "+ increment + "; Bidder: Someone else;");
		translator.processMessage(UNUSED_NULL_CHAT, message);
	}
	
	@Test
	public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper()
	{
		final int currentPrice = 234;
		final int increment = 5;
		
		context.checking(new Expectations(){{
				oneOf(listener).currentPrice(currentPrice,increment,AuctionEventListener.PriceSource.SNIPER);
			}}
		);
		
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice:"+ currentPrice +"; Increment: "+ increment + "; Bidder: " + Constants.SNIPER_ID+";");
		translator.processMessage(UNUSED_NULL_CHAT, message);
	}
	
	@Test public void 
	  notifiesAuctionFailedWhenBadMessageReceived() { 
	    String badMessage = "a bad message";
	    context.checking(new Expectations() {{  
	        oneOf(listener).auctionFailed(); 
	      }}); 
	    Message message = new Message();
	    message.setBody(badMessage);
	    
	    translator.processMessage(UNUSED_NULL_CHAT,message); 
	  } 
	
	@Test public void 
	  notifiesAuctionFailedWhenEventTypeMissing() { 
	    String badMessage = "SOLVersion: 1.1; CurrentPrice: 234; Increment: 5; Bidder: " + Constants.SNIPER_ID + ";";
	    context.checking(new Expectations() {{  
	        oneOf(listener).auctionFailed(); 
	      }}); 
	    Message message = new Message();
	    message.setBody(badMessage);

	    translator.processMessage(UNUSED_NULL_CHAT, message); 
	  } 
	
	@Test public void 
	  notifiesAuctionFailedWhenPriceMissing() { 
		final int increment = 5;
	    String badMessage = "SOLVersion: 1.1; Event: PRICE; Increment: "+ increment + "; Bidder: " + Constants.SNIPER_ID+";";
	    context.checking(new Expectations() {{  
	        oneOf(listener).auctionFailed(); 
	      }}); 
	    Message message = new Message();
	    message.setBody(badMessage);

	    translator.processMessage(UNUSED_NULL_CHAT, message); 
	  } 
	
	@After
	public void checkExpectations(){
		context.assertIsSatisfied();
	}

}
