package ua.com.asakharuta.auctionsniper.ui;

import javax.swing.table.AbstractTableModel;

import ua.com.asakharuta.auctionsniper.common.SniperStatus;

public class SnipersTableModel extends AbstractTableModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8020703731512421093L;
	private SniperStatus sniperStatus= SniperStatus.JOINING;

	@Override
	public int getColumnCount()
	{
		return 1;
	}

	@Override
	public int getRowCount()
	{
		return 1;
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		return sniperStatus.getStatusText();
	}

	public void setStatusText(SniperStatus sniperStatus)
	{
		this.sniperStatus = sniperStatus;
		fireTableDataChanged();
	}

}
