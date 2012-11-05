package ua.com.asakharuta.auctionsniper.common;

public enum SniperState
{
	JOINING("Joining"), 
	LOST("Lost"), 
	BIDDING("Bidding"), 
	WINNING("Winning"), 
	WON("Won"), 
	;

	private String text;

	private SniperState(String text){
		this.text = text;
	}
	
	public String getStatusText()
	{
		return text;
	}

}
