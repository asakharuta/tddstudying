package ua.com.asakharuta.auctionsniper.ui;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import ua.com.asakharuta.auctionsniper.SniperListener;
import ua.com.asakharuta.auctionsniper.SniperSnapshot;

public class SwingThreadSniperListener implements SniperListener
{
	private final SnipersTableModel snipers;

	public SwingThreadSniperListener(SnipersTableModel snipers)
	{
		this.snipers = snipers;
	}

	@Override
	public void sniperStateChanged(final SniperSnapshot sniperSnapshot)
	{
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				
				@Override
				public void run()
				{
					snipers.sniperStateChanged(sniperSnapshot);
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
