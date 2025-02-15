package client;

import java.util.Scanner;
import java.util.regex.Pattern;

import static client.Utils.*;

/**
 * ServerConnector handles user input for configuring server connection details.
 * It ensures the entered IP address and port number are valid before returning them.
 *
 * Responsibilities:
 * - Validate the provided IPv4 address using a regex pattern.
 * - Ensure the port number falls within the defined valid range.
 * - Prompt the user repeatedly until valid input is received.
 */
public class ServerConnector {

    /**
     * Prompts the user for a valid IPv4 address and a port number within the specified range.
     * @param scanner A Scanner instance to read user input.
     * @return A ServerConfig object containing the validated server address and port.
     */
    public static ServerConfig getServerConfig(Scanner scanner) {
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

        int port;
        while (true) {
            System.out.print("Enter server port (" + PORT_MIN + " - " + PORT_MAX + "): ");
            String portInput = scanner.nextLine().trim();
            try {
                port = Integer.parseInt(portInput);
                if (port >= PORT_MIN && port <= PORT_MAX) {
                    break;
                } else {
                    System.out.println("Port number must be between " + PORT_MIN + " and " + PORT_MAX + ". Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number. Please enter a valid integer.");
            }
        }

        return new ServerConfig(serverAddress, port);
    }
}
