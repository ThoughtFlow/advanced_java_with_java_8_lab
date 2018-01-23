package niobased_networking;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChatRoomManager {

	private final Map<String, List<UserData>> chatRooms = new HashMap<>();
	
	public void addToChatRoom(String chatRoom, UserData userData) {
			List<UserData> list = chatRooms.computeIfAbsent(chatRoom, k -> new LinkedList<>());
			list.add(userData);
	}
    
	public List<UserData> getUserDataForChatRoom(String chatRoom) {
		return chatRooms.get(chatRoom);
	}		
	
	public void removeFromChatRoom(String chatRoom, UserData userData) {
		List<UserData> userDataList = getUserDataForChatRoom(chatRoom);
		
		if (userDataList != null) {
			userDataList.remove(userData);
		}
	}
	
	public int getParticipants(String chatRoom) {
		int participants = 0;
		List<UserData> list = getUserDataForChatRoom(chatRoom);
		
		if (list != null) {
			participants = list.size();
		}
		
		return participants;
	}
}
