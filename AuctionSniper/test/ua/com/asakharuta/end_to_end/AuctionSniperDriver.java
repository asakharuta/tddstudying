package ua.com.asakharuta.end_to_end;

import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

import ua.com.asakharuta.auctionsniper.common.Constants;
import ua.com.asakharuta.auctionsniper.common.SniperState;
import ua.com.asakharuta.auctionsniper.ui.MainWindow;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.driver.JTextFieldDriver;
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
	public void showsSniperStatus(String itemId, int price, int bid,
			String sniperStatus)
	{
		JTableDriver table = new JTableDriver(this);
		table.hasRow(matching(
				withLabelText(itemId),
				withLabelText(String.valueOf(price)),
				withLabelText(String.valueOf(bid)),
				withLabelText(sniperStatus)
				));
	}

	@SuppressWarnings("unchecked")
	public void hasColumnTitles()
	{
		JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
		headers.hasHeaders(matching(
				withLabelText("Item"),
				withLabelText("Last Price"),
				withLabelText("Last Bid"),
				withLabelText("State")
				));
	}

	public void startBiddingFor(String itemId)
	{
		itemIdField().replaceAllText(itemId);
		bidButton().click();
	}

	@SuppressWarnings("unchecked")
	private JButtonDriver bidButton()
	{
		return new JButtonDriver(this, JButton.class,named(Constants.JOIN_BUTTON_NAME));
	}

	@SuppressWarnings("unchecked")
	private JTextFieldDriver itemIdField()
	{
		JTextFieldDriver newItemId = new JTextFieldDriver(this, JTextField.class, named(Constants.NEW_ITEM_ID_NAME));
		newItemId.focusWithMouse();
		return newItemId;
	}

}
