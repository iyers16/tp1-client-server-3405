package server;
import java.io.IOException;
import java.io.DataOutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * MessageService manages the sending, broadcasting, and storage of chat messages.
 * It maintains a list of connected clients and ensures messages are broadcast
 * to all clients while being stored in the chat history.
 *
 * Responsibilities:
 * - Add and remove clients from the active connections list.
 * - Send messages to all connected clients.
 * - Store sent messages in the chat history.
 */
public class MessageService {
    private final List<DataOutputStream> clients = new CopyOnWriteArrayList<>();
    private final HistoryService historyService;

    /**
     * Constructs a MessageService with the provided HistoryService instance.
     * @param historyService the service to manage chat history
     */
    public MessageService(HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * Adds a new client to the list of active clients.
     * @param out the DataOutputStream associated with the client
     */
    public void addClient(DataOutputStream out) {
        clients.add(out);
    }

    /**
     * Removes a client from the list of active clients.
     * @param out the DataOutputStream associated with the client
     */
    public void removeClient(DataOutputStream out) {
        clients.remove(out);
    }

    /**
     * Sends a message to all connected clients and saves it to the history.
     * @param username the sender's username
     * @param ip the sender's IP address
     * @param port the sender's port number
     * @param messageText the content of the message
     */
    public void sendMessage(String username, String ip, int port, String messageText) {
        Message message = new Message(username, ip, port, new Date(), messageText);
        historyService.addMessage(message);
        broadcast(message);
    }

    /**
     * Broadcasts a message to all active clients.
     * @param message the message to broadcast
     */
    private void broadcast(Message message) {
        for (DataOutputStream out : clients) {
            try {
                out.writeUTF(message.toString());
                out.flush();
            } catch (IOException e) {
                System.err.println("Error sending message: " + e.getMessage());
            }
        }
    }
}