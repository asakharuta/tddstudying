package ua.com.asakharuta.end_to_end;

import static ua.com.asakharuta.auctionsniper.ui.SnipersTableModel.textFor;
import ua.com.asakharuta.auctionsniper.Main;
import ua.com.asakharuta.auctionsniper.common.Constants;
import ua.com.asakharuta.auctionsniper.common.SniperState;
import ua.com.asakharuta.auctionsniper.ui.MainWindow;

public class ApplicationRunner
{
	private static final int timeToWait = 1000;
	private AuctionSniperDriver driver = new NullDriver(timeToWait);

	public void startBiddingIn(final FakeAuctionServer... auctions)
	{

		startSniper();
		for (FakeAuctionServer auction : auctions)
		{
			openBiddingFor(auction, Integer.MAX_VALUE);
		}
	}

	private void openBiddingFor(FakeAuctionServer auction, int stopPrice)
	{
		final String itemId = auction.getItemId();
		driver.startBiddingFor(itemId,stopPrice);
		driver.showsSniperStatus(itemId, 0, 0, textFor(SniperState.JOINING));
	}

	public void startBiddingWithStopPrice(FakeAuctionServer auction,
			int stopPrice)
	{
		startSniper();
		openBiddingFor(auction, stopPrice);
	}

	private void startSniper()
	{
		Thread thread = new Thread("Test Application")
		{
			@Override
			public void run()
			{
				try
				{
					Main.main(Constants.XMPP_HOSTNAME, Constants.SNIPER_ID,
							Constants.SNIPER_PASSWORD);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();

		driver = new AuctionSniperDriver(timeToWait);
		driver.hasTitle(MainWindow.TITLE);
		driver.hasColumnTitles();
	}

	protected String[] arguments(FakeAuctionServer[] auctions)
	{
		String[] arguments = new String[auctions.length + 3];
		arguments[0] = Constants.XMPP_HOSTNAME;
		arguments[1] = Constants.SNIPER_ID;
		arguments[2] = Constants.SNIPER_PASSWORD;
		for (int i = 0; i < auctions.length; ++i)
		{
			arguments[i + 3] = auctions[i].getItemId();
		}
		return arguments;
	}

	public void hasShownSniperHasLostAuction(FakeAuctionServer auction,
			int lastPrice, int lastBid)
	{
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid,
				textFor(SniperState.LOST));
	}

	public void stop()
	{
		driver.dispose();
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction, int price,
			int bid)
	{
		driver.showsSniperStatus(auction.getItemId(), price, bid,
				textFor(SniperState.BIDDING));
	}

	public void hasShownSniperIsWinnig(FakeAuctionServer auction,
			int winningPrice)
	{
		driver.showsSniperStatus(auction.getItemId(), winningPrice,
				winningPrice, textFor(SniperState.WINNING));
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction, int price)
	{
		driver.showsSniperStatus(auction.getItemId(), price, price,
				textFor(SniperState.WON));
	}

	public void hasShownSniperIsLosing(FakeAuctionServer auction, int lastPrice, int lastBid)
	{
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, textFor(SniperState.LOSING));
	}
}
