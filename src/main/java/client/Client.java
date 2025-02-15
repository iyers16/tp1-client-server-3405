package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Client class that serves as the main entry point for the chat application.
 * Handles the connection to the server, user authentication, and chat operations.
 */
public class Client {
    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;
    /**
     * Main method to start the client application.
     * Establishes the connection, performs authentication, and starts the chat.
     *
     * @param args Command-line arguments to specify the server's IP.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ServerConfig config = ServerConnector.getServerConfig(scanner);
        String serverAddress = config.getServerAddress();
        int port = config.getPort();

        try {
            socket = new Socket(serverAddress, port);
            System.out.format("Connected to server [%s:%d]%n", serverAddress, port);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            AuthHandler authHandler = new AuthHandler(in, out, scanner);
            if (!authHandler.authenticate()) {
                System.out.println("Authentication failed. Closing connection.");
                socket.close();
                return;
            }

            ChatHandler chatHandler = new ChatHandler(in, out, scanner);
            chatHandler.fetchHistory();
            chatHandler.activateListener();

            chatHandler.activateSender();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                scanner.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}