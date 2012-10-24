package ua.com.asakharuta.end_to_end;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;


public class SingleMessageListener implements MessageListener
{

	private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);
	private final long timeToWaitInSeconds = 5;

	@Override
	public void processMessage(Chat arg0, Message message)
	{
		messages .add(message);
	}

	public void receivesAMessage(Matcher<? super String> messageMatcher) throws InterruptedException{
		Message message = messages.poll(timeToWaitInSeconds , TimeUnit.SECONDS);
		assertThat("Message",message, is(notNullValue()));
		assertThat(message.getBody(), messageMatcher);
	}
}
