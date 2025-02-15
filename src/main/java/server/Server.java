package server;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.IOException;
import java.util.regex.Pattern;

import static server.Utils.*;

/**
 * Server class that initializes and runs the chat server.
 * It sets up the server socket, manages core services such as authentication,
 * messaging, and history, and handles client connections.
 *
 * Responsibilities:
 * - Bind to a specified IP address and port.
 * - Initialize authentication, message, and history services.
 * - Accept and handle multiple client connections concurrently.
 */
public class Server {
	private static ServerSocket Listener;
	private static AuthService authService;
	private static MessageService messageService;
	private static HistoryService historyService;

	/**
	 * Main method that starts the server.
	 * @param args Command-line arguments.
	 * @throws Exception if an unexpected error occurs during server operation.
	 */
	public static void main(String[] args) throws Exception {

		try (Scanner scanner = new Scanner(System.in)) {

			String serverAddress;
			while (true) {
				System.out.print("Enter the server's IPv4 address (e.g., 127.0.0.1): ");
				serverAddress = scanner.nextLine().trim();
				if (Pattern.matches(IPV4_REGEX, serverAddress)) {
					break;
				} else {
					System.out.println("Invalid IPv4 address. Please try again.");
				}
			}


			int serverPort;
			while (true) {
				System.out.print("Enter server port (" + PORT_MIN + " - " + PORT_MAX + "): ");
				String portInput = scanner.nextLine().trim();
				try {
					serverPort = Integer.parseInt(portInput);
					if (serverPort >= PORT_MIN && serverPort <= PORT_MAX) {
						break;
					} else {
						System.out.println("Port number must be between " + PORT_MIN + " and " + PORT_MAX + ". Please try again.");
					}
				} catch (NumberFormatException e) {
					System.out.println("Invalid port number. Please enter a valid integer.");
				}
			}

			try {
				Listener = new ServerSocket();
				Listener.setReuseAddress(true);
				InetAddress serverIP = InetAddress.getByName(serverAddress);
				Listener.bind(new InetSocketAddress(serverIP, serverPort));

				authService = new AuthService();
				historyService = new HistoryService();
				messageService = new MessageService(historyService);
				System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);

				int clientNum = 0;
				while(true) {
					new ClientHandler(Listener.accept(), clientNum++, authService, messageService, historyService).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				Listener.close();
			}
		}
	}
}
