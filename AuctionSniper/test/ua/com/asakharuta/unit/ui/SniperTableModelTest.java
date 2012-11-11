package ua.com.asakharuta.unit.ui;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ua.com.asakharuta.auctionsniper.SniperSnapshot;
import ua.com.asakharuta.auctionsniper.common.SniperState;
import ua.com.asakharuta.auctionsniper.ui.SnipersTableModel;
import ua.com.asakharuta.auctionsniper.ui.SnipersTableModel.Column;

public class SniperTableModelTest
{

	private final Mockery context = new Mockery();
	private TableModelListener listener = context.mock(TableModelListener.class);
	private final SnipersTableModel model = new SnipersTableModel();
	
	@Before
	public void attachModelListener(){
		model.addTableModelListener(listener);
	}
	
	@After
	public void checkIfSatisfied(){
		context.assertIsSatisfied();
	}
	
	@Test
	public void hasEnoughColumns()
	{
		assertThat(model.getColumnCount(), equalTo(Column.values().length));
	}

	@Test
	public void setsSnipersValuesInColumns()
	{
		context.checking(new Expectations(){{
				one(listener).tableChanged(with(aRowChangedEvent()));
			}}
		);
		int lastPrice = 555;
		int lastBid = 666;
		String itemId = "item_id";
		SniperState sniperStatus = SniperState.BIDDING;
		model.sniperStatusChanged(new SniperSnapshot(itemId, lastPrice, lastBid, sniperStatus));
		
		assertColumnEquals(Column.ITEM_IDENTIFIER,itemId);
		assertColumnEquals(Column.LAST_PRICE,lastPrice);
		assertColumnEquals(Column.LAST_BID,lastBid);
		assertColumnEquals(Column.SNIPER_STATE,SniperState.BIDDING.getStatusText());
	}

	//TODO refactor to not use Object
	private void assertColumnEquals(Column column, Object expected)
	{
		final int rowIndex = 0;
		final int columnIndex = column.getPosition();
		assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
	}

	protected Matcher<TableModelEvent> aRowChangedEvent()
	{
		return samePropertyValuesAs(new TableModelEvent(model,0));
	}
}
