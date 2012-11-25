package ua.com.asakharuta.integration.xmpp;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ua.com.asakharuta.auctionsniper.Auction;
import ua.com.asakharuta.auctionsniper.AuctionEventListener;
import ua.com.asakharuta.auctionsniper.Item;
import ua.com.asakharuta.auctionsniper.common.Constants;
import ua.com.asakharuta.auctionsniper.xmpp.XMPPAuctionHouse;
import ua.com.asakharuta.end_to_end.FakeAuctionServer;
public class XMPPAuctionTest
{
	private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");  
	  private XMPPAuctionHouse auctionHouse; 

	  @Before public void openConnection() throws XMPPException {
	    auctionHouse = XMPPAuctionHouse.connect(Constants.XMPP_HOSTNAME, Constants.SNIPER_ID, Constants.SNIPER_PASSWORD);
	    
	  }
	  @After public void closeConnection() {
	    if (auctionHouse != null) {
	      auctionHouse.disconnect();
	    }
	  }
	  @Before public void startAuction() throws XMPPException {
	    auctionServer.startSellingItem();
	  }
	  @After public void stopAuction() {
	    auctionServer.stop();
	  }


	  @Test public void 
	  receivesEventsFromAuctionServerAfterJoining() throws Exception { 
	    CountDownLatch auctionWasClosed = new CountDownLatch(1); 
	    
	    Auction auction = auctionHouse.auctionFor(new Item(auctionServer.getItemId(),Integer.MAX_VALUE));
	    auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
	    auction.join(); 
	    auctionServer.hasReceivedJoinRequestFrom(Constants.SNIPER_XMPP_ID); 
	    auctionServer.announceClosed(); 
	    
	    assertTrue("should have been closed", auctionWasClosed.await(4, TimeUnit.SECONDS)); 
	  } 

	private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed)
	{
		return new AuctionEventListener()
		{
			
			@Override
			public void currentPrice(int currentPrice, int increment, PriceSource other)
			{
				//not implemented
			}
			
			@Override
			public void auctionClosed()
			{
				auctionWasClosed.countDown();
			}
		};
	}

}
