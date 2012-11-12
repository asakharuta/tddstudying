package ua.com.asakharuta.auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import ua.com.asakharuta.auctionsniper.SniperListener;
import ua.com.asakharuta.auctionsniper.SniperSnapshot;
import ua.com.asakharuta.auctionsniper.common.SniperState;

public class SnipersTableModel extends AbstractTableModel implements SniperListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8020703731512421093L;
	
	public enum Column{
		ITEM_IDENTIFIER(0, "Item") {
			@Override
			public Object valueIn(SniperSnapshot snapshot)
			{
				return snapshot.itemId;
			}
		},
		LAST_PRICE(1,"Last Price") {
			@Override
			public Object valueIn(SniperSnapshot snapshot)
			{
				return snapshot.lastPrice;
			}
		},
		LAST_BID(2, "Last Bid") {
			@Override
			public Object valueIn(SniperSnapshot snapshot)
			{
				return snapshot.lastBid;
			}
		},
		SNIPER_STATE(3, "State") {
			@Override
			public Object valueIn(SniperSnapshot snapshot)
			{
				return SnipersTableModel.textFor(snapshot.state);
			}
		}, 
		;
		
		public final int index;
		public final String name;
		
		private Column(int position, String name){
			this.index = position;
			this.name = name;
		}
		
		public static Column at(int position){
			for (Column column : values())
			{
				if (column.index == position)
				{
					return column;
				}
			}
			throw new IllegalArgumentException("No column at " + position);
		}

		abstract public Object valueIn(SniperSnapshot snapshot);
	}
	
	private SniperSnapshot snapshot= new SniperSnapshot("", 0, 0, SniperState.JOINING);
	
	@Override
	public int getColumnCount()
	{
		return Column.values().length;
	}

	@Override
	public int getRowCount()
	{
		return 1;
	}

	@Override
	public Object getValueAt(int row, int column)
	{
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
		this.snapshot  = sniperSnapshot;
		fireTableRowsUpdated(0, 0);
	}
}
