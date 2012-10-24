package ua.com.asakharuta.unit;

import static org.junit.Assert.*;

import java.util.EventListener;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Test;

import ua.com.asakharuta.auctionsniper.Auction;
import ua.com.asakharuta.auctionsniper.AuctionEventListener;
import ua.com.asakharuta.auctionsniper.AuctionSniper;
import ua.com.asakharuta.auctionsniper.SniperListener;

public class AuctionSniperTest
{
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = context.mock(SniperListener.class);
	private final Auction auction = context.mock(Auction.class);
	private final AuctionEventListener sniper = new AuctionSniper(auction,sniperListener);
	
	@Test
	public void reportsLostWhenAuctionClosed()
	{
		context.checking(new Expectations(){{
				atLeast(1).of(sniperListener).sniperLost();
			}}
		);
		
		sniper.auctionClosed();
	}
	
	@Test
	public void bidsHigherAndReportsBiddingWhenNewPriceArrives(){
		final int price = 1001;
		final int increment = 25;
		context.checking(new Expectations(){{
				one(auction).bid(price+increment);
				atLeast(1).of(sniperListener).sniperBidding();
			}}
		);
		
		sniper.currentPrice(price, increment);
	}

	@After
	public void checkExpectations(){
		context.assertIsSatisfied();
	}
}
