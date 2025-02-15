package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.net.Socket;

/**
 * AuthService handles user authentication, user data management, and session tracking.
 */
public class AuthService {
    private Map<String, String> users = new HashMap<>();
    private Map<Socket, String> authenticatedUsers = new HashMap<>();

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String USER_DATA_PATH = Utils.USER_DATA_PATH;

    /**
     * Constructor that loads existing users from the data file.
     */
    public AuthService() {
        loadUsers();
        System.out.println(GSON.toJson(users));
    }

    /**
     * Authenticates a client by prompting for username and password.
     *
     * @param clientSocket The socket representing the client connection.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticate(Socket clientSocket) {
        try {
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            out.writeUTF("Enter username: ");
            out.flush();
            String username = in.readUTF();

            out.writeUTF("Enter password: ");
            out.flush();
            String password = in.readUTF();

            if (this.isValidUser(username, password)) {
                out.writeUTF("Successful login! Welcome back " + username);
                System.out.println(username + " has logged in.");
                authenticatedUsers.put(clientSocket, username);
                return true;
            } else {
                out.writeUTF("ERROR: Incorrect username or password.");
                out.flush();
            }

        } catch (IOException e) {
            System.out.println("Authentication error: " + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * Retrieves the username associated with a given client socket.
     *
     * @param clientSocket The client socket.
     * @return The username of the authenticated client.
     */
    public String getUsername(Socket clientSocket) {
        return authenticatedUsers.get(clientSocket);
    }

    /**
     * Checks if a username-password combination is valid or creates a new user.
     *
     * @param username The provided username.
     * @param password The provided password.
     * @return true if the user is valid or newly created, false otherwise.
     */
    private boolean isValidUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, password);
            saveUsers();
            System.out.println("User created: " + username);
            return true;
        } else if (users.get(username).equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Loads user data from the JSON file.
     */
    private void loadUsers() {
        File file = new File(USER_DATA_PATH);
        if (!file.exists()) {
            System.out.println("Users data file not found. Creating a new one.");
            saveUsers();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            users = GSON.fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
            if (users == null) {
                users = new HashMap<>();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading users from file: " + Utils.USER_DATA_PATH, e);
        }
    }

    /**
     * Saves user data to the JSON file.
     */
    private void saveUsers() {
        try {
            File file = new File(USER_DATA_PATH);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            try (Writer writer = new FileWriter(USER_DATA_PATH)) {
                GSON.toJson(users, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving users to file: " + Utils.USER_DATA_PATH, e);
        }
    }
}