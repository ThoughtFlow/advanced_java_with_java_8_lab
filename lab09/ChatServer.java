package lab09;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatServer implements Closeable {

	private final ServerSocket serverSocket;
	private final Map<Category, MessageBroadcastWorker> broadcasters = new HashMap<>();
	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	public ChatServer() throws IOException {
		serverSocket = new ServerSocket(8080);
	}

	public void acceptNewConnections() throws IOException {

		System.out.println("Chat forum is open");
		String stringedCategory;
		
		do  {
			Socket newSocket = serverSocket.accept();

			BufferedReader reader = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));

		    stringedCategory = reader.readLine();
			
			if (!stringedCategory.equals("SHUTDOWN")) {
				Category category = Category.toCategory(stringedCategory);

				MessageBroadcastWorker broadcaster = broadcasters.get(category);
				if (broadcaster == null) {
					broadcaster = new MessageBroadcastWorker();
					broadcasters.put(category, broadcaster);
					threadPool.execute(broadcaster);
				}

			    UserToChatQueueWorker userToChatQueueWorker = new UserToChatQueueWorker(broadcaster, newSocket, category);
				ChatQueueToUserWorker chatQueueToUserWorker = new ChatQueueToUserWorker(broadcaster, newSocket, userToChatQueueWorker.getId());
				userToChatQueueWorker.setLastMessageSink(chatQueueToUserWorker);
				

				threadPool.execute(userToChatQueueWorker);
				threadPool.execute(chatQueueToUserWorker);
				chatQueueToUserWorker.put(new Message("", "Welcome to " + category + ", " + userToChatQueueWorker.getId()));
				chatQueueToUserWorker.put(new Message("", "There are " + broadcaster.getParticipantCount() + " participants in this room"));
				System.out.println(userToChatQueueWorker.getId() + " has just joined " + category);
			}
		} while (stringedCategory.equals("SHUTDOWN") == false);
	}

	public void close()  {
		
		try {
		   serverSocket.close();
		}
		catch (IOException e) {
			
		}
		
		try {
			threadPool.shutdownNow();
		   threadPool.awaitTermination(30, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			
		}

	}
	
	public static void main(String... args) {
		try (ChatServer server = new ChatServer()) {
			server.acceptNewConnections();
		} 
		catch (IOException e) {
			System.err.println("An error occurred");
			e.printStackTrace();
		}
	}
}
