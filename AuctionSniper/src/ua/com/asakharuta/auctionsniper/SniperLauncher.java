package ua.com.asakharuta.auctionsniper;


public class SniperLauncher implements UserRequestListener
{
	private final SniperCollector collector;
	private final AuctionHouse auctionHouse;
	
	public SniperLauncher(AuctionHouse auctionHouse,
			SniperCollector collector)
	{
		this.auctionHouse = auctionHouse;
		this.collector = collector;
	}

	@Override
	public void joinAuction(final Item item)
	{
		Auction auction = auctionHouse.auctionFor(item);
		AuctionSniper sniper = new AuctionSniper(auction,item);
		auction.addAuctionEventListener(sniper);
		collector.addSniper(sniper);
		auction.join();
	}

}
