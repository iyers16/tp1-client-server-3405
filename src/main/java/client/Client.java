package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // System.out.print("Enter the server's IP address: ");

        // TODO: change so that it accepts user inputs for serverId and serverPort. (hardcoded for faster test)
        String serverAddress = "127.0.0.1";
        int port = 5000;

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

            // This is a while (true) loop that can only be quit if the user input "q".
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