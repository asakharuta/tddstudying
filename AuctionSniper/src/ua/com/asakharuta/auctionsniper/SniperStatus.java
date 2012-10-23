package ua.com.asakharuta.auctionsniper;

public enum SniperStatus
{
	JOINING(""), 
	LOST("")
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
