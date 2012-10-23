package ua.com.asakharuta.auctionsniper.ui;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import ua.com.asakharuta.auctionsniper.SniperStatus;

public class MainWindow extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8323397588084337061L;
	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_STATUS_NAME = "Sniper Status";
	private static final String title = "Auction Sniper";

	private final JLabel sniperStatusLabel = createLabel(SniperStatus.JOINING);

	public MainWindow(){
		super(title);
		setName(MAIN_WINDOW_NAME);
		add(sniperStatusLabel);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private JLabel createLabel(SniperStatus sniperStatus)
	{
		JLabel resultLabel = new JLabel(sniperStatus.getStatusText());
		resultLabel.setName(SNIPER_STATUS_NAME);
		resultLabel.setBorder(new LineBorder(Color.BLACK));
		return resultLabel;
	}
}
