package ua.com.asakharuta.end_to_end;

import ua.com.asakharuta.auctionsniper.Main;
import ua.com.asakharuta.auctionsniper.common.Constants;
import ua.com.asakharuta.auctionsniper.common.SniperStatus;

public class ApplicationRunner
{
	private static final String SNIPER_PASSWORD = "sniper";
	private static final int timeToWait = 1000;
	private AuctionSniperDriver driver = new NullDriver(timeToWait);
	private String itemId;

	public void startBiddingIn(final FakeAuctionServer auction)
	{
		itemId  = auction.getItemId();
		Thread thread  = new Thread("Test Application"){
			@Override 
			public void run(){
				try{
					Main.main(Constants.XMPP_HOSTNAME, Constants.SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		
		driver = new AuctionSniperDriver(timeToWait);
		driver.showsSniperStatus(SniperStatus.JOINING);
		
	}

	public void showsSniperHasLostAuction()
	{
		driver.showsSniperStatus(SniperStatus.LOST);
	}

	//TODO delete
	public void hasShownSniperIsBidding()
	{
		driver.showsSniperStatus(SniperStatus.BIDDING);
	}
	
	//TODO delete
	public void hasShownSniperIsWinnig()
	{
		driver.showsSniperStatus(SniperStatus.WINNING);
	}
	
	//TODO delete
	public void showsSniperHasWonAuction()
	{
		driver.showsSniperStatus(SniperStatus.WON);
	}
	
	public void stop()
	{
		driver.dispose();
	}

	public void hasShownSniperIsBidding(int price, int bid)
	{
		driver.showsSniperStatus(itemId,price,bid,SniperStatus.BIDDING);
	}

	public void hasShownSniperIsWinnig(int winningPrice)
	{
		driver.showsSniperStatus(itemId,winningPrice,winningPrice,SniperStatus.BIDDING);
	}

	public void showsSniperHasWonAuction(int price)
	{
		driver.showsSniperStatus(itemId,price,price,SniperStatus.BIDDING);
	}

}
