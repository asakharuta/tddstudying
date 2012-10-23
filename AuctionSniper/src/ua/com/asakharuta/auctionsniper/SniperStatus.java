package ua.com.asakharuta.auctionsniper;

public enum SniperStatus
{
	JOINING("Joining"), 
	LOST("Lost")
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
