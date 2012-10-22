package test.ua.com.asakharuta.end_to_end;

import static org.junit.Assert.*;

import org.junit.Test;

public class AuctuinSniperEndToEndTest
{

	private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private final ApplicationRunner application = new ApplicationRunner();

	@Test
	public void sniperJoinsAuctionUntilAuctionCloses()
	{
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFromSniper();
		auction.announceClosed();
		application.showsSniperHasLostAuction();
	}

}
