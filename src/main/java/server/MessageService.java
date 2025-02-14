package server;
import java.io.IOException;
import java.io.DataOutputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageService {
    private final List<DataOutputStream> clients = new CopyOnWriteArrayList<>();
    private final HistoryService historyService;

    public MessageService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void addClient(DataOutputStream out) {
        clients.add(out);
    }

    public void removeClient(DataOutputStream out) {
        clients.remove(out);
    }

    public void sendMessage(String username, String ip, int port, String messageText) {
        Message message = new Message(username, ip, port, new Date(), messageText);
        historyService.addMessage(message);
        broadcast(message);
    }

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