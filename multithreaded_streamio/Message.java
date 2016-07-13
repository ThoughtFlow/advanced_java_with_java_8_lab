package multithreaded_streamio;

public class Message {

	private final String id;
	private final String message;
	
	public static Message makeFinaleMessage(String id) {
		return new Message(id, null);
	}
	
	public Message(String id, String message) {
		this.id = id;
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}
	
	public boolean isLastMessage() {
		return message == null;
	}
}
