package ua.com.asakharuta.unit;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.junit.After;
import org.junit.Test;

import ua.com.asakharuta.auctionsniper.Auction;
import ua.com.asakharuta.auctionsniper.AuctionEventListener;
import ua.com.asakharuta.auctionsniper.AuctionSniper;
import ua.com.asakharuta.auctionsniper.SniperListener;
import ua.com.asakharuta.auctionsniper.SniperSnapshot;
import ua.com.asakharuta.auctionsniper.common.Constants;
import ua.com.asakharuta.auctionsniper.common.SniperStatus;

public class AuctionSniperTest
{
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = context.mock(SniperListener.class);
	private final Auction auction = context.mock(Auction.class);
	private final AuctionEventListener sniper = new AuctionSniper(auction,Constants.ITEM_ID_1,sniperListener);
	private final States sniperState = context.states("sniper");
	
	@Test
	public void reportsLostWhenAuctionClosesImmidiately()
	{
		context.checking(new Expectations(){{
				atLeast(1).of(sniperListener).sniperLost();
			}}
		);
		
		sniper.auctionClosed();
	}
	
	@Test
	public void reportsLostIfAuctionClosesWhenBidding(){
		context.checking(new Expectations(){{
				ignoring(auction);
				allowing(sniperListener).sniperBidding(with(any(SniperSnapshot.class)));then(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperLost(); when(sniperState.is("bidding"));
			}}
		);
		final int price = 123;
		final int increment = 45;
		sniper.currentPrice(price, increment, AuctionEventListener.PriceSource.OTHER);
		sniper.auctionClosed();
	}
	
	@Test
	public void bidsHigherAndReportsBiddingWhenNewPriceArrives(){
		final int price = 1001;
		final int increment = 25;
		final int bid = price + increment;
		context.checking(new Expectations(){{
				one(auction).bid(bid);
				atLeast(1).of(sniperListener).sniperBidding( new SniperSnapshot(Constants.ITEM_ID_1, price, bid, SniperStatus.BIDDING));
			}}
		);
		
		sniper.currentPrice(price, increment,AuctionEventListener.PriceSource.OTHER);
	}

	@Test
	public void reportWonIfAuctionClosesWhenWinning(){
		final int price = 123;
		final int increment = 45;
		context.checking(new Expectations(){{
				ignoring(auction);
				allowing(sniperListener).sniperWinning(); then(sniperState.is("winning"));
				atLeast(1).of(sniperListener).sniperWon(); when(sniperState.is("winning"));
			}}
		);
		
		sniper.currentPrice(price, increment,AuctionEventListener.PriceSource.SNIPER);
		sniper.auctionClosed();
	}
	
	@After
	public void checkExpectations(){
		context.assertIsSatisfied();
	}
}
