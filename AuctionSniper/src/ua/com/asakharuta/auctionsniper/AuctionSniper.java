package ua.com.asakharuta.auctionsniper;

import ua.com.asakharuta.auctionsniper.common.Announcer;


public class AuctionSniper implements AuctionEventListener
{

	private final Announcer<SniperListener> listeners = Announcer.to(SniperListener.class);
	private final Auction auction;
	private SniperSnapshot lastSnapshot;

	public AuctionSniper(Auction auction, String itemid)
	{
		this.auction = auction;
		this.lastSnapshot = SniperSnapshot.joining(itemid);
	}
	
	@Override
	public void auctionClosed()
	{
		lastSnapshot = lastSnapshot.closed();
		notifySnapshotHasChanged();
	}

	@Override
	public void currentPrice(int currentPrice, int increment, PriceSource priceSource)
	{
		if(priceSource == PriceSource.SNIPER){
			lastSnapshot = lastSnapshot.winning(currentPrice);
		}else if (priceSource == PriceSource.OTHER){
			int bid = currentPrice+increment;
			auction.bid(bid);
			lastSnapshot = lastSnapshot.bidding(currentPrice,bid);
		}
		notifySnapshotHasChanged();
	}
	
	private void notifySnapshotHasChanged()
	{
		listeners.announce().sniperStateChanged(lastSnapshot);
	}

	public SniperSnapshot getSnapshot()
	{
		return lastSnapshot;
	}

	public void addSniperListener(
			SniperListener listener)
	{
		listeners.addListener(listener);
	}
}
