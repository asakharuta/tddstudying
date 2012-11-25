package ua.com.asakharuta.end_to_end;

import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

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

	public void startBiddingFor(String itemId,int stopPrice)
	{
		itemIdField(MainWindow.NEW_ITEM_ID_NAME).replaceAllText(itemId);
		itemIdField(MainWindow.NEW_ITEM_STOP_PRICE_NAME).replaceAllText(String.valueOf(stopPrice));
		bidButton().click();
	}

	@SuppressWarnings("unchecked")
	private JButtonDriver bidButton()
	{
		return new JButtonDriver(this, JButton.class,named(MainWindow.JOIN_BUTTON_NAME));
	}

	@SuppressWarnings("unchecked")
	private JTextFieldDriver itemIdField(String textFieldName)
	{
		JTextFieldDriver textField = new JTextFieldDriver(this, JTextField.class, named(textFieldName));
		textField.focusWithMouse();
		return textField;
	}

}
