package ua.com.asakharuta.auctionsniper.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import ua.com.asakharuta.auctionsniper.Item;
import ua.com.asakharuta.auctionsniper.SniperPortfolio;
import ua.com.asakharuta.auctionsniper.UserRequestListener;
import ua.com.asakharuta.auctionsniper.common.Announcer;

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
	 public static final String NEW_ITEM_ID_NAME = "item id";
	  public static final String NEW_ITEM_STOP_PRICE_NAME = "stop price";
	  public static final String JOIN_BUTTON_NAME = "JOIN";

	protected final Announcer<UserRequestListener> userRequests = Announcer.to(UserRequestListener.class);
	private final SniperPortfolio portfolio;
	
	public MainWindow(SniperPortfolio portfolio){
		super(TITLE);
		setName(MAIN_WINDOW_NAME);
		
		this.portfolio = portfolio;
		
		fillContentPane(makeSnipersTable(),makeControls());
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private JPanel makeControls()
	{
		final JTextField itemIdField = itemIdField();
	    final JFormattedTextField stopPriceField = stopPriceField();
		
	    JPanel constrols = new JPanel(new FlowLayout());
		constrols.add(itemIdField);
		constrols.add(stopPriceField);
		
		JButton joinAuctionButton = new JButton("Join auction");
		joinAuctionButton.setName(JOIN_BUTTON_NAME);
		joinAuctionButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				userRequests.announce().joinAuction(new Item(itemId(), stopPrice())); 
		      } 
		      private String itemId() {
		        return itemIdField.getText();
		      }
		      private int stopPrice() { 
		        return ((Number)stopPriceField.getValue()).intValue(); 
		      } 
		});
		constrols.add(joinAuctionButton);
		
		return constrols;
	}

	private JFormattedTextField stopPriceField()
	{
		final JFormattedTextField stopPriceField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		stopPriceField.setColumns(7);
		stopPriceField.setName(NEW_ITEM_STOP_PRICE_NAME);
		return stopPriceField;
	}

	private JTextField itemIdField()
	{
		final JTextField itemIdField = new JTextField();
		itemIdField.setColumns(25);
		itemIdField.setName(NEW_ITEM_ID_NAME);
		return itemIdField;
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
		SnipersTableModel model = new SnipersTableModel();
		portfolio.addPortfolioListener(model);
		final JTable snipersTable = new JTable(model);
		snipersTable.setName(SNIPER_TABLE_NAME);
		return snipersTable;
	}

	public void addUserRequestListener(UserRequestListener userRequestListener)
	{
		userRequests.addListener(userRequestListener);
	}
}
