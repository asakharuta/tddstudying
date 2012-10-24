package ua.com.asakharuta.auctionsniper.common;

public enum SniperStatus
{
	JOINING("Joining"), 
	LOST("Lost"), 
	BIDDING("Bidding")
	;

	private String text;

	private SniperStatus(String text){
		this.text = text;
	}
	
	public String getStatusText()
	{
		return text;
	}

}
