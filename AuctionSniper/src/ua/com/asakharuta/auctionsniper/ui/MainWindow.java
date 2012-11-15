package ua.com.asakharuta.auctionsniper.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import ua.com.asakharuta.auctionsniper.SniperSnapshot;
import ua.com.asakharuta.auctionsniper.UserRequestListener;
import ua.com.asakharuta.auctionsniper.common.Announcer;
import ua.com.asakharuta.auctionsniper.common.Constants;

public class MainWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8323397588084337061L;
	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_STATUS_NAME = "Sniper Status";
	public static final String TITLE = "Auction Sniper";
	private static final String SNIPER_TABLE_NAME = "Auction Sniper Table";

	private final SnipersTableModel snipers;
	protected Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);
	
	public MainWindow(SnipersTableModel snipers){
		super(TITLE);
		setName(MAIN_WINDOW_NAME);
		
		this.snipers = snipers;
		
		fillContentPane(makeSnipersTable(),makeControls());
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private JPanel makeControls()
	{
		JPanel constrols = new JPanel(new FlowLayout());
		final JTextField itemIdField = new JTextField();
		itemIdField.setColumns(25);
		itemIdField.setName(Constants.NEW_ITEM_ID_NAME);
		constrols.add(itemIdField);
		
		JButton joinAuctionButton = new JButton("Join auction");
		joinAuctionButton.setName(Constants.JOIN_BUTTON_NAME);
		joinAuctionButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				userRequests.announce().joinAuction(itemIdField.getText());
			}
		});
		constrols.add(joinAuctionButton);
		
		return constrols;
	}

	private void fillContentPane(JTable snipersTable, JPanel controlPanel)
	{
		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(controlPanel, BorderLayout.NORTH);
		contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
	}

	private JTable makeSnipersTable()
	{
		final JTable snipersTable = new JTable(snipers);
		snipersTable.setName(SNIPER_TABLE_NAME);
		return snipersTable;
	}

	public void sniperStateChanged(SniperSnapshot sniperSnapshot)
	{
		snipers.sniperStateChanged(sniperSnapshot);
	}

	public void addUserRequestListener(UserRequestListener userRequestListener)
	{
		userRequests.addListener(userRequestListener);
	}
}
