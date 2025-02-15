package server;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * HistoryService manages the storage and retrieval of chat history.
 * This service ensures that chat messages are persisted in a JSON file
 * and provides methods to add, retrieve, and format chat messages.
 *
 * Responsibilities:
 * - Load chat history from a JSON file.
 * - Save updated chat history back to the file.
 * - Provide access to the last 'n' messages.
 * - Add new messages to the history.
 * - Format the complete chat history as a string.
 */
public class HistoryService {
    private static Queue<Message> messages = new LinkedList<>();
    private static final String CHAT_HISTORY_PATH = Utils.HISTORY_PATH;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Initializes the HistoryService by loading existing chat history from file.
     */
    public HistoryService() {
        loadMessages();
    }

    /**
     * Loads chat messages from the JSON file.
     */
    private void loadMessages() {
        File file = new File(CHAT_HISTORY_PATH);
        if (!file.exists()) {
            System.out.println("Chat history not found, creating a new one.");
            messages = new LinkedList<>();
            saveMessages();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            messages = GSON.fromJson(reader, new TypeToken<Queue<Message>>() {}.getType());

            if (messages == null) {
                messages = new LinkedList<>();
            }
        } catch (IOException e) {
            System.err.println("Error loading chat history: " + e.getMessage());
        }
    }

    /**
     * Saves the current chat messages to the JSON file.
     */
    public void saveMessages() {
        try {
            File file = new File(CHAT_HISTORY_PATH);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            try (Writer writer = new FileWriter(file)) {
                GSON.toJson(messages, writer);
            }
        } catch (IOException e) {
            System.err.println("Error saving chat history: " + e.getMessage());
        }
    }

    /**
     * Retrieves the last specified number of chat messages.
     * @param count the number of recent messages to retrieve
     * @return a list of the most recent messages
     */
    public List<Message> getLastMessages(int count) {
        List<Message> allMessages = new LinkedList<>(messages);
        int startIndex = Math.max(0, allMessages.size() - count);
        return allMessages.subList(startIndex, allMessages.size());
    }

    /**
     * Adds a new message to the chat history and saves it.
     * @param message the message to be added
     */
    public void addMessage(Message message) {
        messages.add(message);
        saveMessages();
    }

    /**
     * Returns a formatted string of the entire chat history.
     * @return a string containing all messages
     */
    public static String getFormattedHistory() {
        StringBuilder historyBuilder = new StringBuilder();
        for (Message message : messages) {
            historyBuilder.append(message.toString()).append("\n");
        }
        return historyBuilder.toString();
    }
}