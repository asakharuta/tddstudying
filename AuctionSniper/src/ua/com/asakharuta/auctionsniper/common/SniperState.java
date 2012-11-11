package ua.com.asakharuta.auctionsniper.common;

public enum SniperState
{
	JOINING("Joining"){
		@Override
		public SniperState whenAuctionClosed(){
			return LOST;
		}
	},
	LOST("Lost"), 
	BIDDING("Bidding"){
		@Override
		public SniperState whenAuctionClosed(){
			return LOST;
		}
	}, 
	WINNING("Winning"){
		@Override
		public SniperState whenAuctionClosed(){
			return WON;
		}
	}, 
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

	public SniperState whenAuctionClosed()
	{
		throw new Defect("Auction is closed!");
	}

}
