package ua.com.asakharuta.auctionsniper;

import java.util.EventListener;

public interface UserRequestListener extends EventListener
{
	public void joinAuction(String itemId);
}
