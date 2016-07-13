package multithreaded_streamio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ChatQueueToUserWorker implements Runnable, MessageSink<Message> {

	private final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
	private final MessageBroadcastWorker broadcaster;
	private final BufferedWriter writer;
	private final String id;

	public ChatQueueToUserWorker(String id, MessageBroadcastWorker broadcaster, Socket socket) throws IOException {
		this.broadcaster = broadcaster;
	    writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		this.id = id;
		broadcaster.addMessageSink(this);
	}

	@Override
	public void run() {
		Message nextMessage;
		
		try {
		   while ((nextMessage = queue.take()) != null && !(nextMessage.isLastMessage() && nextMessage.getId().equals(id))) {
			   if (nextMessage.getId().equals(id) == false) {
				   String formattedSenderId = !nextMessage.getId().equals("") ? "[" + nextMessage.getId() + "] "  : "";
			      writer.write(formattedSenderId + nextMessage.getMessage());
			      writer.newLine();
			      writer.flush();
			   }
		   }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		broadcaster.removeMessageSink(this);
	    System.out.println(this.getClass().getName() + " Done");
	}

	@Override
	public void put(Message message) {
		queue.add(message);
	}
}
