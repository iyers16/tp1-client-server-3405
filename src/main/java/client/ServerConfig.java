package client;

/**
 * ServerConfig stores server connection details, including IP address and port number.
 * It acts as a data container used to pass validated server connection parameters.
 */
public class ServerConfig {
    private final String serverAddress;
    private final int port;

    /**
     * Constructs a ServerConfig object with the given server address and port.
     * @param serverAddress The IP address of the server.
     * @param port The port number used for the connection.
     */
    public ServerConfig(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    /**
     * Retrieves the server address.
     * @return The server's IP address as a String.
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * Retrieves the server port.
     * @return The server port as an integer.
     */
    public int getPort() {
        return port;
    }
}
