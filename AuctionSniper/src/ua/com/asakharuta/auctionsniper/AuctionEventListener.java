package ua.com.asakharuta.auctionsniper;
import static ua.com.asakharuta.auctionsniper.common.Constants.*;

import java.util.EventListener;
public interface AuctionEventListener extends EventListener
{
	enum PriceSource
	{
		OTHER("Some other"), 
		SNIPER(SNIPER_XMPP_ID)
		;

		private final String name;

		private PriceSource(String name){
			this.name = name;
		}
		
		public String getName()
		{
			return this.name;
		}

	}
	
	void auctionClosed();

	void currentPrice(int currentPrice, int increment, PriceSource other);

	void auctionFailed();
}
