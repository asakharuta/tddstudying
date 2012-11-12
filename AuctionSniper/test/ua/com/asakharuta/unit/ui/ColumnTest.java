package ua.com.asakharuta.unit.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import ua.com.asakharuta.auctionsniper.SniperSnapshot;
import ua.com.asakharuta.auctionsniper.common.SniperState;
import ua.com.asakharuta.auctionsniper.ui.SnipersTableModel.Column;

public class ColumnTest
{
	@Test
	public void
	retrievesValuesFromASniperSnapshot() {
		String itemId = "item_id";
		int lastPrice = 100;
		int lastBid = 23;
		SniperState state = SniperState.BIDDING;
		SniperSnapshot snapshot = new SniperSnapshot(itemId, lastPrice, lastBid, state);
		
		assertEquals(itemId, Column.ITEM_IDENTIFIER.valueIn(snapshot));
	    assertEquals(lastPrice, Column.LAST_PRICE.valueIn(snapshot));
	    assertEquals(lastBid, Column.LAST_BID.valueIn(snapshot));
	    assertEquals(state.getStatusText(), Column.SNIPER_STATE.valueIn(snapshot));
	}

}
