package ua.com.asakharuta.end_to_end;

import ua.com.asakharuta.auctionsniper.Main;
import ua.com.asakharuta.auctionsniper.SniperStatus;

public class ApplicationRunner
{
	private static final String SNIPER_ID = "sniper";
	private static final String SNIPER_PASSWORD = "sniper";
	private static final int timeToWait = 1000;
	private AuctionSniperDriver driver = new NullDriver(timeToWait);

	public void startBiddingIn(final FakeAuctionServer auction)
	{
		Thread thread  = new Thread("Test Application"){
			@Override 
			public void run(){
				try{
					Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
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

	public void stop()
	{
		driver.dispose();
	}

}
