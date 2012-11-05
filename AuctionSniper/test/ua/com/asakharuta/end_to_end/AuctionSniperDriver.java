package ua.com.asakharuta.end_to_end;

import static org.hamcrest.CoreMatchers.equalTo;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import ua.com.asakharuta.auctionsniper.common.SniperState;
import ua.com.asakharuta.auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
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

	//TODO delete
	@SuppressWarnings("unchecked")
	public void showsSniperStatus(SniperState sniperStatus)
	{
		new JTableDriver(this).hasCell(withLabelText(equalTo(sniperStatus.getStatusText())));
	}

	@SuppressWarnings("unchecked")
	public void showsSniperStatus(String itemId, int price, int bid,
			SniperState sniperStatus)
	{
		JTableDriver table = new JTableDriver(this);
		table.hasRow(matching(
				withLabelText(itemId),
				withLabelText(String.valueOf(price)),
				withLabelText(String.valueOf(bid)),
				withLabelText(sniperStatus.getStatusText())
				));
	}

}
