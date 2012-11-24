package ua.com.asakharuta.auctionsniper.xmpp;

import ua.com.asakharuta.auctionsniper.Auction;

public interface AuctionHouse
{
	Auction auctionFor(String itemId);
}
