package ua.com.asakharuta.auctionsniper;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ua.com.asakharuta.auctionsniper.common.SniperState;

public class SniperSnapshot {
  public final String itemId;
  public final int lastPrice;
  public final int lastBid;
  public final SniperState state;
  
  public SniperSnapshot(String itemId, int lastPrice, int lastBid, SniperState state) {
    this.itemId = itemId;
    this.lastPrice = lastPrice;
    this.lastBid = lastBid;
    this.state = state;
  }
  
  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }
  
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
 
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public static SniperSnapshot joining(String itemId)
  {
	return new SniperSnapshot(itemId, 0,0, SniperState.JOINING);
  }

  public SniperSnapshot winning(int newLastPrice)
  {
	return new SniperSnapshot(itemId, newLastPrice, lastBid, SniperState.WINNING);
  }

  public SniperSnapshot bidding(int newLastPrice, int newLastBid)
  {
	 return new SniperSnapshot(itemId, newLastPrice, newLastBid, SniperState.BIDDING);
  }

  public SniperSnapshot closed()
  {
	return new SniperSnapshot(itemId, lastPrice, lastBid, state.whenAuctionClosed());
  }

  public boolean isForSameItemAs(SniperSnapshot sniperSnapshot)
  {
	return this.itemId.equals(sniperSnapshot.itemId);
  }

public SniperSnapshot losing(int currentPrice)
{
	return new SniperSnapshot(itemId, currentPrice, lastBid, SniperState.LOSING);
}
}
