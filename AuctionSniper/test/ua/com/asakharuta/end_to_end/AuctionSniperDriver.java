package ua.com.asakharuta.end_to_end;

import static org.hamcrest.CoreMatchers.equalTo;
import ua.com.asakharuta.auctionsniper.SniperStatus;
import ua.com.asakharuta.auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class AuctionSniperDriver extends JFrameDriver
{

	private static final long pollDelayMillis = 100;

	@SuppressWarnings("unchecked")
	public AuctionSniperDriver(int timeoutMillis)
	{
		super(new GesturePerformer(), 
			  JFrameDriver.topLevelFrame(
										named(MainWindow.MAIN_WINDOW_NAME),
										showingOnScreen()),
				new AWTEventQueueProber(timeoutMillis, pollDelayMillis));
	}

	@SuppressWarnings("unchecked")
	public void showsSniperStatus(SniperStatus sniperStatus)
	{
		new JLabelDriver(this, named(MainWindow.SNIPER_STATUS_NAME)).hasText(equalTo(sniperStatus.getStatusText()));
	}

}
