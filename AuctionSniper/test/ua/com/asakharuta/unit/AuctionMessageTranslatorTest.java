package ua.com.asakharuta.unit;

import static org.junit.Assert.*;

import org.hamcrest.Description;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Expectation;
import org.jmock.api.Invocation;
import org.junit.Test;

import ua.com.asakharuta.auctionsniper.AuctionEventListener;
import ua.com.asakharuta.auctionsniper.AuctionMessageTranslator;

public class AuctionMessageTranslatorTest
{
	private final Mockery context = new Mockery();
	private final AuctionEventListener listener = context.mock(AuctionEventListener.class);
	
	private static final Chat UNUSED_NULL_CHAT = null;
	private final AuctionMessageTranslator translator = new AuctionMessageTranslator(listener);
	
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
		
		context.assertIsSatisfied();
	}

	@Test
	public void notifiesBidDetailsWhenCurrentPriceMessageReceived()
	{
		final int currentPrice = 192;
		final int increment = 7;
		
		context.checking(new Expectations(){{
				oneOf(listener).currentPrice(currentPrice,increment);
			}}
		);
	
		Message message = new Message();
		message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice:"+ currentPrice +"; Increment: "+ increment + "; Bidder: Someone else;");
		translator.processMessage(UNUSED_NULL_CHAT, message);
		
		context.assertIsSatisfied();
	}
}
