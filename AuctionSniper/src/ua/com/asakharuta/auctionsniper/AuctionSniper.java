package ua.com.asakharuta.auctionsniper;

import ua.com.asakharuta.auctionsniper.common.SniperStatus;

public class AuctionSniper implements AuctionEventListener
{

	private final SniperListener sniperListener;
	private final Auction auction;
	private boolean isWinning = false;
	private final String itemId;

	public AuctionSniper(Auction auction, String itemid, SniperListener sniperListener)
	{
		this.auction = auction;
		this.itemId = itemid;
		this.sniperListener = sniperListener;
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
			sniperListener.sniperWinning();
		}else{
			int bid = currentPrice+increment;
			auction.bid(bid);
			sniperListener.sniperBidding(new SniperSnapshot(this.itemId, currentPrice, bid, SniperStatus.BIDDING));
		}
	}
}
