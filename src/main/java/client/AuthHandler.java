package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Handles user authentication for the client-side, managing login attempts.
 */
public class AuthHandler {
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner scanner;

    /**
     * Constructor to initialize the authentication handler with communication streams and user input.
     * @param in DataInputStream for reading server responses.
     * @param out DataOutputStream for sending authentication details.
     * @param scanner Scanner to collect input from the console.
     */
    public AuthHandler(DataInputStream in, DataOutputStream out, Scanner scanner) {
        this.in = in;
        this.out = out;
        this.scanner = scanner;
    }

    /**
     * Handles the user authentication process by prompting for username and password.
     * Communicates with the server for authentication validation.
     * @return true if authentication is successful, false if it fails.
     * @throws IOException if an error occurs during communication.
     */
    public boolean authenticate() throws IOException {
        System.out.print(in.readUTF() + " ");
        String username = scanner.nextLine();
        out.writeUTF(username);
        out.flush();

        System.out.print(in.readUTF() + " ");
        String password = scanner.nextLine();
        out.writeUTF(password);
        out.flush();

        String response = in.readUTF();
        System.out.println(response);

        return !response.startsWith("ERROR");
    }
}