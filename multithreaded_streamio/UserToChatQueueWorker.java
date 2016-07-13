package multithreaded_streamio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class UserToChatQueueWorker implements Runnable {

	private final MessageSink<Message> sink;
	private final BufferedReader reader;
	private final String id;
	private final Socket socket; 
	private final Category category;
	
	private MessageSink<Message> lastMessageSink;
	
	public UserToChatQueueWorker(String id, MessageSink<Message> sink, Socket socket, Category category) throws IOException, IllegalArgumentException {
		this.id = id;
		this.sink = sink;
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.category = category;
	}
	
	@Override
	public void run() {
		String nextMessage;
		
		try {
		   while ((nextMessage = reader.readLine()) != null) {
			   sink.put(new Message(id, nextMessage));
		   }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	    sink.put(new Message(id, "Has left the chat room"));
	    lastMessageSink.put(Message.makeFinaleMessage(id));
	    
	    try {
	    	reader.close();
	    }
	    catch (IOException e) {
			e.printStackTrace();
	    }
	    
	    try {
	    	socket.close();
	    }
	    catch (IOException e) {
			e.printStackTrace();
	    }
	    
	    System.out.println(id + " has just left " + category);
	}
	
	public void setLastMessageSink(MessageSink<Message> lastMessageSink) {
		this.lastMessageSink = lastMessageSink;
	}
}
