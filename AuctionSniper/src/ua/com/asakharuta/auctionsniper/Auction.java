package ua.com.asakharuta.auctionsniper;

public interface Auction
{
	void bid(int i);

	void join();

	void addAuctionEventListener(AuctionSniper auctionSniper);
}
