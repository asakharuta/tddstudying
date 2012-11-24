package ua.com.asakharuta.auctionsniper;

import ua.com.asakharuta.auctionsniper.xmpp.AuctionHouse;

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
	public void joinAuction(final String itemId)
	{
		Auction auction = auctionHouse.auctionFor(itemId);
		AuctionSniper sniper = new AuctionSniper(auction,itemId);
		auction.addAuctionEventListener(sniper);
		collector.addSniper(sniper);
		auction.join();
	}

}
