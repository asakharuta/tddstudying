package ua.com.asakharuta.auctionsniper;

import ua.com.asakharuta.auctionsniper.common.Announcer;


public class AuctionSniper implements AuctionEventListener
{

	private final Announcer<SniperListener> listeners = Announcer.to(SniperListener.class);
	private final Auction auction;
	private SniperSnapshot lastSnapshot;
	private final Item item;

	public AuctionSniper(Auction auction, Item item)
	{
		this.auction = auction;
		this.item = item;
		this.lastSnapshot = SniperSnapshot.joining(item.identifier);
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
			if(item.allowsBid(bid)){
				auction.bid(bid);
				lastSnapshot = lastSnapshot.bidding(currentPrice,bid);
			}else{
				lastSnapshot = lastSnapshot.losing(currentPrice);
			}
			
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

	@Override
	public void auctionFailed()
	{
		lastSnapshot = lastSnapshot.failed();
		notifySnapshotHasChanged();
	}
}
