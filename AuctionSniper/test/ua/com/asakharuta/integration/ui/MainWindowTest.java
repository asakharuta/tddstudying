package ua.com.asakharuta.integration.ui;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import ua.com.asakharuta.auctionsniper.UserRequestListener;
import ua.com.asakharuta.auctionsniper.ui.MainWindow;
import ua.com.asakharuta.auctionsniper.ui.SnipersTableModel;
import ua.com.asakharuta.end_to_end.AuctionSniperDriver;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

public class MainWindowTest
{

	private final SnipersTableModel tableModel = new SnipersTableModel();
	private final MainWindow mainWindow = new MainWindow(tableModel);
	private final AuctionSniperDriver driver = new AuctionSniperDriver(100);

	@Test
	public void makeUserRequestWhenJoinButtonClicked()
	{
		final String itemId = "an item-id";
		final ValueMatcherProbe<String> buttonProbe = new ValueMatcherProbe<String>(equalTo(itemId), "join request");
		
		mainWindow.addUserRequestListener(
			new UserRequestListener(){

				@Override
				public void joinAuction(String itemId)
				{
					buttonProbe.setReceivedValue(itemId);
				}
				
			}
		);
		
		driver.startBiddingFor(itemId);
		driver.check(buttonProbe);
	}

}
