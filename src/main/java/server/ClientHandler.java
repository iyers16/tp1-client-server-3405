package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * ClientHandler is responsible for managing a single client connection within a multi-threaded server environment.
 * It handles the complete lifecycle of a client session, including:
 * - Authenticating the client through AuthService.
 * - Sending and receiving messages via MessageService.
 * - Retrieving and delivering chat history from HistoryService.
 * This class extends Thread to enable concurrent handling of multiple clients simultaneously.
 */
public class ClientHandler extends Thread {
	private Socket clientSocket;
	private int clientNumber;
	private AuthService authService;
	private MessageService messageService;
	private HistoryService historyService;
	private DataOutputStream out;
	private String username;

	/**
	 * Initializes a ClientHandler instance with socket and service dependencies.
	 * @param socket The client's socket.
	 * @param clientNumber A unique identifier for the client.
	 * @param authService The authentication service for user validation.
	 * @param messageService The message service for broadcasting messages.
	 * @param historyService The history service for retrieving chat history.
	 */
	public ClientHandler(Socket socket, int clientNumber, AuthService authService, MessageService messageService, HistoryService historyService) {
		this.clientSocket = socket;
		this.clientNumber = clientNumber;
		this.authService = authService;
		this.messageService = messageService;
		this.historyService = historyService;
	}

	/**
	 * The main execution method for the client thread.
	 * Performs authentication, sends chat history, and handles message exchange.
	 * Ensures proper resource cleanup upon client disconnection.
	 */
	public void run() {
		if (!this.authService.authenticate(this.clientSocket)) {
			return;
		}
		try {
			DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			out = new DataOutputStream(clientSocket.getOutputStream());

			this.username = authService.getUsername(clientSocket);
			messageService.addClient(out);

			for (Message msg : historyService.getLastMessages(15)) {
				out.writeUTF(msg.toString());
				out.flush();
			}
			out.writeUTF("END_OF_HISTORY");

			while (true) {
				String receivedMessage = in.readUTF();
				messageService.sendMessage(this.username, clientSocket.getInetAddress().getHostAddress(), clientSocket.getPort(), receivedMessage);
			}

		} catch (IOException e) {
			System.out.println("Error handling client#" + clientNumber + ": " + e.getMessage());
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				System.out.println("Couldn't close the socket, what's going on?");
			}
			System.out.println("Connection with " + this.username + " closed");
		}
	}

}