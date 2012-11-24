package ua.com.asakharuta.auctionsniper.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ua.com.asakharuta.auctionsniper.AuctionSniper;
import ua.com.asakharuta.auctionsniper.SniperListener;
import ua.com.asakharuta.auctionsniper.SniperPortfolio.PortfolioListener;
import ua.com.asakharuta.auctionsniper.SniperSnapshot;
import ua.com.asakharuta.auctionsniper.common.Defect;
import ua.com.asakharuta.auctionsniper.common.SniperState;

public class SnipersTableModel extends AbstractTableModel implements SniperListener,PortfolioListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8020703731512421093L;
	
	private final List<SniperSnapshot> snapshots = new ArrayList<SniperSnapshot>();
	
	@Override
	public int getColumnCount()
	{
		return Column.values().length;
	}

	@Override
	public int getRowCount()
	{
		return snapshots.size();
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		SniperSnapshot snapshot = snapshots.get(row);
		return Column.at(column).valueIn(snapshot);
	}

	@Override
	public String getColumnName(int column)
	{
		return Column.at(column).name;
	}
	
	public static String textFor(SniperState state)
	{
		return state.getStatusText();
	}
	
	@Override
	public void sniperStateChanged(SniperSnapshot sniperSnapshot)
	{
		int row = rowMatching(sniperSnapshot);
		snapshots.set(row, sniperSnapshot);
		fireTableRowsUpdated(row,row);
	}

	private int rowMatching(SniperSnapshot sniperSnapshot)
	{
		for(int i = 0; i < getRowCount(); ++i){
			if(sniperSnapshot.isForSameItemAs(snapshots.get(i))){
				return i;
			}
		}
		throw new Defect("Cannot find match for " + sniperSnapshot);
	}

	public void addSniperSnapshot(SniperSnapshot snapshot)
	{
		snapshots.add(snapshot);
		int row = snapshots.size() - 1;
	    fireTableRowsInserted(row, row);
	}

	public void sniperAdded(AuctionSniper sniper) {
	    addSniperSnapshot(sniper.getSnapshot());
	    sniper.addSniperListener(new SwingThreadSniperListener(this));
	  } 
}
