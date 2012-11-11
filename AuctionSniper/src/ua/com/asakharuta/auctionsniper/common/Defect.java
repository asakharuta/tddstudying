package ua.com.asakharuta.auctionsniper.common;

public class Defect extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -24280204106348014L;

	public Defect(String message)
	{
		super(message);
	}
}
