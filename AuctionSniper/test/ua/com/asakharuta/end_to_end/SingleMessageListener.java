package ua.com.asakharuta.end_to_end;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.is;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.junit.Assert;

public class SingleMessageListener implements MessageListener
{

	private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);
	private final long timeToWaitInSeconds = 5;

	@Override
	public void processMessage(Chat arg0, Message message)
	{
		messages .add(message);
	}

	public void receivesAMessage() throws InterruptedException{
		Assert.assertThat("Message",messages.poll(timeToWaitInSeconds , TimeUnit.SECONDS), is(notNullValue()));
	}
}
