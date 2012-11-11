package ua.com.asakharuta.unit;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Test;

import ua.com.asakharuta.auctionsniper.AuctionEventListener;
import ua.com.asakharuta.auctionsniper.AuctionMessageTranslator;
import ua.com.asakharuta.auctionsniper.common.Constants;

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
	
	@After
	public void checkExpectations(){
		context.assertIsSatisfied();
	}

}
