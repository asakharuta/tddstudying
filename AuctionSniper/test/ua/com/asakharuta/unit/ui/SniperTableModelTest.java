package ua.com.asakharuta.unit.ui;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ua.com.asakharuta.auctionsniper.SniperSnapshot;
import ua.com.asakharuta.auctionsniper.common.Defect;
import ua.com.asakharuta.auctionsniper.common.SniperState;
import ua.com.asakharuta.auctionsniper.ui.Column;
import ua.com.asakharuta.auctionsniper.ui.SnipersTableModel;

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
	public void setsUpColumnHeaders()
	{
		for (Column column : Column.values())
		{
			assertEquals(column.name, model.getColumnName(column.index));
		}
	}
	
	@Test
	public void setsSnipersValuesInColumns()
	{
		SniperSnapshot joining = SniperSnapshot.joining("item123");
		SniperSnapshot bidding = joining.bidding(111, 222);
		context.checking(new Expectations(){{
				allowing(listener).tableChanged(with(anyInsertionEvent()));
				one(listener).tableChanged(with(aChangeInRow(0)));
			}}
		);

		model.addSniperSnapshot(joining);
		model.sniperStateChanged(bidding);
	
		assertRowMatchesSnapshot(0, bidding);
	}
	@Test
	public void notifiesListenersWhenAddingASniper(){
		SniperSnapshot joining = SniperSnapshot.joining("item123");
		 context.checking(new Expectations() { {
		      one(listener).tableChanged(with(anInsertionAtRow(0)));
		    }});

		    assertEquals(0, model.getRowCount());
		    
		    model.addSniperSnapshot(joining);
		    
		    assertEquals(1, model.getRowCount());
		    assertRowMatchesSnapshot(0, joining);
		
	}
	
	@Test public void 
	  holdsSnipersInAdditionOrder() {
		SniperSnapshot joining1 = SniperSnapshot.joining("item123");
		SniperSnapshot joining2 = SniperSnapshot.joining("item234");
	    context.checking(new Expectations() { {
	      ignoring(listener);
	    }});
	    
	    model.addSniperSnapshot(joining1);
	    model.addSniperSnapshot(joining2);
	    
	    assertEquals("item123", cellValue(0, Column.ITEM_IDENTIFIER));
	    assertEquals("item234", cellValue(1, Column.ITEM_IDENTIFIER));
	  }
	  
	  @Test public void 
	  updatesCorrectRowForSniper() {
		 SniperSnapshot joining1 = SniperSnapshot.joining("item123");
		SniperSnapshot joining2 = SniperSnapshot.joining("item234");
	    context.checking(new Expectations() { {
	      allowing(listener).tableChanged(with(anyInsertionEvent()));

	      one(listener).tableChanged(with(aChangeInRow(1)));
	    }});
	    
	    model.addSniperSnapshot(joining1);
	    model.addSniperSnapshot(joining2);
	    

	    SniperSnapshot winning1 = joining2.winning(123);
	    model.sniperStateChanged(winning1);
	    
	    assertRowMatchesSnapshot(1, winning1);
	  }

	  @Test(expected=Defect.class) public void
	  throwsDefectIfNoExistingSniperForAnUpdate() {
	    model.sniperStateChanged(new SniperSnapshot("item 1", 123, 234, SniperState.WINNING));
	  }
	  
	protected Matcher<TableModelEvent> aRowChangedEvent()
	{
		return samePropertyValuesAs(new TableModelEvent(model,0));
	}
	
	private void assertRowMatchesSnapshot(int row, SniperSnapshot snapshot) {
	    assertEquals(snapshot.itemId, cellValue(row, Column.ITEM_IDENTIFIER));
	    assertEquals(snapshot.lastPrice, cellValue(row, Column.LAST_PRICE));
	    assertEquals(snapshot.lastBid, cellValue(row, Column.LAST_BID));
	    assertEquals(SnipersTableModel.textFor(snapshot.state), cellValue(row, Column.SNIPER_STATE));
	  }

	  private Object cellValue(int rowIndex, Column column) {
	    return model.getValueAt(rowIndex, column.ordinal());
	  }

	  Matcher<TableModelEvent> anyInsertionEvent() {
		    return hasProperty("type", equalTo(TableModelEvent.INSERT));
		  }
	  
	  Matcher<TableModelEvent> anInsertionAtRow(final int row) {
	    return samePropertyValuesAs(new TableModelEvent(model, row, row, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
	  }
	  
	  private Matcher<TableModelEvent> aChangeInRow(int row) { 
		    return samePropertyValuesAs(new TableModelEvent(model, row)); 
		  } 
}
