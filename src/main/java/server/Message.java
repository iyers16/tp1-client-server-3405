package server;

import java.util.Date;

/**
 * Message is a Plain Old Java Object (POJO) that represents a chat message.
 * It stores the details of each message including the sender's username, IP address,
 * port number, timestamp, and the message content. This class is used for creating,
 * storing, and displaying messages within the chat application.
 *
 * Responsibilities:
 * - Store message metadata such as username, IP, port, and timestamp.
 * - Format message details for display.
 */
public class Message {
    private String username;
    private String ip;
    private String port;
    private String timestamp;
    private String message;

    /**
     * Constructs a Message instance with provided user and network details.
     * @param username the sender's username
     * @param ip the sender's IP address
     * @param port the sender's port number
     * @param timestamp the time the message was sent
     * @param message the content of the message
     */
    public Message(String username, String ip, Integer port, Date timestamp, String message) {
        this.username = username;
        this.ip = ip;
        this.port = String.valueOf(port);
        this.timestamp = Utils.TIMESTAMP_FORMAT.format(timestamp);
        this.message = message;
    }

    /**
     * Returns a formatted string representation of the message.
     * @return formatted message string with metadata
     */
    @Override
    public String toString() {
        return String.format("[%s - %s:%s - %s]: %s", this.username, this.ip, this.port, this.timestamp, this.message);
    }
}