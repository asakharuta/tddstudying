package ua.com.asakharuta.auctionsniper.xmpp;

import static ua.com.asakharuta.auctionsniper.common.Constants.AUCTION_RESOURCE;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import ua.com.asakharuta.auctionsniper.Auction;
import ua.com.asakharuta.auctionsniper.common.Constants;

public class XMPPAuctionHouse implements AuctionHouse
{

	private XMPPConnection connection;

	public XMPPAuctionHouse(XMPPConnection connection)
	{
		this.connection = connection;
	}

	@Override
	public Auction auctionFor(String itemId)
	{
		return new XMPPAuction(connection, auctionId(itemId, connection));
	}

	private String auctionId(String itemId, XMPPConnection connection2)
	{
		return String.format(Constants.AUCTION_ID_FORMAT, itemId, connection.getServiceName());
	}

	public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPException
	{
		XMPPConnection connection = new XMPPConnection(hostname);
		try
		{
			connection.connect(); 
			connection.login(username, password, AUCTION_RESOURCE); 
			return new XMPPAuctionHouse(connection);
		} catch (XMPPException e)
		{
			throw new XMPPException();// TODO: handle exception
		}
		
	}

	public void disconnect()
	{
		connection.disconnect();
	}

}
