package lab09;

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
	
	public UserToChatQueueWorker(MessageSink<Message> sink, Socket socket, Category category) throws IOException, IllegalArgumentException {
		this.sink = sink;
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.category = category;
		
		id = reader.readLine();
	}
	
	public String getId() {
		return id;
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

		}
		
	    sink.put(new Message(id, "Has left the chat room"));
	    
	    try {
	    	reader.close();
	    }
	    catch (IOException e) {
	    }
	    
	    try {
	    	socket.close();
	    }
	    catch (IOException e) {
	    	
	    }
	    
	    System.out.println(getId() + " has just left " + category);
	}
}
