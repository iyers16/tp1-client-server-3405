package client;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Utils provides utility constants and methods used across the server-side chat application.
 * It includes validation patterns, file paths, and formatting tools to assist in various operations.
 * Responsibilities:
 * - Validate IPv4 addresses with a regular expression.
 * - Define server port range.
 * - Store file paths for user data and chat history.
 * - Provide a standard timestamp format.
 * - Retrieve file paths and create missing files automatically.
 */
public class Utils {
    public static final String IPV4_REGEX = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public static final int PORT_MIN = 5000;
    public static final int PORT_MAX = 5050;
    public static final String USER_DATA_PATH = "user_data.json";
    public static final String HISTORY_PATH = "history.json";
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");

    /**
     * Retrieves the absolute file path, creating the file if it does not exist.
     * @param fileName The name or path of the file.
     * @return The absolute path of the file.
     * @throws RuntimeException if the file cannot be created.
     */
    public static final String getFilePath(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                System.out.println("Fichier introuvable, création : " + fileName);
                file.createNewFile();
            } catch (Exception e) {
                throw new RuntimeException("Impossible de créer le fichier : " + fileName, e);
            }
        }
        return file.getAbsolutePath();
    }
}