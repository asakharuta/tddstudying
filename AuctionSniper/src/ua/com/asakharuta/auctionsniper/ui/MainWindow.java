package ua.com.asakharuta.auctionsniper.ui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ua.com.asakharuta.auctionsniper.SniperSnapshot;

public class MainWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8323397588084337061L;
	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_STATUS_NAME = "Sniper Status";
	private static final String title = "Auction Sniper";
	private static final String SNIPER_TABLE_NAME = "Auction Sniper Table";

	private final SnipersTableModel snipers = new SnipersTableModel();
	
	public MainWindow(){
		super(title);
		setName(MAIN_WINDOW_NAME);
		fillContentPane(makeSnipersTable());
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void fillContentPane(JTable snipersTable)
	{
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
	}

	private JTable makeSnipersTable()
	{
		final JTable snipersTable = new JTable(snipers);
		snipersTable.setName(SNIPER_TABLE_NAME);
		return snipersTable;
	}

	public void sniperStatusChanged(SniperSnapshot sniperSnapshot)
	{
		snipers.sniperStatusChanged(sniperSnapshot);
	}
}
