package ua.com.asakharuta.end_to_end;

import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Test;

import static ua.com.asakharuta.auctionsniper.common.Constants.*;
import ua.com.asakharuta.auctionsniper.common.Winner;

public class AuctionSniperEndToEndTest
{

	private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private final ApplicationRunner application = new ApplicationRunner();

	@Test
	public void sniperJoinsAuctionUntilAuctionCloses() throws XMPPException, InterruptedException
	{
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}

	@Test
	public void sniperMakesAHigherBidButLoses() throws XMPPException, InterruptedException{
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(SNIPER_XMPP_ID);
		
		int price = 1000;
		int minimalIncriment = 98;
		auction.reportPrice(price,minimalIncriment,Winner.OTHER);
		application.hasShownSniperIsBidding();
		
		auction.hasReceivedBid(price+minimalIncriment, SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}
	
	@After 
	public void stopAuction(){
		auction.stop();
	}
	
	@After
	public void stopApplicaion(){
		application.stop();
	}
}