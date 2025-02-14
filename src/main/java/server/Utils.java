package server;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;

public class Utils {
    public static final String IPV4_REGEX = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public static final int PORT_MIN = 5000;
    public static final int PORT_MAX = 5050;
    public static final String USER_DATA_PATH = "src/main/resources/user_data.json";
    public static final String HISTORY_PATH = "src/main/resources/history.json";
    public static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss");

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