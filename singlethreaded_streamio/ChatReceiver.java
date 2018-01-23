package singlethreaded_streamio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatReceiver implements Closeable{

	private static final String END_OF_CONVERSATION = "Bye";
	
	private final String myUserName;
	private final BufferedReader consoleReader;
	
	private BufferedReader peerReader;
	private BufferedWriter peerWriter;
	private String peerName;

	public ChatReceiver(String userName) throws IOException {
		consoleReader = new BufferedReader(new InputStreamReader(System.in));
		this.myUserName = userName;
	}
	
	public void establishConnection() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(8080)) {

			System.out.println("Waiting for connection on 8080");
			Socket socket = serverSocket.accept();

			peerReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			peerName = peerReader.readLine();

		    peerWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		    System.out.println("Connected with " + peerName);
		    sendMessage(myUserName);
		}
	}

	public void chat() throws IOException {
		String peerLine;
		String myLine = "";
		
		while ((peerLine = peerReader.readLine()) != null && !END_OF_CONVERSATION.equalsIgnoreCase(peerLine)) {
			System.out.println("[" + peerName + "] " + peerLine);

			System.out.print("[" + myUserName + "]: ");
			sendMessage(myLine = consoleReader.readLine());
			
			if (END_OF_CONVERSATION.equalsIgnoreCase(myLine))
			{
				break;
			}
		}
		
		System.out.println("End of chat");
	}

	@Override
	public void close() throws IOException {

		IOException lastException = null;
		
		try {
			peerReader.close();
		} catch (IOException e) {
			lastException = e;
		}

		try {
			peerWriter.close();
		} catch (IOException e) {
			lastException = e;
		}
		
		try {
			consoleReader.close();
		} catch (IOException e) {
			lastException = e;
		}
		
		if (lastException != null) {
			throw lastException;
		}
	}

	private void sendMessage(String message) throws IOException
	{
		peerWriter.write(message);
		peerWriter.newLine();
		peerWriter.flush();
	}
	
	public static void main(String... args) {
		
		if (args.length == 1) {
		   String userName = args[0];
			
		   try (ChatReceiver receiver = new ChatReceiver(userName)) {

		      receiver.establishConnection();
		      receiver.chat();
		   }
		   catch (IOException exception) {
			   exception.printStackTrace();
		   }
		}
		else
		{
			System.err.println("Usage: Receiver userName");
		}
	}
}
