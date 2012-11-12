package ua.com.asakharuta.auctionsniper;

import java.util.EventListener;

public interface SniperListener extends EventListener
{
	void sniperStateChanged(final SniperSnapshot sniperSnapshot);
}
