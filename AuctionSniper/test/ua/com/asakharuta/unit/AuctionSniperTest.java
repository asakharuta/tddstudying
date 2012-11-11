package ua.com.asakharuta.unit;

import static org.hamcrest.CoreMatchers.*;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
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
import ua.com.asakharuta.auctionsniper.common.SniperState;

public class AuctionSniperTest
{
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = context.mock(SniperListener.class);
	private final Auction auction = context.mock(Auction.class);
	private final AuctionEventListener sniper = new AuctionSniper(auction,Constants.ITEM_ID_1,sniperListener);
	private final States sniperState = context.states("sniper");
	
	private  Matcher<SniperSnapshot> aSniperThatIs(final SniperState state)
	{
		return new FeatureMatcher<SniperSnapshot, SniperState>(equalTo(state),"",""){

			@Override
			protected SniperState featureValueOf(SniperSnapshot actual)
			{
				return actual.state;
			}
		};
	}
	
	@Test
	public void reportsLostWhenAuctionClosesImmidiately()
	{
		context.checking(new Expectations(){{
			atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(Constants.ITEM_ID_1, 0, 0, SniperState.LOST));
			}}
		);
		
		sniper.auctionClosed();
	}
	
	@Test
	public void reportsLostIfAuctionClosesWhenBidding(){
		final int price = 123;
		final int increment = 12;
		context.checking(new Expectations(){{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));then(sniperState.is("bidding"));
//				atLeast(1).of(sniperListener).sniperLost(); when(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(Constants.ITEM_ID_1, price, price+increment, SniperState.LOST)); when(sniperState.is("bidding"));
			}}
		);
		sniper.currentPrice(price, increment, AuctionEventListener.PriceSource.OTHER);
		sniper.auctionClosed();
	}
	
	@Test
	public void reportsIsWinningWhenCurrentPriceComesFromSniper(){
		final int price = 123;
		int priceBefore = price;
		final int increment = 12;
		final int bid = price + increment;
		int currentPrice = bid; 
		context.checking(new Expectations(){{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));then(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(Constants.ITEM_ID_1, bid, bid, SniperState.WINNING)); when(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(Constants.ITEM_ID_1, bid, bid, SniperState.WON)); 
			}}
		);
		
		sniper.currentPrice(priceBefore, increment, AuctionEventListener.PriceSource.OTHER);
		priceBefore = currentPrice;
		int secondIncrement = 45;
		currentPrice = priceBefore + secondIncrement;
		sniper.currentPrice(priceBefore, secondIncrement, AuctionEventListener.PriceSource.SNIPER);
		sniper.auctionClosed();
	}
	
	
	@Test
	public void bidsHigherAndReportsBiddingWhenNewPriceArrives(){
		final int price = 1001;
		final int increment = 25;
		final int bid = price + increment;
		context.checking(new Expectations(){{
				one(auction).bid(bid);
				atLeast(1).of(sniperListener).sniperStateChanged( new SniperSnapshot(Constants.ITEM_ID_1, price, bid, SniperState.BIDDING));
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
				allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.WINNING))); then(sniperState.is("winning"));
				atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapshot(Constants.ITEM_ID_1, price, 0, SniperState.WON));when(sniperState.is("winning")); 
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
