package ua.com.asakharuta.auctionsniper;

import java.util.EventListener;

public interface SniperListener extends EventListener
{
	void sniperLost();

	void sniperBidding();

	void sniperWinning();
}
