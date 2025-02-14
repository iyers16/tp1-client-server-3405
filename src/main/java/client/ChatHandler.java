package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatHandler {
	private DataInputStream in;
	private DataOutputStream out;
	private Scanner scanner;

	public ChatHandler(DataInputStream in, DataOutputStream out, Scanner scanner) {
		this.in = in;
		this.out = out;
		this.scanner = scanner;
	}

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

	public void activateSender() throws IOException {
		while (true) {
			String message = this.scanner.nextLine();
			if (message.equals("q")) {
				System.out.println("You disconnected from the server");
				break;
			}
			this.out.writeUTF(message);
			this.out.flush();
		}
	}
}