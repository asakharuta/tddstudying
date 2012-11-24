package ua.com.asakharuta.auctionsniper.ui;

import ua.com.asakharuta.auctionsniper.SniperSnapshot;

public enum Column{
	ITEM_IDENTIFIER(0, "Item") {
		@Override
		public Object valueIn(SniperSnapshot snapshot)
		{
			return snapshot.itemId;
		}
	},
	LAST_PRICE(1,"Last Price") {
		@Override
		public Object valueIn(SniperSnapshot snapshot)
		{
			return snapshot.lastPrice;
		}
	},
	LAST_BID(2, "Last Bid") {
		@Override
		public Object valueIn(SniperSnapshot snapshot)
		{
			return snapshot.lastBid;
		}
	},
	SNIPER_STATE(3, "State") {
		@Override
		public Object valueIn(SniperSnapshot snapshot)
		{
			return SnipersTableModel.textFor(snapshot.state);
		}
	}, 
	;
	
	public final int index;
	public final String name;
	
	private Column(int position, String name){
		this.index = position;
		this.name = name;
	}
	
	public static Column at(int position){
		for (Column column : values())
		{
			if (column.index == position)
			{
				return column;
			}
		}
		throw new IllegalArgumentException("No column at " + position);
	}

	abstract public Object valueIn(SniperSnapshot snapshot);
}
