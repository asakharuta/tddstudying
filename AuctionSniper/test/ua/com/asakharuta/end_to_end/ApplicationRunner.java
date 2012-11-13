package ua.com.asakharuta.end_to_end;

import ua.com.asakharuta.auctionsniper.Main;
import ua.com.asakharuta.auctionsniper.common.Constants;
import ua.com.asakharuta.auctionsniper.common.SniperState;
import ua.com.asakharuta.auctionsniper.ui.MainWindow;

public class ApplicationRunner
{
	private static final int timeToWait = 1000;
	private AuctionSniperDriver driver = new NullDriver(timeToWait);

	public void startBiddingIn(final FakeAuctionServer ... auctions)
	{
		Thread thread  = new Thread("Test Application"){
			@Override 
			public void run(){
				try{
					Main.main(arguments(auctions));
//					Main.main(Constants.XMPP_HOSTNAME, Constants.SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		
		driver = new AuctionSniperDriver(timeToWait);
		driver.hasTitle(MainWindow.TITLE);
		driver.hasColumnTitles();
		for(FakeAuctionServer auction : auctions){
			driver.showsSniperStatus(auction.getItemId(), 0, 0, SniperState.JOINING);
		}
		
	}

	protected String[] arguments(FakeAuctionServer[] auctions)
	{
		String[] arguments = new String[auctions.length+3];
		arguments[0] = Constants.XMPP_HOSTNAME; 
		arguments[1] = Constants.SNIPER_ID;
		arguments[2] = Constants.SNIPER_PASSWORD;
		for (int i = 0; i < auctions.length; ++i)
		{
			arguments[i + 3]= auctions[i].getItemId();
		}
		return arguments;
	}

	public void showsSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid)
	{
		driver.showsSniperStatus(auction.getItemId(),lastPrice, lastBid, SniperState.LOST);
	}

	public void stop()
	{
		driver.dispose();
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction, int price, int bid)
	{
		driver.showsSniperStatus(auction.getItemId(),price,bid,SniperState.BIDDING);
	}

	public void hasShownSniperIsWinnig(FakeAuctionServer auction, int winningPrice)
	{
		driver.showsSniperStatus(auction.getItemId(),winningPrice,winningPrice,SniperState.WINNING);
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction, int price)
	{
		driver.showsSniperStatus(auction.getItemId(),price,price,SniperState.WON);
	}

}
