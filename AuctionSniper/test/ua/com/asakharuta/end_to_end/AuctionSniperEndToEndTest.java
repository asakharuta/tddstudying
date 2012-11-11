package ua.com.asakharuta.end_to_end;

import org.jivesoftware.smack.XMPPException;
import org.junit.After;
import org.junit.Test;

import static ua.com.asakharuta.auctionsniper.common.Constants.*;
import ua.com.asakharuta.auctionsniper.AuctionEventListener;
import ua.com.asakharuta.auctionsniper.common.Constants;

public class AuctionSniperEndToEndTest
{

	private final FakeAuctionServer auction = new FakeAuctionServer(Constants.ITEM_ID_1);
	private final ApplicationRunner application = new ApplicationRunner();

	@Test
	public void sniperJoinsAuctionUntilAuctionCloses() throws XMPPException, InterruptedException
	{
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLostAuction(0,0);
	}

	@Test
	public void sniperMakesAHigherBidButLoses() throws XMPPException, InterruptedException{
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(SNIPER_XMPP_ID);
		
		int priceBefore = 1000;
		int minimalIncriment = 98;
		int currentPrice = priceBefore + minimalIncriment;
		auction.reportPrice(priceBefore,minimalIncriment,AuctionEventListener.PriceSource.OTHER);
		application.hasShownSniperIsBidding(priceBefore,currentPrice);
		
		auction.hasReceivedBid(currentPrice, SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLostAuction(priceBefore,currentPrice);
	}
	
	@Test
	public void sniperWinAnAuctionByBiddingHihger() throws XMPPException, InterruptedException{
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(SNIPER_XMPP_ID);
		
		int priceBefore = 1000;
		int minimalIncriment = 98;
		int currentPrice = priceBefore + minimalIncriment;
		auction.reportPrice(priceBefore,minimalIncriment,AuctionEventListener.PriceSource.OTHER);
		application.hasShownSniperIsBidding(priceBefore,currentPrice);
		
		auction.hasReceivedBid(currentPrice, SNIPER_XMPP_ID);
		
		int secondIncrement = 97;
		priceBefore = currentPrice;
		currentPrice += secondIncrement;
		auction.reportPrice(priceBefore, secondIncrement, AuctionEventListener.PriceSource.SNIPER);
		application.hasShownSniperIsWinnig(priceBefore);
		
		auction.announceClosed();
		application.showsSniperHasWonAuction(priceBefore);
	}

	@Test
	public void sniperLostAnAuctionByBiddingHihger() throws XMPPException, InterruptedException{
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(SNIPER_XMPP_ID);
		
		int priceBefore = 1000;
		int minimalIncriment = 98;
		int currentPrice = priceBefore + minimalIncriment;
		auction.reportPrice(priceBefore,minimalIncriment,AuctionEventListener.PriceSource.OTHER);
		application.hasShownSniperIsBidding(priceBefore,currentPrice);
		
		auction.hasReceivedBid(currentPrice, SNIPER_XMPP_ID);
		
		int secondIncrement = 97;
		priceBefore = currentPrice;
		currentPrice += secondIncrement;
		auction.reportPrice(priceBefore, secondIncrement, AuctionEventListener.PriceSource.SNIPER);
		application.hasShownSniperIsWinnig(priceBefore);

		int thirdIncrement = 96;
		priceBefore = currentPrice;
		currentPrice += thirdIncrement;
		auction.reportPrice(priceBefore, thirdIncrement, AuctionEventListener.PriceSource.OTHER);
		application.hasShownSniperIsBidding(priceBefore,currentPrice);
		
		auction.hasReceivedBid(currentPrice, SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.showsSniperHasLostAuction(priceBefore,currentPrice);
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
