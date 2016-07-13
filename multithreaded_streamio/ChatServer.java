package multithreaded_streamio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ChatServer implements Closeable {

	private static final String CATEGORIES;
	
	private final ServerSocket serverSocket;
	private final Map<Category, MessageBroadcastWorker> broadcasters = new HashMap<>();
	private final ExecutorService threadPool = Executors.newCachedThreadPool();
	
	static {
		StringBuilder builder = new StringBuilder();
		
		for (Category category : Category.values()) {
			builder.append(category).append(", ");
		}
		
		CATEGORIES = builder.substring(0, builder.length() - 2);
	}
	

	public ChatServer() throws IOException {
		serverSocket = new ServerSocket(8080);
	}

	private String prompt(String promptString, BufferedReader reader, BufferedWriter writer) throws IOException {
		writer.write(promptString);
		writer.flush();
	    return reader.readLine();
	}
	
	public void acceptNewConnections() throws IOException {

		System.out.println("Chat forum is open");
		String stringedCategory;
		String userName;
		
		do  {
			Socket newSocket = serverSocket.accept();

			BufferedReader reader = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(newSocket.getOutputStream())); 
			
			userName = prompt("User name: ", reader, writer);

			
			if (!userName.equals("SHUTDOWN")) {
				stringedCategory = prompt("Category (" + CATEGORIES + "): ", reader, writer);
				Category category = Category.toCategory(stringedCategory);

				MessageBroadcastWorker broadcaster = broadcasters.get(category);
				if (broadcaster == null) {
					broadcaster = new MessageBroadcastWorker();
					broadcasters.put(category, broadcaster);
					threadPool.execute(broadcaster);
				}

			    UserToChatQueueWorker userToChatQueueWorker = new UserToChatQueueWorker(userName, broadcaster, newSocket, category);
				ChatQueueToUserWorker chatQueueToUserWorker = new ChatQueueToUserWorker(userName, broadcaster, newSocket);
				userToChatQueueWorker.setLastMessageSink(chatQueueToUserWorker);

				threadPool.execute(userToChatQueueWorker);
				threadPool.execute(chatQueueToUserWorker);
				chatQueueToUserWorker.put(new Message("", "Welcome to " + category + ", " + userName));
				chatQueueToUserWorker.put(new Message("", "There are " + broadcaster.getParticipantCount() + " participants in this room"));
				System.out.println(userName + " has just joined " + category);
			}
		} while (userName.equals("SHUTDOWN") == false);
	}

	public void close()  {
		
		try {
		   serverSocket.close();
		}
		catch (IOException e) {
			System.err.println("An error occurred");
			e.printStackTrace();
		}
		
		try {
			threadPool.shutdownNow();
		    threadPool.awaitTermination(30, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			System.err.println("An error occurred");
			e.printStackTrace();
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
