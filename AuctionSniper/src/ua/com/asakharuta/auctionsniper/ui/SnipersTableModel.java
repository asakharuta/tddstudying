package ua.com.asakharuta.auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import ua.com.asakharuta.auctionsniper.SniperSnapshot;
import ua.com.asakharuta.auctionsniper.common.SniperState;

public class SnipersTableModel extends AbstractTableModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8020703731512421093L;
	
	public enum Column{
		ITEM_IDENTIFIER(0),
		LAST_PRICE(1),
		LAST_BID(2),
		SNIPER_STATE(3), 
		;
		
		private final int position;

		private Column(int position){
			this.position = position;
		}
		
		public static Column at(int position){
			for (Column column : values())
			{
				if (column.position == position)
				{
					return column;
				}
			}
			throw new IllegalArgumentException("No column at " + position);
		}

		public int getPosition()
		{
			return position;
		}
	}
	
	private SniperSnapshot sniperStatus= new SniperSnapshot("", 0, 0, SniperState.JOINING);
	private SniperState state = sniperStatus.state;
	
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
		switch (Column.at(column))
		{
		case ITEM_IDENTIFIER:
			return sniperStatus.itemId;
		case LAST_PRICE:
			return sniperStatus.lastPrice;
		case LAST_BID:
			return sniperStatus.lastBid;
		case SNIPER_STATE:
			return state.getStatusText();

		default:
			throw new IllegalArgumentException("No column at " + column);
		}
	}

	public void sniperStatusChanged(SniperSnapshot sniperSnapshot)
	{
		this.sniperStatus  = sniperSnapshot;
		state = sniperStatus.state;
		fireTableRowsUpdated(0, 0);
	}

}
