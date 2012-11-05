package ua.com.asakharuta.auctionsniper;

import ua.com.asakharuta.auctionsniper.common.SniperState;

public class AuctionSniper implements AuctionEventListener
{

	private final SniperListener sniperListener;
	private final Auction auction;
	private boolean isWinning = false;
	private SniperSnapshot lastSnapshot;

	public AuctionSniper(Auction auction, String itemid, SniperListener sniperListener)
	{
		this.auction = auction;
		this.sniperListener = sniperListener;
		this.lastSnapshot = SniperSnapshot.joining(itemid);
	}
	
	@Override
	public void auctionClosed()
	{
		if(isWinning ){
			sniperListener.sniperWon();
		}else{
			sniperListener.sniperLost();
		}
	}

	@Override
	public void currentPrice(int currentPrice, int increment, PriceSource priceSource)
	{
		isWinning = priceSource == PriceSource.SNIPER;
		if(isWinning){
			lastSnapshot = lastSnapshot.winning(currentPrice);
		}else{
			int bid = currentPrice+increment;
			auction.bid(bid);
			lastSnapshot = lastSnapshot.bidding(currentPrice,bid);
		}
		sniperListener.sniperStateChanged(lastSnapshot);
	}
}
