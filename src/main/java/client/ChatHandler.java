package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles client-side chat operations, including sending, receiving, and fetching history.
 */
public class ChatHandler {
	private DataInputStream in;
	private DataOutputStream out;
	private Scanner scanner;

	/**
	 * Constructor to initialize input/output streams and scanner.
	 * Establishes communication channels between client and server.
	 * @param in DataInputStream to read messages from server.
	 * @param out DataOutputStream to send messages to server.
	 * @param scanner Scanner to read user input from the console.
	 */
	public ChatHandler(DataInputStream in, DataOutputStream out, Scanner scanner) {
		this.in = in; // Input stream from server for receiving data
		this.out = out; // Output stream to server for sending data
		this.scanner = scanner; // Scanner to capture user input from the console
	}

	/**
	 * Fetches and displays the last 15 messages from the chat history sent by the server.
	 * Reads messages until it encounters the "END_OF_HISTORY" marker.
	 */
	public void fetchHistory() throws IOException {
		System.out.println("\n=== Chat History ==================================");

		List<String> historyMessages = new ArrayList<>();
		while (true) {
			String historyMessage = in.readUTF();
			if (historyMessage.equals("END_OF_HISTORY")) {
				break;
			}
			historyMessages.add(historyMessage);
		}

		int startIndex = Math.max(0, historyMessages.size() - 15);
		for (int i = startIndex; i < historyMessages.size(); i++) {
			System.out.println(historyMessages.get(i));
		}

		System.out.println("=====================================================\n");
		System.out.println("Write 'q' whenever you want to disconnect.\n");
	}

	/**
	 * Starts a listener thread to continuously receive messages from the server.
	 * Prints each received message to the console.
	 */
	public void activateListener() {
		Thread messageListener = new Thread(() -> {
			try {
				while (true) {
					System.out.print("> ");
					String serverMessage = this.in.readUTF();
					System.out.println("\n" + serverMessage);
				}
			} catch (IOException e) {
				System.out.println("Disconnected from server.");
			}
		});
		messageListener.start();
	}

	/**
	 * Continuously reads user input from the console and sends it to the server.
	 * Terminates when the user enters 'q' to quit.
	 */
	public void activateSender() throws IOException {
		while (true) {
			String message = this.scanner.nextLine();
			if (message.equals("q")) {
				System.out.println("You disconnected from the server");
				break;
			}
			if (message.length() > 200) {
				System.out.println("Message is to long. Message must be less than or equal to 200");
				continue;
			}
			this.out.writeUTF(message);
			this.out.flush();
		}
	}
}