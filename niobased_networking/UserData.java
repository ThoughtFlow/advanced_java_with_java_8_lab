package niobased_networking;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class UserData {

	private final SocketChannel channel;
	private final SelectionKey selectionKey;
	private final List<String> messages = new LinkedList<>();
	
	private String userId;
	private String chatRoom;

	public UserData(SocketChannel channel, SelectionKey selectionKey) {
		this.channel = channel;
		this.selectionKey = selectionKey;
	}

	public boolean isUserIdAndTopicSet() {
		return userId != null && chatRoom != null;
	}
	
	public void setUserIdAndTopic(String userIdAndTopic) {
		StringTokenizer tokenizer = new StringTokenizer(userIdAndTopic);
		
		try {
		   userId = tokenizer.nextToken(" ");
		   chatRoom = tokenizer.nextToken();
		   chatRoom = chatRoom.toLowerCase();
		}
		catch (NoSuchElementException ex) {
			// userId and/or topic will remain unset
		}
	}
	
	public SocketChannel getChannel() {
		return channel;
	}

	public String getUserId() {
		return userId;
	}
	
	public String getChatRoom() {
		return chatRoom;
	}

	public SelectionKey getSelectionKey() {
		return selectionKey;
	}
	
	public void addMessage(String message) {
		messages.add(message);
	}
	
	public String getNextMessage() {
		return messages.size() > 0 ? messages.remove(0) : null; 
	}
}
