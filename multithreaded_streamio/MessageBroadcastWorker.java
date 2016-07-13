package multithreaded_streamio;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageBroadcastWorker implements Runnable, MessageSink<Message> {

	private final BlockingQueue<Message> sourceQueue;
	private final List<MessageSink<Message>> messageSinks;
	
	public MessageBroadcastWorker() {
		sourceQueue = new LinkedBlockingQueue<>(); 
		messageSinks = new LinkedList<>();
	}


	@Override
	public void run() {
		Message nextMessage;
		
		try {
		   while ((nextMessage = sourceQueue.take()) != null) {
			   for (MessageSink<Message> nextSink : messageSinks) {
				   nextSink.put(nextMessage);
			   }
		   }
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	    System.out.println(this.getClass().getName() + " Done");
	}

	@Override
	public void put(Message message) {
		sourceQueue.add(message);
	}
	
	public void addMessageSink(MessageSink<Message> messageSink)
	{
		messageSinks.add(messageSink);
	}
	
	public int getParticipantCount() {
		return messageSinks.size();
	}
	
	public void removeMessageSink(MessageSink<Message> messageSink) {
		messageSinks.remove(messageSink);
	}
}
