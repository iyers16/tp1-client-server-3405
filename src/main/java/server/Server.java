package server;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.IOException;

public class Server {
	private static ServerSocket Listener;
	private static AuthService authService;
	private static MessageService messageService;
	private static HistoryService historyService;

	public static void main(String[] args) throws Exception {

		try (Scanner scanner = new Scanner(System.in)) {
			// System.out.print("Entrez l'adresse IP du serveur (ex: 127.0.0.1) : ");

			// TODO: change so that it accepts user inputs for server address and port (hardcoded for now)
			String serverAddress = "127.0.0.1";
			int serverPort = 5000;

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
