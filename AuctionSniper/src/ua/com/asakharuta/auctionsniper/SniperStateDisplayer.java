package ua.com.asakharuta.auctionsniper;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import ua.com.asakharuta.auctionsniper.common.SniperStatus;
import ua.com.asakharuta.auctionsniper.ui.MainWindow;

public class SniperStateDisplayer implements SniperListener
{
	private final MainWindow mainWindow;

	public SniperStateDisplayer(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
	}

	@Override
	public void sniperLost()
	{
		showStatus(SniperStatus.LOST);
	}

	@Override
	public void sniperWinning()
	{
		showStatus(SniperStatus.WINNING);
	}

	@Override
	public void sniperWon()
	{
		showStatus(SniperStatus.WON);
	}

	//TODO delete
	private void showStatus(final SniperStatus status)
	{
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				
				@Override
				public void run()
				{
					mainWindow.showStatus(status);
				}
			});
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void sniperBidding(SniperSnapshot sniperSnapshot)
	{
		sniperStatusChanged(sniperSnapshot);
	}

	private void sniperStatusChanged(final SniperSnapshot sniperSnapshot)
	{
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				
				@Override
				public void run()
				{
					mainWindow.sniperStatusChanged(sniperSnapshot);
				}
			});
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
	}
}
