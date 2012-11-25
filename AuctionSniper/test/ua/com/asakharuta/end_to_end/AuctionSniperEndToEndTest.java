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
	private final FakeAuctionServer auction2 = new FakeAuctionServer(Constants.ITEM_ID_2); 
	private final ApplicationRunner application = new ApplicationRunner();

	@Test
	public void sniperJoinsAuctionUntilAuctionCloses() throws XMPPException, InterruptedException
	{
		auction.startSellingItem();
		
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFrom(SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.hasShownSniperHasLostAuction(auction, 0,0);
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
		application.hasShownSniperIsBidding(auction,priceBefore,currentPrice);
		
		auction.hasReceivedBid(currentPrice, SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.hasShownSniperHasLostAuction(auction, priceBefore,currentPrice);
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
		application.hasShownSniperIsBidding(auction,priceBefore,currentPrice);
		
		auction.hasReceivedBid(currentPrice, SNIPER_XMPP_ID);
		
		int secondIncrement = 97;
		priceBefore = currentPrice;
		currentPrice += secondIncrement;
		auction.reportPrice(priceBefore, secondIncrement, AuctionEventListener.PriceSource.SNIPER);
		application.hasShownSniperIsWinnig(auction, priceBefore);
		
		auction.announceClosed();
		application.showsSniperHasWonAuction(auction, priceBefore);
	}

	@Test
	public void sniperBidsForMultipleItems() throws XMPPException, InterruptedException{
		auction.startSellingItem();
		auction2.startSellingItem();
		
		application.startBiddingIn(auction,auction2);
		auction.hasReceivedJoinRequestFrom(SNIPER_XMPP_ID);
		auction2.hasReceivedJoinRequestFrom(SNIPER_XMPP_ID);
		
		auction.reportPrice(1000,98,AuctionEventListener.PriceSource.OTHER);
		auction.hasReceivedBid(1098, SNIPER_XMPP_ID);
		
		auction2.reportPrice(500,21,AuctionEventListener.PriceSource.OTHER);
		auction2.hasReceivedBid(521, SNIPER_XMPP_ID);

		application.hasShownSniperIsBidding(auction,1000,1098);
		application.hasShownSniperIsBidding(auction2,500,521);
		
		auction.reportPrice(1098, 97, AuctionEventListener.PriceSource.SNIPER);
		auction2.reportPrice(521, 20, AuctionEventListener.PriceSource.SNIPER);

		application.hasShownSniperIsWinnig(auction,1098);
		application.hasShownSniperIsWinnig(auction2,521);
		
		auction.announceClosed();
		auction2.announceClosed();
		application.showsSniperHasWonAuction(auction,1098);
		application.showsSniperHasWonAuction(auction2,521);
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
		application.hasShownSniperIsBidding(auction,priceBefore,currentPrice);
		
		auction.hasReceivedBid(currentPrice, SNIPER_XMPP_ID);
		
		int secondIncrement = 97;
		priceBefore = currentPrice;
		currentPrice += secondIncrement;
		auction.reportPrice(priceBefore, secondIncrement, AuctionEventListener.PriceSource.SNIPER);
		application.hasShownSniperIsWinnig(auction, priceBefore);

		int thirdIncrement = 96;
		priceBefore = currentPrice;
		currentPrice += thirdIncrement;
		auction.reportPrice(priceBefore, thirdIncrement, AuctionEventListener.PriceSource.OTHER);
		application.hasShownSniperIsBidding(auction,priceBefore,currentPrice);
		
		auction.hasReceivedBid(currentPrice, SNIPER_XMPP_ID);
		
		auction.announceClosed();
		application.hasShownSniperHasLostAuction(auction, priceBefore,currentPrice);
	}
	
	@Test public void 
	  sniperLosesAnAuctionWhenThePriceIsTooHigh() throws Exception { 
	    auction.startSellingItem(); 
	    
	    application.startBiddingWithStopPrice(auction, 1100); 
	    auction.hasReceivedJoinRequestFrom(SNIPER_XMPP_ID); 
	    auction.reportPrice(1000, 98, AuctionEventListener.PriceSource.OTHER); 
	    application.hasShownSniperIsBidding(auction, 1000, 1098);
	    
	    auction.hasReceivedBid(1098, SNIPER_XMPP_ID); 
	    
	    auction.reportPrice(1197, 10, AuctionEventListener.PriceSource.OTHER); 
	    application.hasShownSniperIsLosing(auction, 1197, 1098); 
	    
	    auction.reportPrice(1207, 10, AuctionEventListener.PriceSource.OTHER); 
	    application.hasShownSniperIsLosing(auction, 1207, 1098); 
	    auction.announceClosed(); 
	    application.hasShownSniperHasLostAuction(auction, 1207, 1098); 
	  } 
	
	@After 
	public void stopAuction(){
		auction.stop();
		auction2.stop();
	}
	
	@After
	public void stopApplicaion(){
		application.stop();
	}
}
