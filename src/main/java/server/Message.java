package server;

import java.util.Date;

/*
 * POJO pour recevoir, ecrire et stocker des messages
 */
public class Message {
    private String username;
    private String ip;
    private String port;
    private String timestamp;
    private String message;

    public Message(String username, String ip, Integer port, Date timestamp, String message) {
        this.username = username;
        this.ip = ip;
        this.port = String.valueOf(port);
        this.timestamp = Utils.TIMESTAMP_FORMAT.format(timestamp);
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("[%s - %s:%s - %s]: %s", this.username, this.ip, this.port, this.timestamp, this.message);
    }
}