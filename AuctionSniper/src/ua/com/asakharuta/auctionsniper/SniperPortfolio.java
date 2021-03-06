package ua.com.asakharuta.auctionsniper;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import ua.com.asakharuta.auctionsniper.common.Announcer;

public class SniperPortfolio implements SniperCollector
{

	public interface PortfolioListener extends EventListener
	{
		void sniperAdded(AuctionSniper sniper);
	}

	private final Announcer<PortfolioListener> announcer = Announcer.to(PortfolioListener.class);
	private final List<AuctionSniper> snipers = new ArrayList<AuctionSniper>();

	@Override
	public void addSniper(AuctionSniper sniper)
	{
		snipers.add(sniper);
		announcer.announce().sniperAdded(sniper);
	}

	public void addPortfolioListener(PortfolioListener listener) {
	    announcer.addListener(listener);
	}
	
}
