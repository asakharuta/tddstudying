package ua.com.asakharuta.auctionsniper;


public class AuctionSniper implements AuctionEventListener
{

	private final SniperListener sniperListener;
	private final Auction auction;
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
		sniperListener.sniperStateChanged(lastSnapshot);
	}
}
