package singlethreaded_streamio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ChatConnector implements Closeable {

	private static final String END_OF_CONVERSATION = "Bye";

	private final String myUserName;
	private final BufferedReader consoleReader;
	private final BufferedReader peerReader;
	private final BufferedWriter peerWriter;
	private final Socket clientSocket;
	private final String peerName;

	public ChatConnector(String host, int port, String userName) throws IOException {

		consoleReader = new BufferedReader(new InputStreamReader(System.in));

		clientSocket = new Socket(host, port);

		peerReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		peerWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
		this.myUserName = userName;

		sendMessage(myUserName);
		peerName = peerReader.readLine();

		sendMessage("Greetings");
	}

	public void chat() throws IOException {
		String peerLine;
		String myLine;

		System.out.println("Connected with " + peerName + " who is typing...");
		while ((peerLine = peerReader.readLine()) != null && !END_OF_CONVERSATION.equalsIgnoreCase(peerLine)) {
			System.out.println("[" + peerName + "] " + peerLine);

			System.out.print("[" + myUserName + "]: ");
			sendMessage(myLine = consoleReader.readLine());

			if (END_OF_CONVERSATION.equalsIgnoreCase(myLine)) {
				break;
			}
		}
	}

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

		try {
			clientSocket.close();
		} catch (IOException e) {
			lastException = e;
		}

		if (lastException != null) {
			throw lastException;
		}
	}

	private void sendMessage(String message) throws IOException {
		peerWriter.write(message);
		peerWriter.newLine();
		peerWriter.flush();
	}

	public static void main(String... args) {

		if (args.length == 3) {
			String host = args[0];
			Integer port = new Integer(args[1]);
			String userName = args[2];
			try (ChatConnector connector = new ChatConnector(host, port, userName)) {
				connector.chat();
			}
			catch (IOException exception) {
				exception.printStackTrace();
			}
		} else {
			System.err.println("Usage: Connector host port userName");
		}
	}
}
